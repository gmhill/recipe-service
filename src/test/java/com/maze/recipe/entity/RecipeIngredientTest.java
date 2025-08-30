package com.maze.recipe.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeIngredientTest {

    @Test
    void testCase1_sameRecipeId_sameOrderIndex_sameContents_shouldBeEqual() {
        RecipeIngredient ingredient1 = new RecipeIngredient(1L, 1, 1, 2, "teaspoon", "salt");
        RecipeIngredient ingredient2 = new RecipeIngredient(1L, 1, 1, 2, "teaspoon", "salt");

        assertThat(ingredient1).isEqualTo(ingredient2);
    }

    @Test
    void testCase2_sameRecipeId_sameOrderIndex_differentContents_shouldNotBeEqual() {
        RecipeIngredient ingredient1 = new RecipeIngredient(1L, 1, 1, 2, "teaspoon", "salt");
        RecipeIngredient ingredient2 = new RecipeIngredient(1L, 1, 2, 3, "tablespoon", "pepper");

        assertThat(ingredient1).isNotEqualTo(ingredient2);
    }

    @Test
    void testCase3_sameRecipeId_differentOrderIndex_sameContents_shouldBeEqual() {
        RecipeIngredient ingredient1 = new RecipeIngredient(1L, 1, 1, 2, "teaspoon", "salt");
        RecipeIngredient ingredient2 = new RecipeIngredient(1L, 2, 1, 2, "teaspoon", "salt");

        assertThat(ingredient1).isEqualTo(ingredient2);
    }

    @Test
    void testCase4_sameRecipeId_differentOrderIndex_differentContents_shouldNotBeEqual() {
        RecipeIngredient ingredient1 = new RecipeIngredient(1L, 1, 1, 2, "teaspoon", "salt");
        RecipeIngredient ingredient2 = new RecipeIngredient(1L, 2, 2, 3, "tablespoon", "pepper");

        assertThat(ingredient1).isNotEqualTo(ingredient2);
    }

    @Test
    void testCase5_differentRecipeId_sameOrderIndex_sameContents_shouldBeEqual() {
        RecipeIngredient ingredient1 = new RecipeIngredient(1L, 1, 1, 2, "teaspoon", "salt");
        RecipeIngredient ingredient2 = new RecipeIngredient(2L, 1, 1, 2, "teaspoon", "salt");

        assertThat(ingredient1).isEqualTo(ingredient2);
    }

    @Test
    void testCase6_differentRecipeId_differentOrderIndex_sameContents_shouldBeEqual() {
        RecipeIngredient ingredient1 = new RecipeIngredient(1L, 1, 1, 2, "teaspoon", "salt");
        RecipeIngredient ingredient2 = new RecipeIngredient(2L, 2, 1, 2, "teaspoon", "salt");

        assertThat(ingredient1).isEqualTo(ingredient2);
    }
}