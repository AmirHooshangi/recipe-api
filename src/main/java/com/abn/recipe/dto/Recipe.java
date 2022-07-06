package com.abn.recipe.dto;

import com.abn.recipe.entity.DishType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Recipe {
	private Integer id;
	private String name;
    private String instructions;
    //TODO: To enum
	private DishType type;
	private Integer servingNumber;
    private List<Ingredient> ingredientsList = new ArrayList<>();
}
