package com.abn.recipe.dto;

import com.abn.recipe.entity.DishType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class Recipe {
	@NotNull(message = "id must not be Null")
	private Integer id;

	@NotNull(message = "name must not be Null")
	@NotBlank(message = "name must not be Blank")
	private String name;

	@NotNull(message = "instructions must not be Null")
	@NotBlank(message = "instructions must not be Blank")
	@Size(min=5,message = "instructions size must be greater than or equal to 5 ")
    private String instructions;

	@NotNull(message = "type must not be Null")
	private DishType type;

	@NotNull(message = "servingNumber must not be Null")
	@Max(value = 100 ,message = "servingNumber size must be less than or equal to 100 " )
	private Integer servingNumber;

	@NotNull(message ="ingredients must not be Null")
	@NotBlank(message = "ingredients must not be Blank")
    private String ingredients;

}
