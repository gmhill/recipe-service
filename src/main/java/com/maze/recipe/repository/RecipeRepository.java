package com.maze.recipe.repository;

import com.maze.recipe.entity.Recipe;

import java.util.Collection;
import java.util.Optional;

public interface RecipeRepository {
    Collection<Recipe> findAll();
    Optional<Recipe> findById(Long id);
    Optional<Recipe> deleteById(Long id);
    boolean existsById(Long id);
    long create(Recipe recipe);
}
