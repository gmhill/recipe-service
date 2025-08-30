package com.maze.recipe.mapper;

import com.maze.recipe.dto.request.CreateIngredientDto;
import com.maze.recipe.dto.request.CreateInstructionDto;
import com.maze.recipe.dto.request.CreateRecipeDto;
import com.maze.recipe.dto.response.IngredientResponseDto;
import com.maze.recipe.dto.response.InstructionResponseDto;
import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.entity.Recipe;
import com.maze.recipe.entity.RecipeIngredient;
import com.maze.recipe.entity.RecipeInstruction;
import org.apache.commons.math3.fraction.Fraction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);
    
    @Mapping(target = "ingredients", source = "recipeIngredients")
    @Mapping(target = "instructions", source = "recipeInstructions")
    RecipeResponseDto toResponseDto(Recipe recipe);
    
    @Mapping(target = "quantity", expression = "java(formatQuantity(ingredient.getQuantityNumerator(), ingredient.getQuantityDenominator()))")
    IngredientResponseDto toIngredientResponseDto(RecipeIngredient ingredient);
    
    @Mapping(target = "stepText", source = "instructionText")
    InstructionResponseDto toInstructionResponseDto(RecipeInstruction instruction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recipeIngredients", source = "ingredients")
    @Mapping(target = "recipeInstructions", source ="instructions")
    Recipe toRecipe(CreateRecipeDto dto);
    
    @Mapping(target = "recipeId", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "quantityNumerator", expression = "java(parseQuantity(dto.quantity()).getNumerator())")
    @Mapping(target = "quantityDenominator", expression = "java(parseQuantity(dto.quantity()).getDenominator())")
    RecipeIngredient toRecipeIngredient(CreateIngredientDto dto);
    
    @Mapping(target = "recipeId", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "instructionText", source = "stepText")
    RecipeInstruction toRecipeInstruction(CreateInstructionDto dto);
    
    default String formatQuantity(Integer numerator, Integer denominator) {
        if (numerator == null || denominator == null) {
            return "0";
        }
        Fraction fraction = new Fraction(numerator, denominator);
        return fraction.toString();
    }
    
    default Fraction parseQuantity(String quantityStr) {
        try {
            return new Fraction(Double.parseDouble(quantityStr));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid quantity format: " + quantityStr, e);
        }
    }
}