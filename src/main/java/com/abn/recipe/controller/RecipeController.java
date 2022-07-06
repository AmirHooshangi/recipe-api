package com.abn.recipe.controller;

import com.abn.recipe.dto.Recipe;
import com.abn.recipe.exception.RecipeNotFoundException;
import com.abn.recipe.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recipeService.getRecipeByID(id)
                        .orElseThrow(RecipeNotFoundException::new));
    }
}
