package com.abn.recipe.rest;


import com.abn.recipe.controller.RecipeController;
import com.abn.recipe.dto.Recipe;
import com.abn.recipe.entity.DishType;
import com.abn.recipe.repository.RecipeRepository;
import com.abn.recipe.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RecipeController controller;

    //for mocking
    @Autowired
    RecipeService mockingService;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void GivenCorrectRecipes_WhenPosted_ThenResponseIsCreated_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very difficult pizza");
        recipe.setType(DishType.REGULAR);

        this.mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipe)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void GivenCorrectRecipeId_WhenGet_ThenResponseIsOK_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very difficult pizza");
        recipe.setType(DishType.REGULAR);
        mockingService.createRecipe(recipe);

        this.mockMvc.perform(get("/api/recipe/1"))
                        .andDo(print())
                .andExpect(status().isOk());
        }

    @Test
    public void GivenWrongRecipeId_WhenGet_ThenResponseIsNotFound_Test() throws Exception {
        this.mockMvc.perform(get("/api/recipe/6666"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void AllRecipes_WhenGet_ThenResponseRecipes_Test() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(56);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very difficult pizza");
        recipe.setType(DishType.REGULAR);
        mockingService.createRecipe(recipe);

        Recipe recipe2 = new Recipe();
        recipe2.setId(92);
        recipe2.setName("Rashti Pizza");
        recipe2.setInstructions("Very easy pizza");
        recipe2.setType(DishType.VEGETARIAN);
        mockingService.createRecipe(recipe2);

        this.mockMvc.perform(get("/api/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("\"id\":92")));
    }


    @Test
    public void GivenExistedRecipe_WhenPutted_ThenResponseIsChanged_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(555);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very easy pizza");
        recipe.setType(DishType.VEGETARIAN);
        mockingService.createRecipe(recipe);

        recipe.setName("Rashti Pizza");
        recipe.setInstructions("Very difficult pizza");
        recipe.setType(DishType.REGULAR);

        this.mockMvc.perform(put("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipe)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("\"instructions\":\"Very difficult pizza\"")))
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("\"type\":\"REGULAR\"")))
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("\"name\":\"Rashti Pizza\"")));
    }

    @Test
    public void GivenNotExistedRecipe_WhenPutted_ThenResponseNotFoundError_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(555);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very easy pizza");
        recipe.setType(DishType.VEGETARIAN);
        mockingService.createRecipe(recipe);

        recipe.setId(555555);
        recipe.setName("Rashti Pizza");
        recipe.setInstructions("Very difficult pizza");
        recipe.setType(DishType.REGULAR);

        this.mockMvc.perform(put("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipe)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
