package com.abn.recipe.repository;

import com.abn.recipe.bean.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity,Integer> {


}
