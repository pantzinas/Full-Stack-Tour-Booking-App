package gr.aueb.cf.tourapp.core.specifications;

import gr.aueb.cf.tourapp.model.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class BookingSpecification {

    private BookingSpecification() {

    }

    public static Specification<Booking> bookingIdIs(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("id"), id);
        });
    }

    public static Specification<Booking> bookingDateIs(LocalDate date) {
        return ((root, query, criteriaBuilder) -> {
            if (date == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("bookingDate"), date);
        });
    }

    public static Specification<Booking> bookingDateGreaterThanToday() {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get("bookingDate"), LocalDate.now());
        });
    }

    public static Specification<Booking> bookingPriceLessThan(Double price) {
        return ((root, query, criteriaBuilder) -> {
            if (price == null || price == 0) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.lessThan(root.get("price"), price);
        });
    }

    public static Specification<Booking> bookingTourCategoryIs(String category) {
        return ((root, query, criteriaBuilder) -> {
            if (category == null || category.isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Booking, Tour> tour = root.join("tour");
            return criteriaBuilder.equal(tour.get("category"), category);
        });
    }

    public static Specification<Booking> bookingCustomerIdIs(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null || id == 0) criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Booking, Customer> customer = root.join("customer");
            return criteriaBuilder.equal(customer.get("id"), id);
        });
    }

    public static Specification<Booking> bookingCustomerLastnameIs(String lastname) {
        return ((root, query, criteriaBuilder) -> {
            if (lastname == null || lastname.isEmpty()) criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Booking, Customer> customer = root.join("customer");
            Join<Customer, User> user = customer.join("user");
            return criteriaBuilder.equal(user.get("lastname"), lastname);
        });
    }

    public static Specification<Booking> bookingGuideIdIs(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null || id == 0) criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Booking, Guide> guide = root.join("guide");
            return criteriaBuilder.equal(guide.get("id"), id);
        });
    }

    public static Specification<Booking> bookingGuideIsNull() {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNull(root.get("guide"));
        });
    }
}
