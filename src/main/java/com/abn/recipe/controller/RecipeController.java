package com.abn.recipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public class RecipeController {

    @PostMapping("/recipe")
    public String createRecipe() {

        return "HI";
    }
}
