package com.maze.recipe.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RecipeIngredient {
    @EqualsAndHashCode.Exclude
    private Long recipeId;

    @EqualsAndHashCode.Exclude
    private Integer orderIndex;

    @Positive(message = "Ingredient quantity must be positive")
    private Integer quantityNumerator;

    @Positive(message = "Ingredient quantity denominator must be positive")
    private Integer quantityDenominator;

    @NotBlank(message = "Measurement unit cannot be empty")
    private String unit;

    @NotBlank(message = "Ingredient name cannot be empty")
    private String ingredient;
}
