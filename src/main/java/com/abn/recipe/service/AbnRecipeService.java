package com.abn.recipe.service;

import com.abn.recipe.entity.RecipeEntity;
import com.abn.recipe.dto.Recipe;
import com.abn.recipe.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        return null;
    }

    public Optional<Recipe> getRecipeByID(Integer id){
      return recipeRepository.findById(id).map(x -> modelMapper.map(x, Recipe.class));
    }

    public List<Recipe> fetchAllRecipes(){
        return modelMapper.map(recipeRepository.findAll(), List.class) ;
    }

}
