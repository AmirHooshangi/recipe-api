package com.abn.recipe.exception;

import com.abn.recipe.dto.HttpError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleRecipeNotFoundException(RecipeNotFoundException ex){
        log.info("No Recipe found exception: ");
        return errorBuilder(ErrorDescription.RECIPE_NOT_FOUND_MSG, HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<Object> errorBuilder(String message, HttpStatus status){
        HttpError errorResponse = new HttpError(message, LocalDateTime.now());
        return ResponseEntity.status(status).body(errorResponse);
    }
}