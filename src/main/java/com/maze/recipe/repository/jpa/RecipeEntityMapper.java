package com.maze.recipe.repository.jpa;

import com.maze.recipe.entity.Recipe;
import com.maze.recipe.entity.RecipeIngredient;
import com.maze.recipe.entity.RecipeInstruction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class RecipeEntityMapper {

    public RecipeEntity toNewEntity(Recipe recipe) {
        RecipeEntity entity = new RecipeEntity();
        entity.setRecipeName(recipe.getRecipeName());
        entity.getIngredients().addAll(toIngredientEntities(recipe.getRecipeIngredients(), entity));
        entity.getInstructions().addAll(toInstructionEntities(recipe.getRecipeInstructions(), entity));
        return entity;
    }

    public void copyOnto(Recipe recipe, RecipeEntity entity) {
        entity.setRecipeName(recipe.getRecipeName());

        entity.getIngredients().clear();
        entity.getIngredients().addAll(toIngredientEntities(recipe.getRecipeIngredients(), entity));

        entity.getInstructions().clear();
        entity.getInstructions().addAll(toInstructionEntities(recipe.getRecipeInstructions(), entity));
    }

    public Recipe toDomain(RecipeEntity entity) {
        List<RecipeIngredient> ingredients = entity.getIngredients().stream()
                .sorted(Comparator.comparing(RecipeIngredientEntity::getOrderIndex, Comparator.nullsLast(Integer::compareTo)))
                .map(ingredient -> new RecipeIngredient(
                        entity.getId(),
                        ingredient.getOrderIndex(),
                        ingredient.getQuantityNumerator(),
                        ingredient.getQuantityDenominator(),
                        ingredient.getUnit(),
                        ingredient.getIngredient()))
                .toList();

        List<RecipeInstruction> instructions = entity.getInstructions().stream()
                .sorted(Comparator.comparing(RecipeInstructionEntity::getOrderIndex, Comparator.nullsLast(Integer::compareTo)))
                .map(instruction -> new RecipeInstruction(
                        entity.getId(),
                        instruction.getOrderIndex(),
                        instruction.getInstructionText()))
                .toList();

        return new Recipe(entity.getId(), entity.getRecipeName(), ingredients, instructions);
    }

    private List<RecipeIngredientEntity> toIngredientEntities(List<RecipeIngredient> ingredients, RecipeEntity parent) {
        List<RecipeIngredientEntity> entities = new ArrayList<>();
        for (int index = 0; index < ingredients.size(); index++) {
            RecipeIngredient ingredient = ingredients.get(index);
            RecipeIngredientEntity entity = new RecipeIngredientEntity();
            entity.setRecipe(parent);
            entity.setOrderIndex(index);
            entity.setQuantityNumerator(ingredient.getQuantityNumerator());
            entity.setQuantityDenominator(ingredient.getQuantityDenominator());
            entity.setUnit(ingredient.getUnit());
            entity.setIngredient(ingredient.getIngredient());
            entities.add(entity);
        }
        return entities;
    }

    private List<RecipeInstructionEntity> toInstructionEntities(List<RecipeInstruction> instructions, RecipeEntity parent) {
        List<RecipeInstructionEntity> entities = new ArrayList<>();
        for (int index = 0; index < instructions.size(); index++) {
            RecipeInstruction instruction = instructions.get(index);
            RecipeInstructionEntity entity = new RecipeInstructionEntity();
            entity.setRecipe(parent);
            entity.setOrderIndex(index);
            entity.setInstructionText(instruction.getInstructionText());
            entities.add(entity);
        }
        return entities;
    }
}
