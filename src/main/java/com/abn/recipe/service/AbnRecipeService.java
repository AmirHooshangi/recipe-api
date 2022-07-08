package com.abn.recipe.service;

import com.abn.recipe.entity.RecipeEntity;
import com.abn.recipe.dto.Recipe;
import com.abn.recipe.exception.RecipeNotFoundException;
import com.abn.recipe.repository.RecipeRepository;
import com.abn.recipe.repository.RecipeSpecification;
import com.abn.recipe.repository.SearchCriteria;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import java.lang.reflect.Type;

import java.util.List;
import java.util.Optional;

/**
 *
 *  Service implementation of RecipeService interface.
 *  This class provides CRUD functionality for Recipes as well as
 *  writing more complex search filter queries to find Recipes.
 *
 * @see RecipeService
 * */
@Service
public class AbnRecipeService implements RecipeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    RecipeRepository recipeRepository;

    /**
     *  This method creates the incoming Recipe to the database.
     *  ID should be provided by the caller.
     * @param recipe  recipe object to be stored.
     * @return Recipe returns the saved (incoming) object
     * */
    public Recipe createRecipe(Recipe recipe) {
        RecipeEntity recipeEntity = modelMapper.map(recipe, RecipeEntity.class);
        recipeRepository.save(recipeEntity);
        return recipe;
    }

    /**
     *  Method that finds a recipe by its id;
     * @param id an integer value which is the recipes id
     * @return Optional<Recipe>
     * */
    public Optional<Recipe> getRecipeByID(Integer id){
      return recipeRepository.findById(id).map(x -> modelMapper.map(x, Recipe.class));
    }

    /**
     * Method to get all recipes.
     * In the upcoming release it will support pagination
     * @return List<recipe> list all recipes
     * */
    public List<Recipe> fetchAllRecipes(){
        Type listType = new TypeToken<List<Recipe>>(){}.getType();
        return modelMapper.map(recipeRepository.findAll(), listType) ;
    }

    /**
     * Updating current recipe by its id. If the corresponding id is not in the database
     * it will raise RecipeNotFoundException
     *
     * @see RecipeNotFoundException
     * @param recipe recipe with values to be updated
     * @return an updated recipe
     * */
    public Recipe updateRecipe(Recipe recipe){
        recipeRepository.findById(recipe.getId()).orElseThrow(RecipeNotFoundException::new);
        RecipeEntity recipeEntity = modelMapper.map(recipe, RecipeEntity.class);
        recipeRepository.save(recipeEntity);
        return recipe;
    }

    /**
     *  Deleting a recipe based on its id
     * @param id the id which should be deleted
     * */
    public void deleteRecipe(Integer id){
        try {
            recipeRepository.deleteById(id);
        }catch (EmptyResultDataAccessException exception){
            throw new RecipeNotFoundException();
        }
    }

    /**
     * This method accepts dynamic search query in the following format:
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
     * @param complexQuery A query string which contains multiple search criteria
     *
     * */
    public List<Recipe> dynamicSearch(String complexQuery){

        String[] queries = complexQuery.split(";");
        String[] query = queries[0].split(" ");
        String key = query[0];
        String operation = query[1];
        String value = query[2];

        RecipeSpecification firstClause =
                new RecipeSpecification(new SearchCriteria(key, operation, value));
        Specification<RecipeEntity> specification =  Specification.where(firstClause);

        for(int i = 1; i < queries.length; i++) {
            String[] query1 = queries[i].split(" ");
            String key1 = query1[0];
            String operation1 = query1[1];
            String value1 = query1[2];
            specification = Specification.where(firstClause).and(new RecipeSpecification(new SearchCriteria(key1, operation1, value1)));
        }
        List<RecipeEntity> results =
                recipeRepository.findAll(specification);
        Type listType = new TypeToken<List<Recipe>>(){}.getType();
        return modelMapper.map(results, listType) ;

    }
}
