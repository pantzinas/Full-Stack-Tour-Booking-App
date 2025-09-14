package gr.aueb.cf.tourapp.service;

import gr.aueb.cf.tourapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.tourapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.tourapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.tourapp.dto.TourInsertDTO;
import gr.aueb.cf.tourapp.dto.TourReadOnlyDTO;
import gr.aueb.cf.tourapp.mapper.Mapper;
import gr.aueb.cf.tourapp.model.Tour;
import gr.aueb.cf.tourapp.repository.TourRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourService {

    public static final Logger LOGGER = LoggerFactory.getLogger(TourService.class);
    public final TourRepository tourRepository;
    public final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public TourReadOnlyDTO saveTour(TourInsertDTO tourInsertDTO) throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {

        if (tourRepository.findByCategory(tourInsertDTO.getCategory()).isPresent()) {
            throw new AppObjectAlreadyExistsException("Tour", "The tour of the category " + tourInsertDTO.getCategory() + " already exists");
        }

        Tour tourToBeInserted = mapper.mapToTourEntity(tourInsertDTO);
        Tour savedTour = tourRepository.save(tourToBeInserted);
        return mapper.mapToTourReadOnlyDTO(savedTour);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteByTourId(Long id) throws AppObjectNotFoundException {

        if (tourRepository.findById(id).isEmpty()) {
            throw new AppObjectNotFoundException("Tour", "Tour with id " + id + " do not exist");
        }

        tourRepository.deleteById(id);
        LOGGER.info("The tour was successfully deleted");
    }

    @Transactional
    public List<TourReadOnlyDTO> getAllTours() {
        return tourRepository.findAll().stream().map(mapper::mapToTourReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public TourReadOnlyDTO findTourById(Long id) {
        return tourRepository.findById(id).map(mapper::mapToTourReadOnlyDTO).orElseThrow(() ->
                new AppObjectNotFoundException("Tour", "Tour with id " + id + " do not exist"));
    }
}
