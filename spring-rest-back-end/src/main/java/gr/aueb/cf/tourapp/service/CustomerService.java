package gr.aueb.cf.tourapp.service;

import gr.aueb.cf.tourapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.tourapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.tourapp.core.filters.CustomerFilters;
import gr.aueb.cf.tourapp.core.filters.Paginated;
import gr.aueb.cf.tourapp.core.specifications.CustomerSpecification;
import gr.aueb.cf.tourapp.dto.CustomerInsertDTO;
import gr.aueb.cf.tourapp.dto.CustomerReadOnlyDTO;
import gr.aueb.cf.tourapp.mapper.Mapper;
import gr.aueb.cf.tourapp.model.Customer;
import gr.aueb.cf.tourapp.repository.CustomerRepository;
import gr.aueb.cf.tourapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = {Exception.class})
    public CustomerReadOnlyDTO saveCustomer(CustomerInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {
        String vatToBeChecked = insertDTO.getUserInsertDTO().getVat();
        if (userRepository.findByVat(vatToBeChecked).isPresent()) throw new AppObjectAlreadyExistsException("Customer", "Customer with vat number " + vatToBeChecked + " already exists");

        String usernameToBeChecked = insertDTO.getUserInsertDTO().getUsername();
        if (userRepository.findByUsername(usernameToBeChecked).isPresent()) throw new AppObjectAlreadyExistsException("Customer", "Customer with username " + usernameToBeChecked + " already exists");

        Customer customer = mapper.mapToCustomerEntity(insertDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.mapToCustomerReadOnlyDTO(savedCustomer);
    }

    @Transactional
    public List<CustomerReadOnlyDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(mapper::mapToCustomerReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Page<CustomerReadOnlyDTO> getPaginatedCustomers(int page, int pageSize) {
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(page, pageSize, sort.ascending());
        return customerRepository.findAll(pageable).map(mapper::mapToCustomerReadOnlyDTO);
    }

    @Transactional
    public Page<CustomerReadOnlyDTO> getPaginatedSortedCustomers(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection) ,sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return customerRepository.findAll(pageable).map(mapper::mapToCustomerReadOnlyDTO);
    }

    @Transactional
    public List<CustomerReadOnlyDTO> getCustomersFiltered(CustomerFilters filters) {
        var filtered = customerRepository.findAll(getSpecsFromFilters(filters));
        return filtered.stream().map(mapper::mapToCustomerReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Paginated<CustomerReadOnlyDTO> getCustomersFilteredPaginated(CustomerFilters filters) {
        var filtered = customerRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToCustomerReadOnlyDTO));
    }

    private Specification<Customer> getSpecsFromFilters(CustomerFilters filters) {
        return Specification
                .where(CustomerSpecification.customerFieldLike("uuid", filters.getUuid()))
                .and(CustomerSpecification.customerIsActive(filters.getIsActive()))
                .and(CustomerSpecification.customerVatIs(filters.getUserVat()))
                .and(CustomerSpecification.customerUserLastnameIs(filters.getUserLastname()));
    }

}
