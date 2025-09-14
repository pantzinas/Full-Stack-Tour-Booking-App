package gr.aueb.cf.tourapp.core.specifications;

import gr.aueb.cf.tourapp.model.Customer;
import gr.aueb.cf.tourapp.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {

    private CustomerSpecification() {

    }

    public static Specification<Customer> customerVatIs(String vat) {
        return ((root, query, criteriaBuilder) ->
        {
            if (vat == null || vat.isEmpty()) criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Customer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("vat"), vat);
        });
    }

    public static Specification<Customer> customerIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null) criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        });
    }

    public static Specification<Customer> customerUserLastnameIs(String lastname) {
        return ((root, query, criteriaBuilder) -> {
            if (lastname == null || lastname.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Customer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("lastname"), lastname);
        });
    }

    public static Specification<Customer> customerFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) ->
        {
            if (value == null || value.isEmpty()) criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(root.get(field), "%" + value.toUpperCase() + "%");
        });
    }
}
