package com.example.rememberconstellations.utilities;

import com.example.rememberconstellations.models.Star;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class StarSpecification {

    public static Specification<Star> withName(String name) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(name)
                ? criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%") : null;
    }

    public static Specification<Star> withType(String type) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(type)
                ? criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), "%" + type.toLowerCase() + "%") : null;
    }

    public static Specification<Star> withMassGreaterThanOrEqual(Double mass) {
        return (root, query, criteriaBuilder) ->
                mass != null
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("mass"), mass) : null;
    }

    public static Specification<Star> withRadiusGreaterThanOrEqual(Double radius) {
        return (root, query, criteriaBuilder) ->
                radius != null
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("radius"), radius) : null;
    }

    public static Specification<Star> withTemperatureGreaterThanOrEqual(Double temperature) {
        return (root, query, criteriaBuilder) ->
                temperature != null
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("temperature"), temperature) : null;
    }

    public static Specification<Star> withLuminosityGreaterThanOrEqual(Double luminosity) {
        return (root, query, criteriaBuilder) ->
                luminosity != null
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("luminosity"), luminosity) : null;
    }

    public static Specification<Star> withRightAscensionGreaterThanOrEqual(Double rightAscension) {
        return (root, query, criteriaBuilder) ->
                rightAscension != null
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("rightAscension"), rightAscension) : null;
    }

    public static Specification<Star> withDeclinationGreaterThanOrEqual(Double declination) {
        return (root, query, criteriaBuilder) ->
                declination != null
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("declination"), declination) : null;
    }

    public static Specification<Star> withPositionInConstellation(String positionInConstellation) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(positionInConstellation)
                ? criteriaBuilder.like(criteriaBuilder.lower(root.get("positionInConstellation")), "%"
                        + positionInConstellation.toLowerCase() + "%") : null;
    }

}
