package com.maze.recipe.entity;

import jakarta.validation.constraints.NotBlank;
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
    private Integer orderIndex;

    @NotBlank(message = "Instruction text cannot be empty")
    private String instructionText;
}
