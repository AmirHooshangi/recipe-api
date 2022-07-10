package com.abn.recipe.rest;


import com.abn.recipe.controller.RecipeController;
import com.abn.recipe.dto.AuthenticationRequest;
import com.abn.recipe.dto.Recipe;
import com.abn.recipe.entity.DishType;
import com.abn.recipe.repository.UserRepository;
import com.abn.recipe.service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
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

    @Autowired
    UserRepository users;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    private String token;

    @BeforeEach
    public void getToken() throws Exception {

        String response = this.mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().
                                writeValueAsString(AuthenticationRequest.builder()
                                        .username("admin").password("@#:OJFL:OI:#J@#@#:IJ#@#OJ#").build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        token = JsonPath.read(response, "$.token");
        System.out.println("the token is: " + token);
    }


    @Test
    public void GivenCorrectRecipes_WhenPosted_ThenResponseIsCreated_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very difficult pizza");
        recipe.setType(DishType.REGULAR);
        recipe.setServingNumber(3);
        recipe.setIngredients("Sabzi");

        this.mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
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

        this.mockMvc.perform(get("/api/recipe/1")
                        .header("Authorization", "Bearer " + token))
                        .andDo(print())
                .andExpect(status().isOk());
        }

    @Test
    public void GivenWrongRecipeId_WhenGet_ThenResponseIsNotFound_Test() throws Exception {
        this.mockMvc.perform(get("/api/recipe/6666")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void AllRecipes_WhenGet_ThenResponseRecipes_Test() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(56);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very difficult Pizza");
        recipe.setIngredients("Cheese 2gr, Bread 1kg");
        recipe.setType(DishType.REGULAR);
        mockingService.createRecipe(recipe);

        Recipe recipe2 = new Recipe();
        recipe2.setId(92);
        recipe2.setName("Rashti Pizza");
        recipe2.setInstructions("Very easy pizza in the Oven");
        recipe2.setIngredients("Cheese 2gr, Bread 1kg");
        recipe2.setType(DishType.VEGETARIAN);
        mockingService.createRecipe(recipe2);

        this.mockMvc.perform(get("/api/recipes")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("\"id\":92")));
    }


    @Test
    public void GivenExistedRecipe_WhenPut_ThenResponseIsChanged_Test() throws Exception {
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
                        .header("Authorization", "Bearer " + token)
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
    public void GivenNotExistedRecipe_WhenPut_ThenResponseNotFoundError_Test() throws Exception {
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
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipe)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void GivenCorrectRecipeId_WhenDelete_ThenResponseIsOK_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(7);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very easy pizza");
        recipe.setType(DishType.VEGETARIAN);
        mockingService.createRecipe(recipe);

        this.mockMvc.perform(delete("/api/recipe/7")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void GivenInvalidRecipeId_WhenDelete_ThenResponseIsNotFound_Test() throws Exception {
        this.mockMvc.perform(delete("/api/recipe/-23")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void GivenNonVegetarianSearchQuery_WhenGet_ResponseIsRegularRecipes() throws Exception {
        mockData();

        String searchQuery = "type != VEGETARIAN;";

        this.mockMvc.perform(get("/api/search?searchQuery=" + searchQuery)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(not(org.hamcrest.Matchers.containsString("\"type\":\"VEGETARIAN\""))));

    }


    @Test
    public void GivenValidTypeAndInstructionsSearchQuery_WhenGet_ResponseIsCorrectRecipes() throws Exception {
        mockData();

        String searchQuery = "type != VEGETARIAN;" +
                "instructions ~= oil";

        this.mockMvc.perform(get("/api/search?searchQuery=" + searchQuery)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("\"instructions\":\"You need a lot of oil.\"")))
                .andExpect(content()
                .string(org.hamcrest.Matchers.containsString("\"name\":\"Burger\"")));

    }

    @Test
    public void GivenNotMathcingSearchQuery_WhenGet_ResponseIsEmpty() throws Exception {
        mockData();
        String searchQuery = "type == VEGETARIAN;" +
                "instructions ~= shalgham";

        this.mockMvc.perform(get("/api/search?searchQuery=" + searchQuery)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }


    @Test
    public void GivenBlankUserName_WhenPost_BadRequestRespons_Test() throws JsonProcessingException, UnsupportedEncodingException, Exception{
        MvcResult result= this.mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().
                                writeValueAsString(AuthenticationRequest.builder()
                                        .username("")
                                        .password("@#:OJFL:OI:#J@#@#:IJ#@#OJ#").build())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("username must Not Be Blank")))
                .andReturn();
    }

    @Test
    public void GivenBlankPassword_WhenPost_BadRequestResponse_Test() throws JsonProcessingException, UnsupportedEncodingException, Exception{
        MvcResult result= this.mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().
                                writeValueAsString(AuthenticationRequest.builder()
                                        .username("test")
                                        .password("").build())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("password must Not Be Blank")))
                .andReturn();
    }

    @Test
    public void GivenViolatedMaxServingNumber_WhenPost_BadRequestResponse_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(43);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very difficult pizza");
        recipe.setType(DishType.REGULAR);
        recipe.setServingNumber(102);
        recipe.setIngredients("Havij");

        this.mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(new ObjectMapper().writeValueAsString(recipe)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("servingNumber size must be less than or equal to 100")))
                .andReturn();
    }

    @Test
    public void GivenValidServingNumberLength_WhenPost_BadRequestResponse_Test() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(43);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("V");
        recipe.setIngredients("Sabzi");

        recipe.setType(DishType.REGULAR);
        recipe.setServingNumber(87);

        this.mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(new ObjectMapper().writeValueAsString(recipe)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("instructions size must be greater than or equal to 5 ")))
                .andReturn();
    }



    public void mockData(){
        Recipe recipe = new Recipe();
        recipe.setId(56);
        recipe.setName("Italian Pizza");
        recipe.setInstructions("Very difficult Pizza");
        recipe.setIngredients("Cheese 2gr, Bread 1kg");
        recipe.setType(DishType.REGULAR);
        mockingService.createRecipe(recipe);

        Recipe recipe2 = new Recipe();
        recipe2.setId(92);
        recipe2.setName("Rashti Pizza");
        recipe2.setInstructions("Very easy pizza in the Oven");
        recipe2.setIngredients("Cheese 2gr, Bread 1kg");
        recipe2.setType(DishType.VEGETARIAN);
        mockingService.createRecipe(recipe2);

        Recipe recipe3 = new Recipe();
        recipe3.setId(92);
        recipe3.setName("Burger");
        recipe3.setInstructions("You need a lot of oil.");
        recipe3.setIngredients("Beef Meet 2kg, Bread 2 pieces");
        recipe3.setType(DishType.REGULAR);
        mockingService.createRecipe(recipe3);

    }

}
