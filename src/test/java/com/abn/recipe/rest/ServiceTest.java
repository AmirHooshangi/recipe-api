package com.abn.recipe.rest;

import com.abn.recipe.dto.Recipe;
import com.abn.recipe.entity.DishType;
import com.abn.recipe.entity.RecipeEntity;
import com.abn.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ServiceTest {

    @Autowired
    RecipeService recipeService;


    @Test
    public void GivenNonVegetarianSearchQuery_ResponseIsRegularRecipes(){

        mockData();

        String searchQuery = "type != VEGETARIAN;";
        List<Recipe> recipes = recipeService.dynamicSearch(searchQuery);

        assertThat(recipes).extracting(x -> x.getType().toString())
                .as("Recipe type is not equal to VEGETERIAN")
                .contains("REGULAR");
    }


    @Test
    public void GivenTypeAndInstructionsSearchQuery_ResponseValidRecipes(){

        mockData();

        String searchQuery = "type != VEGETARIAN;" +
                "instructions ~= oil";
        List<Recipe> recipes = recipeService.dynamicSearch(searchQuery);

        assertThat(recipes).extracting(x -> x.getInstructions().toString())
                .as("Recipe is for Burger")
                .contains("You need a lot of oil.");


        assertThat(recipes).extracting(x -> x.getName())
                .as("Recipe is for Burger")
                .contains("Burger");
    }

    @Test
    public void GivenNotExistedSearchQuery_ResponseEmptyList(){

        mockData();
        String searchQuery = "type == VEGETARIAN;" +
                "instructions ~= shalgham";
        List<Recipe> recipes = recipeService.dynamicSearch(searchQuery);

        assertThat(recipes).extracting(x -> x.getName())
                .as("Recipe is for nothing")
                .isNullOrEmpty();
    }

    public void mockData(){
        Recipe recipe = new Recipe();
        recipe.setId(56);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very difficult Pizza");
        recipe.setIngredients("Cheese 2gr, Bread 1kg");
        recipe.setType(DishType.REGULAR);
        recipeService.createRecipe(recipe);

        Recipe recipe2 = new Recipe();
        recipe2.setId(92);
        recipe2.setName("Rashti Pizza");
        recipe2.setInstructions("Very easy pizza in the Oven");
        recipe2.setIngredients("Cheese 2gr, Bread 1kg");
        recipe2.setType(DishType.VEGETARIAN);
        recipeService.createRecipe(recipe2);

        Recipe recipe3 = new Recipe();
        recipe3.setId(92);
        recipe3.setName("Burger");
        recipe3.setInstructions("You need a lot of oil.");
        recipe3.setIngredients("Beef Meet 2kg, Bread 2 pieces");
        recipe3.setType(DishType.REGULAR);
        recipeService.createRecipe(recipe3);

    }
}
