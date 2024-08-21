package com.py.konecta.base.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenericSpecification<T> {

    public Specification<T> getSpec(T ejemplo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Field[] fields = ejemplo.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(ejemplo);

                    if (value != null) {
                        if (value instanceof String) {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(field.getName())),
                                    "%" + value.toString().toLowerCase() + "%"));
                        } else {
                            predicates.add(criteriaBuilder.equal(root.get(field.getName()), value));
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
