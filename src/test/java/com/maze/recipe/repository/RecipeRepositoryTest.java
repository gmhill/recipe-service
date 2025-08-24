package com.maze.recipe.repository;

import com.maze.recipe.models.Recipe;
import com.maze.recipe.models.RecipeIngredient;
import com.maze.recipe.models.RecipeInstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class RecipeRepositoryTest {
    RecipeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRecipeRepository();
    }

    @Test
    void savedRecipeCanBeRead() {
        // Given
        assertThat(repository.findAll()).hasSize(0);
        Recipe expectedRecipe = getRecipe();

        // When
        long id = repository.create(expectedRecipe);

        // Then
        Optional<Recipe> actualRecipe = repository.findById(id);

        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.existsById(id)).isTrue();
        assertThat(actualRecipe).isNotEmpty();
        assertThat(actualRecipe.get()).isEqualTo(expectedRecipe);
    }

    @Test
    void multipleSavedRecipesCanBeRead() {
        // Given
        assertThat(repository.findAll()).hasSize(0);

        // When
        repository.create(getRecipe());
        repository.create(getSecondRecipe());

        // Then
        Collection recipes = repository.findAll();
        assertThat(recipes).hasSize(2);
    }

    @Test
    void multipleSavedRecipesGetUniqueIds() {
        // When
        long id1 = repository.create(getRecipe());
        long id2 = repository.create(getSecondRecipe());

        // Then
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    void nonNullIdThrowsException() {
        // Given
        Recipe recipe = getRecipe();
        recipe.setId(99L);

        // When/Then
        assertThatThrownBy(() -> repository.create(recipe)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void duplicateIdThrowsException() {
        // Given
        Recipe recipe = getRecipe();
        repository.create(recipe);

        // When/Then
        assertThatThrownBy(() -> repository.create(recipe)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void recipeCanBeDeleted() {
        // Given
        Long id = repository.create(getRecipe());
        repository.create(getSecondRecipe());
        assertThat(repository.findAll()).hasSize(2);

        // When
        repository.deleteById(id);

        //Then
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void existingRecipeCanBeFound() {
        Long id = repository.create(getRecipe());
        assertThat(repository.existsById(id)).isTrue();
    }

    @Test
    void nonExistentRecipeCantBeFound() {
        assertThat(repository.existsById(1L)).isFalse();
        assertThat(repository.existsById(0L)).isFalse();
        assertThat(repository.existsById(-1L)).isFalse();
    }

    @Test
    void nonExistentRecipeCantBeDeleted() {
        assertThat(repository.deleteById(1L)).isEmpty();
        assertThat(repository.deleteById(0L)).isEmpty();
        assertThat(repository.deleteById(-1L)).isEmpty();
    }

    @Test
    void idGetsAssignedUponSavingToRepository() {
        // Given
        Recipe recipe = getRecipe();
        assertThat(recipe.getId() == null);

        // When
        Long id = repository.create(recipe);

        // Then
        Recipe actualRecipe = repository.findById(id).get();
        assertThat(actualRecipe.getId()).isNotNull();
    }

    @Test
    void idConsistentAmongRecipeAndSubObjects() {
        // Given
        Recipe recipe = getRecipe();

        // When
        Long id = repository.create(recipe);

        // Then
        Long expectedId = recipe.getId();
        assertThat(recipe.getRecipeIngredients().stream()
                .allMatch(ingredient -> expectedId.equals(ingredient.getRecipeId())));
        assertThat(recipe.getRecipeInstructions().stream()
                .allMatch(instruction -> expectedId.equals(instruction.getRecipeId())));
    }

    private Recipe getRecipe() {
        Long id = null;
        String recipeName = "A test recipe";
        RecipeIngredient ingredient = new RecipeIngredient(id, 0, 3, 1, "ounces", "lemons");
        RecipeInstruction instruction = new RecipeInstruction(id, 0, "Blend it");

        return new Recipe(id, recipeName, List.of(ingredient), List.of(instruction));
    }

    private Recipe getSecondRecipe() {
        Long id = null;
        String recipeName = "This is a different recipe";
        RecipeIngredient ingredient = new RecipeIngredient(id, 0, 1, 2, "tsp", "limes");
        RecipeInstruction instruction = new RecipeInstruction(id, 0, "Boil it");

        return new Recipe(id, recipeName, List.of(ingredient), List.of(instruction));
    }
}