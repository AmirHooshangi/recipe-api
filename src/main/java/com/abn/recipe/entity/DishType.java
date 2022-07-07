package com.abn.recipe.entity;

public enum DishType {
    VEGETARIAN("VEGETARIAN"),
    REGULAR("REGULAR");

    public final String label;

    private DishType(String label) {
        this.label = label;
    }
}
