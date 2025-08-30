package com.maze.recipe.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {

    @Test
    void testRecipesWithSameContentsButDifferentIdsAreEqual() {
        Recipe recipe1 = new Recipe(1L, "Pasta", List.of(new RecipeIngredient()), List.of(new RecipeInstruction()));
        Recipe recipe2 = new Recipe(2L, "Pasta", List.of(new RecipeIngredient()), List.of(new RecipeInstruction()));

        assertThat(recipe1).isEqualTo(recipe2);
    }

    @Test
    void recipesWithDifferentContentButSameIdsAreNotEqual() {
        Recipe recipe1 = new Recipe(1L, "Pasta", List.of(new RecipeIngredient()), List.of(new RecipeInstruction()));
        Recipe recipe2 = new Recipe(1L, "Burger", List.of(new RecipeIngredient()), List.of(new RecipeInstruction()));

        assertThat(recipe1).isNotEqualTo(recipe2);
    }

    @Test
    void recipesWithDifferentContentAndDifferentIdsAreNotEqual() {
        Recipe recipe1 = new Recipe(1L, "Pasta", List.of(new RecipeIngredient()), List.of(new RecipeInstruction()));
        Recipe recipe2 = new Recipe(2L, "Burger", List.of(new RecipeIngredient()), List.of(new RecipeInstruction()));

        assertThat(recipe1).isNotEqualTo(recipe2);
    }

}