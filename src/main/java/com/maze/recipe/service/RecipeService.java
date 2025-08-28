package com.maze.recipe.service;

import com.maze.recipe.dto.request.CreateRecipeDto;
import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.exception.InvalidIdException;
import com.maze.recipe.exception.RecipeNotFoundException;
import com.maze.recipe.mapper.RecipeMapper;
import com.maze.recipe.models.Recipe;
import com.maze.recipe.repository.RecipeRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final Validator validator;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, Validator validator) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.validator = validator;
    }

    @Transactional
    public long createRecipe(CreateRecipeDto createRecipeDto) {
        Recipe recipe = recipeMapper.toRecipe(createRecipeDto);
        
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return recipeRepository.create(recipe);
    }

    @Transactional(readOnly = true)
    public List<RecipeResponseDto> readAllRecipes() {
        Collection<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(recipeMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RecipeResponseDto readRecipe(long id) {
        validateId(id);

        return recipeRepository.findById(id)
                .map(recipeMapper::toResponseDto)
                .orElseThrow(() -> new RecipeNotFoundException("Unable to find recipe with id " + id)
                );
    }

    @Transactional
    public RecipeResponseDto deleteRecipe(long id) {
        validateId(id);

        return recipeRepository.deleteById(id)
                .map(recipeMapper::toResponseDto)
                .orElseThrow(() -> new RecipeNotFoundException("Unable to find recipe with id " + id)
        );
    }

    private void validateId(long id) {
        if (id <= 0L) {
            throw new InvalidIdException("Provided ID must be greater than 0");
        }
    }

}
