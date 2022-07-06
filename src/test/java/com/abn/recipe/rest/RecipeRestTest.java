package com.abn.recipe.rest;


import com.abn.recipe.controller.RecipeController;
import com.abn.recipe.dto.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void GivenCorrectRecipe_WhenPosted_ThenResponseIsCreated_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("You cook it easily.");

        this.mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipe)))
                .andDo(print())
                .andExpect(status().isCreated());
    }



    @Test
    public void GivenCorrectRecipeId_WhenGet_ThenResponseIsOK_Test() throws Exception {
        this.mockMvc.perform(get("/api/recipe/1"))
                        .andDo(print())
                .andExpect(status().isOk());
        }
}
