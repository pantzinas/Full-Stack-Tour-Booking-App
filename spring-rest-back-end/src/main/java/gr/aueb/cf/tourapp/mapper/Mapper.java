package gr.aueb.cf.tourapp.mapper;

import gr.aueb.cf.tourapp.dto.*;
import gr.aueb.cf.tourapp.model.*;
import gr.aueb.cf.tourapp.repository.TourRepository;
import gr.aueb.cf.tourapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;

    public User mapToUserEntity(UserInsertDTO dto) {
        User user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGenderType(dto.getGenderType());
        user.setRole(dto.getRole());
        user.setIsActive(dto.getIsActive());
        user.setVat(dto.getVat());

        return user;
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        UserReadOnlyDTO userReadOnlyDTO = new UserReadOnlyDTO();
        userReadOnlyDTO.setFirstname(user.getFirstname());
        userReadOnlyDTO.setLastname(user.getLastname());
        userReadOnlyDTO.setVat(user.getVat());

        return userReadOnlyDTO;
    }

    public Tour mapToTourEntity(TourInsertDTO dto) {
        Tour tour = new Tour();
        tour.setCategory(dto.getCategory());
        tour.setPrice(dto.getPrice());

        return tour;
    }

    public TourReadOnlyDTO mapToTourReadOnlyDTO(Tour tour) {
        TourReadOnlyDTO tourReadOnlyDTO = new TourReadOnlyDTO();
        tourReadOnlyDTO.setId(tour.getId());
        tourReadOnlyDTO.setCategory(tour.getCategory());
        tourReadOnlyDTO.setPrice(tour.getPrice());

        return tourReadOnlyDTO;
    }

    public Guide mapToGuideEntity(GuideInsertDTO dto) {
        Guide guide = new Guide();
        guide.setIsActive(dto.getIsActive());

        UserInsertDTO userInsertDTO = dto.getUserInsertDTO();
        User user = mapToUserEntity(userInsertDTO);
        guide.setUser(user);

        Tour tour = tourRepository.findByCategory(dto.getTourCategory()).get();
        guide.setTour(tour);

        return guide;
    }

    public GuideReadOnlyDTO mapToGuideReadOnlyDTO(Guide guide) {
        var dto = new GuideReadOnlyDTO();

        dto.setId(guide.getId());
        dto.setUuid(guide.getUuid());
        dto.setIsActive(guide.getIsActive());

        User user = guide.getUser();
        UserReadOnlyDTO userReadOnlyDTO = mapToUserReadOnlyDTO(user);
        dto.setUserReadOnlyDTO(userReadOnlyDTO);

        Tour tour = guide.getTour();
        TourReadOnlyDTO tourReadOnlyDTO = mapToTourReadOnlyDTO(tour);
        dto.setTourReadOnlyDTO(tourReadOnlyDTO);

        return dto;
    }

    public Customer mapToCustomerEntity(CustomerInsertDTO dto) {
        Customer customer = new Customer();
        customer.setIsActive(dto.getIsActive());

        UserInsertDTO userInsertDTO = dto.getUserInsertDTO();
        User user = mapToUserEntity(userInsertDTO);
        customer.setUser(user);

        return customer;
    }

    public CustomerReadOnlyDTO mapToCustomerReadOnlyDTO(Customer customer) {
        var dto = new CustomerReadOnlyDTO();

        dto.setId(customer.getId());
        dto.setUuid(customer.getUuid());
        dto.setIsActive(customer.getIsActive());

        User user = customer.getUser();
        UserReadOnlyDTO userReadOnlyDTO = mapToUserReadOnlyDTO(user);
        dto.setUserReadOnlyDTO(userReadOnlyDTO);

        return dto;
    }

    public Booking mapToBookingEntity(BookingInsertDTO dto, String username) {
        Booking booking = new Booking();
        booking.setBookingDate(dto.getBookingDate());
        // Fetch user
        User user = userRepository.findByUsername(username).get();
        booking.setCustomer(user.getCustomer());

        // Fetch tour
        Tour tour = tourRepository.findByCategory(dto.getTourCategory()).get();
        booking.setTour(tour);

        return booking;
    }

    public BookingReadOnlyDTO mapToBookingReadOnlyDTO(Booking booking) {
        BookingReadOnlyDTO dto = new BookingReadOnlyDTO();
        dto.setId(booking.getId());
        dto.setBookingDate(booking.getBookingDate());

        Customer customer = booking.getCustomer();
        CustomerReadOnlyDTO customerReadOnlyDTO = mapToCustomerReadOnlyDTO(customer);
        dto.setCustomerReadOnlyDTO(customerReadOnlyDTO);

        // To NullPointerException since during the first insert of a booking the guide column is null
        if (booking.getGuide() != null) {
            dto.setGuideReadOnlyDTO(mapToGuideReadOnlyDTO(booking.getGuide()));
        } else {
            dto.setGuideReadOnlyDTO(null);
        }

        Tour tour = booking.getTour();
        TourReadOnlyDTO tourReadOnlyDTO = mapToTourReadOnlyDTO(tour);
        dto.setTourReadOnlyDTO(tourReadOnlyDTO);

        return dto;
    }
}
