package gr.aueb.cf.tourapp.repository;

import gr.aueb.cf.tourapp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Optional<Booking> findByUuid(String uuid);
    Optional<Booking> findByGuideId(Long id);
    Optional<Booking> findByCustomerId(Long id);
    Optional<Booking> findByBookingDate(LocalDate bookingDate);
}
