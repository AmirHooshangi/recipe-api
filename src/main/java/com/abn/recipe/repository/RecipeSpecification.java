package com.abn.recipe.repository;

import com.abn.recipe.entity.DishType;
import com.abn.recipe.entity.RecipeEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class RecipeSpecification implements Specification<RecipeEntity> {

    private SearchCriteria criteria;

    public RecipeSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<RecipeEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase("==")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.equal(
                        root.<String>get(criteria.getKey()), criteria.getValue());
            }else if((root.get(criteria.getKey()).getJavaType() == DishType.class)) {
                return builder.equal(root.get(criteria.getKey()), DishType.valueOf(criteria.getValue().toString()) );
            }

        } else if (criteria.getOperation().equalsIgnoreCase("~=")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            }
        } else if (criteria.getOperation().equalsIgnoreCase("!=")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.notEqual(
                        root.<String>get(criteria.getKey()), criteria.getValue());
            } else if((root.get(criteria.getKey()).getJavaType() == DishType.class)) {
                return builder.notEqual(root.get(criteria.getKey()), DishType.valueOf(criteria.getValue().toString()) );
            }
        }
        return null;
    }
}


