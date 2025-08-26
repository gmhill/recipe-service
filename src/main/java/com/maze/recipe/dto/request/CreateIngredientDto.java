package com.maze.recipe.dto.request;

public record CreateIngredientDto (
    String quantity,
    String unit,
    String ingredient
) { }
