package com.abn.recipe.dto;

import com.abn.recipe.entity.DishType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Recipe {
	private Integer id;
	private String name;
    private String instructions;
	private DishType type;
	private Integer servingNumber;
    private String ingredients;
}
