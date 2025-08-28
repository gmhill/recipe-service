package com.maze.recipe.repository;

import com.maze.recipe.models.Recipe;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryRecipeRepository implements RecipeRepository {
    private final AtomicLong idCounter = new AtomicLong(1L);
    private final HashMap<Long, Recipe> inMemoryRepository = new HashMap<>();

    @Override
    public Collection<Recipe> findAll() {
        return inMemoryRepository.values();
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return Optional.ofNullable(inMemoryRepository.get(id));
    }

    @Override
    public Optional<Recipe> deleteById(Long id) {
        return Optional.ofNullable(inMemoryRepository.remove(id));
    }

    @Override
    public boolean existsById(Long id) {
        return inMemoryRepository.containsKey(id);
    }

    @Override
    public long create(Recipe recipe) {
        if(recipe.getId() != null) {
            throw new RuntimeException("Recipe object must be passed in with no key");
        }

        Long id = idCounter.getAndIncrement();
        if(inMemoryRepository.containsKey(id)) {
            throw new RuntimeException("The key chosen for this recipe is already in use");
        }

        // Set ID and cascade
        recipe.setId(id);
        recipe.getRecipeIngredients().stream()
                .forEach(ingredient -> ingredient.setRecipeId(id));
        recipe.getRecipeInstructions().stream()
                .forEach(instruction -> instruction.setRecipeId(id));
        inMemoryRepository.put(id, recipe);

        return id;
    }
}
