package gr.aueb.cf.tourapp.core.specifications;

import gr.aueb.cf.tourapp.model.Guide;
import gr.aueb.cf.tourapp.model.Tour;
import gr.aueb.cf.tourapp.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class GuideSpecification {

    private GuideSpecification() {

    }

    public static Specification<Guide> guideUserVatIs(String vat) {
        return ((root, query, criteriaBuilder) -> {
            if (vat == null || vat.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Guide, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("vat"), vat);
        });
    }

    public static Specification<Guide> guideIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        });
    }

    public static Specification<Guide> guideUserLastnameIs(String lastname) {
        return ((root, query, criteriaBuilder) -> {
            if (lastname == null || lastname.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Guide, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("lastname"), lastname);
        });
    }

    public static Specification<Guide> guideTourIs(String tourCategory) {
        return ((root, query, criteriaBuilder) -> {
            if (tourCategory == null || tourCategory.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Guide, Tour> tour = root.join("tour");
            return criteriaBuilder.equal(tour.get("category"), tourCategory);
        });
    }

    public static Specification<Guide> guideFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(root.get(field), "%" + value.toUpperCase() + "%");
        });
    }
}
