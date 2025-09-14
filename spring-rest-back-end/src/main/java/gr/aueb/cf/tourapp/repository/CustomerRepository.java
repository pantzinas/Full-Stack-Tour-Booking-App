package gr.aueb.cf.tourapp.repository;

import gr.aueb.cf.tourapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByUserId(Long id);
    Optional<Customer> findByUuid(String uuid);

}
