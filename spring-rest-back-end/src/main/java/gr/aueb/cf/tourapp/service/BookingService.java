package gr.aueb.cf.tourapp.service;

import gr.aueb.cf.tourapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.tourapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.tourapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.tourapp.core.specifications.BookingSpecification;
import gr.aueb.cf.tourapp.dto.BookingInsertDTO;
import gr.aueb.cf.tourapp.dto.BookingReadOnlyDTO;
import gr.aueb.cf.tourapp.mapper.Mapper;
import gr.aueb.cf.tourapp.model.Booking;
import gr.aueb.cf.tourapp.repository.*;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository bookingRepository;
    private final Mapper mapper;
    private final UserRepository userRepository;

    @Transactional(rollbackOn = {Exception.class})
    public BookingReadOnlyDTO saveBooking(BookingInsertDTO insertDTO, String username)
            throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {

        LocalDate dateToBeChecked = insertDTO.getBookingDate();

        Specification<Booking> specs = Specification
                .where(BookingSpecification.bookingDateIs(dateToBeChecked))
                .and(BookingSpecification.bookingCustomerIdIs(userRepository.findByUsername(username).get().getCustomer().getId()));

        if (!bookingRepository.findAll(specs).isEmpty()) {
            throw new AppObjectAlreadyExistsException("Booking", "The customer with username " +
                    username + " has already booked a tour on " + dateToBeChecked);
        }

        Booking bookingToBeInserted = mapper.mapToBookingEntity(insertDTO, username);
        Booking booking = bookingRepository.save(bookingToBeInserted);
        return mapper.mapToBookingReadOnlyDTO(booking);
    }

    @Transactional(rollbackOn = {Exception.class})
    public BookingReadOnlyDTO updateBookingGuide(Long bookingId, String username)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {

        Booking bookingToBeUpdated = bookingRepository.findById(bookingId).orElseThrow(() ->
                new AppObjectNotFoundException("Booking", "Booking with id " + bookingId + " was not found"));

        LocalDate dateToBeChecked = bookingToBeUpdated.getBookingDate();

        Specification<Booking> specs = Specification
                .where(BookingSpecification.bookingDateIs(dateToBeChecked))
                .and(BookingSpecification.bookingGuideIdIs(userRepository.findByUsername(username).get().getGuide().getId()));

        if (!bookingRepository.findAll(specs).isEmpty()) {
            throw new AppObjectAlreadyExistsException("Booking", "The guide with the username " +
                    username + " has already a booked tour on " + dateToBeChecked);
        }

        bookingToBeUpdated.setGuide(userRepository.findByUsername(username).get().getGuide());
        Booking updatedBooking = bookingRepository.save(bookingToBeUpdated);
        return mapper.mapToBookingReadOnlyDTO(updatedBooking);
    }

    @Transactional(rollbackOn = {Exception.class})
    public List<BookingReadOnlyDTO> findBookingByGuideUsername(String username)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {

        Specification<Booking> specs = Specification
                .where(BookingSpecification.bookingDateGreaterThanToday())
                .and(BookingSpecification.bookingGuideIdIs(userRepository.findByUsername(username).get().getGuide().getId()));

        if (bookingRepository.findAll(specs).isEmpty()) {
            throw new AppObjectAlreadyExistsException("Booking", "The guide with the username " +
                    username + " has no upcoming booked tours");
        }
        List<Booking> bookings = bookingRepository.findAll(specs);
        return bookings.stream().map(mapper::mapToBookingReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteBookingById(Long id, String username) throws AppObjectNotFoundException {
        List<BookingReadOnlyDTO> currentBookings = findBookingByBookingDateAndCustomerUsername(username);
        if (currentBookings.isEmpty()) {
            LOGGER.warn("The booking could not be deleted");
            throw new AppObjectNotFoundException("Booking", "Current bookings of the customer with username: "
                    + username + " do not exist");
        }
        Specification<Booking> specs = Specification
                .where(BookingSpecification.bookingCustomerIdIs(userRepository.findByUsername(username).get().getCustomer().getId()))
                .and(BookingSpecification.bookingDateGreaterThanToday())
                .and(BookingSpecification.bookingIdIs(id));

        if (bookingRepository.findAll(specs).isEmpty()) {
            LOGGER.warn("The booking could not be deleted");
            throw new AppObjectNotFoundException("Booking", "Current booking of customer" +
                    "with username " + username + " and with booking id " + id + " was not found");
        }
        bookingRepository.deleteById(id);
        LOGGER.info("The booking was successfully deleted");
    }

    @Transactional
    public List<BookingReadOnlyDTO> findBookingByBookingDateAndCustomerUsername(String username) throws AppObjectNotFoundException {
        Specification<Booking> specs = Specification
                .where(BookingSpecification.bookingCustomerIdIs(userRepository.findByUsername(username).get().getCustomer().getId()))
                .and(BookingSpecification.bookingDateGreaterThanToday());
        if (bookingRepository.findAll(specs).isEmpty()) {
            throw new AppObjectNotFoundException("Booking", "Current bookings of the customer with username: "
                    + username + " do not exist");
        }
        return bookingRepository.findAll(specs).stream().map(mapper::mapToBookingReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<BookingReadOnlyDTO> findBookingByBookingDateAndGuideIsNull() throws AppObjectNotFoundException {
        Specification<Booking> specs = Specification
                .where(BookingSpecification.bookingDateGreaterThanToday())
                .and(BookingSpecification.bookingGuideIsNull());
        if (bookingRepository.findAll(specs).isEmpty()) {
            throw new AppObjectNotFoundException("Booking", "Available bookings without a guide do not exist");
        }
        return bookingRepository.findAll(specs).stream().map(mapper::mapToBookingReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<BookingReadOnlyDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(mapper::mapToBookingReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Page<BookingReadOnlyDTO> getPaginatedBookings(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("bookingDate").ascending());
        return bookingRepository.findAll(pageable).map(mapper::mapToBookingReadOnlyDTO);
    }

    @Transactional
    public BookingReadOnlyDTO findBookingById(Long id) throws AppObjectNotFoundException {
        return bookingRepository.findById(id).map(mapper::mapToBookingReadOnlyDTO).orElseThrow(() ->
                new AppObjectNotFoundException("Booking", "Booking with id " + id + " was not found"));
    }
}