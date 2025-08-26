package com.maze.recipe.repository;

import com.maze.recipe.models.Recipe;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RecipeRepository {
    Collection<Recipe> findAll();
    Optional<Recipe> findById(Long id);
    Optional<Recipe> deleteById(Long id);
    boolean existsById(Long id);
    long create(Recipe recipe);
}
