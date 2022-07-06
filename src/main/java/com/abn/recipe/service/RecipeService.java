package com.abn.recipe.service;

import com.abn.recipe.dto.Recipe;

import java.util.Optional;

public interface RecipeService {

    Recipe createRecipe(Recipe newRecipe);

    Optional<Recipe> getRecipeByID(Integer id);

}
