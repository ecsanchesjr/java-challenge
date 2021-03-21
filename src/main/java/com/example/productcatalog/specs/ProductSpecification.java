package com.example.productcatalog.specs;

import com.example.productcatalog.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductSpecification implements Specification<Product> {

    private final List<SearchCriteria> searchCriteriaList = new ArrayList<>();

    public ProductSpecification(Map<String, String> searchParams) {
        searchParams.forEach( (key, value) -> {
            if(!value.isEmpty() && SearchOperation.getOperationParams().contains(key)) {
                Arrays.stream(SearchOperation.getByParam(key).getFields()).forEach(field ->
                    searchCriteriaList.add(new SearchCriteria(field, value, SearchOperation.getByParam(key)))
                );
            }
        });
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        searchCriteriaList.forEach(searchCriteria -> {
            predicates.add(createSpecification(searchCriteria).toPredicate(root, criteriaQuery, criteriaBuilder));
        });
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Specification<Product> createSpecification(SearchCriteria searchCriteria) {
        switch (searchCriteria.getOperation()) {
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(searchCriteria.getKey())),
                                "%" + searchCriteria.getValue().toLowerCase() + "%");

            case LESS_OR_EQUAL:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get(searchCriteria.getKey()), searchCriteria.getValue()
                        );

            case GREATER_OR_EQUAL:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(searchCriteria.getKey()), searchCriteria.getValue()
                        );

            default:
                throw new RuntimeException("Operation not supported yet.");
        }
    }
}
