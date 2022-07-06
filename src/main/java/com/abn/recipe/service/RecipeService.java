package com.abn.recipe.service;

import com.abn.recipe.dto.Recipe;
import com.abn.recipe.entity.RecipeEntity;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    Recipe createRecipe(Recipe newRecipe);

    Optional<Recipe> getRecipeByID(Integer id);

    List<Recipe> fetchAllRecipes();

    Recipe updateRecipe(Recipe recipe);

    void deleteRecipe(Integer id);

}
