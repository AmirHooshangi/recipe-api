package com.abn.recipe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "Recipes")
public class RecipeEntity {

    @Id
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
	private DishType type;

	@Column(name = "SERVING_NUMBER")
	private Integer servingNumber;
	
	@Column(name = "INGREDIENTS", nullable=true)
	private String ingredients;
	
	@Column(name = "INSTRUCTIONS", nullable=true)
	private String instructions;
}
