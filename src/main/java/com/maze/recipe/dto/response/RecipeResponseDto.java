package com.maze.recipe.dto.response;

import java.util.List;

public record RecipeResponseDto(
        long id,
        String recipeName,
        List<IngredientResponseDto> ingredients,
        List<InstructionResponseDto> instructions
){}
