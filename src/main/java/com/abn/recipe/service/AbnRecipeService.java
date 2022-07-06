package com.abn.recipe.service;

import com.abn.recipe.entity.RecipeEntity;
import com.abn.recipe.dto.Recipe;
import com.abn.recipe.exception.RecipeNotFoundException;
import com.abn.recipe.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbnRecipeService implements RecipeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe newRecipe) {
        //Map recipe to Recipe Entity
        RecipeEntity recipeEntity = modelMapper.map(newRecipe, RecipeEntity.class);
        recipeRepository.save(recipeEntity);
        return newRecipe;
    }

    public Optional<Recipe> getRecipeByID(Integer id){
      return recipeRepository.findById(id).map(x -> modelMapper.map(x, Recipe.class));
    }

    public List<Recipe> fetchAllRecipes(){
        return modelMapper.map(recipeRepository.findAll(), List.class) ;
    }

    public Recipe updateRecipe(Recipe recipe){
        recipeRepository.findById(recipe.getId()).orElseThrow(RecipeNotFoundException::new);
        RecipeEntity recipeEntity = modelMapper.map(recipe, RecipeEntity.class);
        recipeRepository.save(recipeEntity);
        return recipe;
    }

    public void deleteRecipe(Integer id){
        try {
            recipeRepository.deleteById(id);
        }catch (EmptyResultDataAccessException exception){
            throw new RecipeNotFoundException();
        }
    }
}
