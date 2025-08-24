package com.maze.recipe.models;

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

    private Integer quantityNumerator;

    private Integer quantityDenominator;

    private String unit;

    private String ingredient;
}
