package com.maze.recipe.repository;

import com.maze.recipe.entity.Recipe;
import com.maze.recipe.repository.jpa.RecipeEntity;
import com.maze.recipe.repository.jpa.RecipeEntityMapper;
import com.maze.recipe.repository.jpa.RecipeJpaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
@Profile("!test")
public class PostgresRecipeRepository implements RecipeRepository {
    private final RecipeJpaRepository jpaRepository;
    private final RecipeEntityMapper mapper;

    public PostgresRecipeRepository(RecipeJpaRepository jpaRepository, RecipeEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Recipe> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recipe> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Recipe> deleteById(Long id) {
        return jpaRepository.findById(id).map(entity -> {
            Recipe deleted = mapper.toDomain(entity);
            jpaRepository.delete(entity);
            return deleted;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    @Transactional
    public long create(Recipe recipe) {
        if (recipe.getId() != null) {
            throw new RuntimeException("Recipe object must be passed in with no key");
        }

        RecipeEntity entity = mapper.toNewEntity(recipe);
        RecipeEntity saved = jpaRepository.save(entity);
        Long id = saved.getId();

        // Cascade
        recipe.setId(id);
        recipe.getRecipeIngredients()
                .forEach(ingredient -> ingredient.setRecipeId(id));
        recipe.getRecipeInstructions()
                .forEach(instruction -> instruction.setRecipeId(id));

        return id;
    }

    @Override
    @Transactional
    public Optional<Recipe> update(Recipe recipe) {
        Long id = recipe.getId();
        if (id == null) {
            return Optional.empty();
        }

        return jpaRepository.findById(id).map(entity -> {
            mapper.copyOnto(recipe, entity);
            RecipeEntity saved = jpaRepository.save(entity);
            return mapper.toDomain(saved);
        });
    }
}
