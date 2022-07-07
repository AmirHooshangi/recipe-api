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

    public List<Recipe> dynamicSearch(String complexQuery){

        String[] queries = complexQuery.split(";");
        String[] query = queries[0].split(" ");
        String key = query[0];
        String operation = query[1];
        String value = query[2];

        RecipeSpecification spec1 =
                new RecipeSpecification(new SearchCriteria(key, operation, value));

        Specification<RecipeEntity> specification =  Specification.where(spec1);

        for(int i = 1; i < queries.length; i++) {

            String[] query1 = queries[i].split(" ");
            String key1 = query1[0];
            String operation1 = query1[1];
            String value1 = query1[2];

            specification = Specification.where(spec1).and(new RecipeSpecification(new SearchCriteria(key1, operation1, value1)));
        }
        List<RecipeEntity> results =
                recipeRepository.findAll(specification);

        Type listType = new TypeToken<List<Recipe>>(){}.getType();
        return modelMapper.map(results, listType) ;

    }
}
