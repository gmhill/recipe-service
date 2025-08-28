package com.maze.recipe.dto.response;

public record IngredientResponseDto(
        String quantity,
        String unit,
        String ingredient
) {}
