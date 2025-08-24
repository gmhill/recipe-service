package com.maze.recipe.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Recipe {
    @EqualsAndHashCode.Exclude
    private Long id;

    private String recipeName;

    private List<RecipeIngredient> recipeIngredients;

    private List<RecipeInstruction> recipeInstructions;
}
