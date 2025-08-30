package com.maze.recipe.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeInstructionTest {

    @Test
    void testCase1_sameRecipeId_sameStepOrder_sameInstructionText_shouldBeEqual() {
        RecipeInstruction instruction1 = new RecipeInstruction(1L, 1, "Preheat oven to 350°F");
        RecipeInstruction instruction2 = new RecipeInstruction(1L, 1, "Preheat oven to 350°F");

        assertThat(instruction1).isEqualTo(instruction2);
    }

    @Test
    void testCase2_sameRecipeId_sameStepOrder_differentInstructionText_shouldNotBeEqual() {
        RecipeInstruction instruction1 = new RecipeInstruction(1L, 1, "Preheat oven to 350°F");
        RecipeInstruction instruction2 = new RecipeInstruction(1L, 1, "Mix ingredients together");

        assertThat(instruction1).isNotEqualTo(instruction2);
    }

    @Test
    void testCase3_sameRecipeId_differentStepOrder_sameInstructionText_shouldBeEqual() {
        RecipeInstruction instruction1 = new RecipeInstruction(1L, 1, "Preheat oven to 350°F");
        RecipeInstruction instruction2 = new RecipeInstruction(1L, 2, "Preheat oven to 350°F");

        assertThat(instruction1).isEqualTo(instruction2);
    }

    @Test
    void testCase4_sameRecipeId_differentStepOrder_differentInstructionText_shouldNotBeEqual() {
        RecipeInstruction instruction1 = new RecipeInstruction(1L, 1, "Preheat oven to 350°F");
        RecipeInstruction instruction2 = new RecipeInstruction(1L, 2, "Mix ingredients together");

        assertThat(instruction1).isNotEqualTo(instruction2);
    }

    @Test
    void testCase5_differentRecipeId_sameStepOrder_sameInstructionText_shouldBeEqual() {
        RecipeInstruction instruction1 = new RecipeInstruction(1L, 1, "Preheat oven to 350°F");
        RecipeInstruction instruction2 = new RecipeInstruction(2L, 1, "Preheat oven to 350°F");

        assertThat(instruction1).isEqualTo(instruction2);
    }

    @Test
    void testCase6_differentRecipeId_differentStepOrder_sameInstructionText_shouldBeEqual() {
        RecipeInstruction instruction1 = new RecipeInstruction(1L, 1, "Preheat oven to 350°F");
        RecipeInstruction instruction2 = new RecipeInstruction(2L, 2, "Preheat oven to 350°F");

        assertThat(instruction1).isEqualTo(instruction2);
    }
}