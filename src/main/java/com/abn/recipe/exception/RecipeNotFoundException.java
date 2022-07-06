package com.abn.recipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecipeNotFoundException  extends ResponseStatusException {


    public RecipeNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public RecipeNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
