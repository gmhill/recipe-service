package com.maze.recipe.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotBlank(message = "Recipe name cannot be empty")
    private String recipeName;

    @NotEmpty(message = "Recipe must have at least one ingredient")
    @Valid
    private List<RecipeIngredient> recipeIngredients;

    @NotEmpty(message = "Recipe must have at least one instruction")
    @Valid
    private List<RecipeInstruction> recipeInstructions;
}
