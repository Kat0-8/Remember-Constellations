package com.example.rememberconstellations.utilities.specifications;

import com.example.rememberconstellations.models.Constellation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ConstellationSpecification {

    private ConstellationSpecification() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Specification<Constellation> withName(String name) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(name)
                ? criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%") : null;
    }

    public static Specification<Constellation> withAbbreviation(String abbreviation) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(abbreviation)
                ? criteriaBuilder.like(criteriaBuilder.lower(root.get("abbreviation")), "%" + abbreviation.toLowerCase() + "%") : null;
    }

    public static Specification<Constellation> withFamily(String family) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(family)
                ? criteriaBuilder.like(criteriaBuilder.lower(root.get("family")), "%" + family.toLowerCase() + "%") : null;
    }

    public static Specification<Constellation> withRegion(String region) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(region)
                ? criteriaBuilder.like(criteriaBuilder.lower(root.get("region")), "%" + region.toLowerCase() + "%") : null;
    }
}
