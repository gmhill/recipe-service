package com.maze.recipe.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RecipeInstruction {
    @EqualsAndHashCode.Exclude
    private Long recipeId;

    @EqualsAndHashCode.Exclude
    private Integer stepOrder;

    private String instructionText;
}
