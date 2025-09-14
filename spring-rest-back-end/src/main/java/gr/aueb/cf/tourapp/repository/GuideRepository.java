package gr.aueb.cf.tourapp.repository;

import gr.aueb.cf.tourapp.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long>, JpaSpecificationExecutor<Guide> {

    Optional<Guide> findByUserId(Long id);
    Optional<Guide> findByUuid(String uuid);
}
