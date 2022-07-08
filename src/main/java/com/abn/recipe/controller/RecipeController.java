package com.abn.recipe.controller;

import com.abn.recipe.dto.Recipe;
import com.abn.recipe.entity.RecipeEntity;
import com.abn.recipe.exception.RecipeNotFoundException;
import com.abn.recipe.repository.RecipeRepository;
import com.abn.recipe.repository.RecipeSpecification;
import com.abn.recipe.repository.SearchCriteria;
import com.abn.recipe.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  REST Controller to support CRUD operations for Recipes
 *  and complex search queries.
 *
 * @see RecipeService
 * @see Recipe
 *
 * */
@RestController
@RequestMapping("/api")
@Slf4j
public class RecipeController {

    @Autowired
    RecipeService recipeService;


    /**
     *  POST REST endpoint creates the incoming Recipe to the database.
     *  ID should be provided by the caller.
     * @param recipe  recipe json to be stored.
     * @return Recipe returns the saved (incoming) object
     * */
    @PostMapping("/recipe")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe){
        log.info("Request to create new recipe with id: " + recipe.getId());
        Recipe savedRecipe = recipeService.createRecipe(recipe);
        log.info("New recipe saved successfully with id: " + recipe.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        }

    /**
     *  This controller accepts dynamic search query in the following format:
     *  key == value; each query ends with ";" and is separated by a space " "
     *  if more than one criterion is needed we can chain them in this order:
     *  type != REGULAR; for getting all vegetarian recipes.
     *  instructions ~= Oven;type != REGULAR;
     *  Its equivalent to "SELECT * FROM RECIPE WHERE servingNumber = 10 AND type like "%Oven%"
     * Currently it supports following operators:
     *   (  ~=  )  => which works like SQL 'LIKE' statement
     *   (  !=  ) => which works like SQL '<>' statement
     *   (  ==  ) => which works like SQL '=' statement
     *
     * @param searchQuery A query string which contains multiple search criteria
     * @return List<Recipe> list of found recipes.
     * */
    @GetMapping("/search")
    public List<Recipe> search(@RequestParam String searchQuery){
        log.info("/search finding recipes with this search Query: " + searchQuery);
        return recipeService.dynamicSearch(searchQuery);
    }

    /**
     * PUT REST endpoint to update current recipe by its id. If the corresponding id is not in the database
     * it will raise RecipeNotFoundException
     *
     * @see RecipeNotFoundException
     * @param recipe recipe with values to be updated
     * @return an updated recipe
     * */
    @PutMapping("/recipe")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe){
        log.info("Request to update a recipe with id: " + recipe.getId());
        Recipe savedRecipe = recipeService.updateRecipe(recipe);
        log.info("Recipe update successfully with id: " + recipe.getId());
        return ResponseEntity.status(HttpStatus.OK).body(savedRecipe);
    }

    /**
     *  GET REST endpoint that finds a recipe by its id;
     * @param id an integer value which is the recipes id
     * @return Optional<Recipe>
     * */
    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        log.info("Request to get a recipe for id: " + id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(recipeService.getRecipeByID(id)
                        .orElseThrow(RecipeNotFoundException::new));
    }

    /**
     * GET REST endpoint to get all recipes.
     * In the upcoming release it will support pagination
     * @return List<recipe> list all recipes
     * */
    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        log.info("Request to get All recipes");
        return ResponseEntity.status(HttpStatus.OK)
                .body(recipeService.fetchAllRecipes());
    }

    /**
     *  DELETE REST endpoint to delete a recipe based on its id
     * @param id the id which should be deleted
     * */
    @DeleteMapping("/recipe/{id}")
    public ResponseEntity deleteRecipe(@PathVariable Integer id) {
        log.info("Request to delete a recipe for id: " + id);
        recipeService.deleteRecipe(id);
        log.info("Recipe deleted successfully with id: " + id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
