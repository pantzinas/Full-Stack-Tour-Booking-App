package gr.aueb.cf.tourapp.service;

import gr.aueb.cf.tourapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.tourapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.tourapp.core.filters.GuideFilters;
import gr.aueb.cf.tourapp.core.filters.Paginated;
import gr.aueb.cf.tourapp.core.specifications.GuideSpecification;
import gr.aueb.cf.tourapp.dto.GuideInsertDTO;
import gr.aueb.cf.tourapp.dto.GuideReadOnlyDTO;
import gr.aueb.cf.tourapp.mapper.Mapper;
import gr.aueb.cf.tourapp.model.Guide;
import gr.aueb.cf.tourapp.repository.GuideRepository;
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
public class GuideService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GuideService.class);
    private final GuideRepository guideRepository;
    private final Mapper mapper;
    private final UserRepository userRepository;

    @Transactional(rollbackOn = {Exception.class})
    public GuideReadOnlyDTO saveGuide(GuideInsertDTO guideInsertDTO)
            throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {
        String vatToBeChecked = guideInsertDTO.getUserInsertDTO().getVat();
        if ((userRepository.findByVat(vatToBeChecked)).isPresent()) throw new AppObjectAlreadyExistsException("Guide", "Guide with vat number " + vatToBeChecked + " already exists");

        String usernameToBeChecked = guideInsertDTO.getUserInsertDTO().getUsername();
        if ((userRepository.findByUsername(usernameToBeChecked)).isPresent()) throw new AppObjectAlreadyExistsException("Guide", "Guide with username " + usernameToBeChecked + " already exists");

        Guide guide = mapper.mapToGuideEntity(guideInsertDTO);
        Guide savedGuide = guideRepository.save(guide);

        return mapper.mapToGuideReadOnlyDTO(savedGuide);
    }

    @Transactional
    public List<GuideReadOnlyDTO> getAllGuides() {
        return guideRepository.findAll().stream().map(mapper::mapToGuideReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Page<GuideReadOnlyDTO> getPaginatedGuides(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return guideRepository.findAll(pageable).map(mapper::mapToGuideReadOnlyDTO);
    }

    @Transactional
    public Page<GuideReadOnlyDTO> getPaginatedSortedGuides(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return guideRepository.findAll(pageable).map(mapper::mapToGuideReadOnlyDTO);
    }

    @Transactional
    public List<GuideReadOnlyDTO> getGuidesFiltered(GuideFilters filters) {
        var filtered = guideRepository.findAll(getSpecsFromFilters(filters));
        return filtered.stream().map(mapper::mapToGuideReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Paginated<GuideReadOnlyDTO> getGuidesFilteredPaginated(GuideFilters filters) {
        var filtered = guideRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToGuideReadOnlyDTO));
    }

    private Specification<Guide> getSpecsFromFilters(GuideFilters filters) {
        return Specification
                .where(GuideSpecification.guideFieldLike("uuid", filters.getUuid()))
                .and(GuideSpecification.guideUserVatIs(filters.getUserVat()))
                .and(GuideSpecification.guideTourIs(filters.getTourCategory()))
                .and(GuideSpecification.guideIsActive(filters.getIsActive()));
    }
}
