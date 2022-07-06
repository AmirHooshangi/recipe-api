package com.abn.recipe.controller;

import com.abn.recipe.dto.Recipe;
import com.abn.recipe.exception.RecipeNotFoundException;
import com.abn.recipe.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping("/recipe")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe){
            Recipe savedRecipe = recipeService.createRecipe(recipe);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        }

    @PutMapping("/recipe")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe){
        Recipe savedRecipe = recipeService.updateRecipe(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(savedRecipe);
    }


    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recipeService.getRecipeByID(id)
                        .orElseThrow(RecipeNotFoundException::new));
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recipeService.fetchAllRecipes());
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity deleteRecipe(@PathVariable Integer id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
