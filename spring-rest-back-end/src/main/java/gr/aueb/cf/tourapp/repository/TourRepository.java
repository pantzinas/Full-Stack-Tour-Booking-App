package gr.aueb.cf.tourapp.repository;

import gr.aueb.cf.tourapp.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {

    Optional<Tour> findByCategory(String Category);
}
