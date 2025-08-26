package com.maze.recipe.dto.request;

import java.util.List;

public record CreateRecipeDto (
        String recipeName,
        List<CreateIngredientDto> ingredients,
        List<CreateInstructionDto> instructions
){}
