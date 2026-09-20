package com.maze.recipe.service;

import com.maze.recipe.RecipeApplication;
import com.maze.recipe.dto.request.CreateIngredientDto;
import com.maze.recipe.dto.request.CreateInstructionDto;
import com.maze.recipe.dto.request.CreateRecipeDto;
import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.exception.InvalidIdException;
import com.maze.recipe.exception.RecipeNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RecipeApplication.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class RecipeServiceTest {
    @Autowired
    private RecipeService service;

    @Test
    void createdRecipeReadsBackWithId() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDto();

        // When
        long id = service.createRecipe(recipeDto);

        // Then
        RecipeResponseDto actualRecipe = service.readRecipe(id);
        assertThat(actualRecipe.id()).isPositive();
    }

    @Test
    void readRecipeThrowsNotFoundForUnseededId() {
        // Given
        service.createRecipe(getCreateRecipeDto());
        service.createRecipe(getCreateRecipeDto());

        // When/Then
        assertThatThrownBy(() -> service.readRecipe(Long.MAX_VALUE)).isInstanceOf(RecipeNotFoundException.class);
    }

    @Test
    void updatedRecipeReadsBackWithChanges() {
        // Given
        long id = service.createRecipe(getCreateRecipeDto());
        CreateRecipeDto updateDto = getCreateRecipeDtoWithName("Updated Recipe Name");

        // When
        RecipeResponseDto updatedRecipe = service.updateRecipe(id, updateDto);

        // Then
        assertThat(updatedRecipe.id()).isEqualTo(id);
        assertThat(updatedRecipe.recipeName()).isEqualTo("Updated Recipe Name");
        assertThat(service.readRecipe(id).recipeName()).isEqualTo("Updated Recipe Name");
    }

    @Test
    void updateRecipeThrowsNotFoundForUnseededId() {
        // When/Then
        assertThatThrownBy(() -> service.updateRecipe(Long.MAX_VALUE, getCreateRecipeDto()))
                .isInstanceOf(RecipeNotFoundException.class);
    }

    @Test
    void updateRecipeThrowsInvalidIdForNonPositiveId() {
        // When/Then
        assertThatThrownBy(() -> service.updateRecipe(0L, getCreateRecipeDto()))
                .isInstanceOf(InvalidIdException.class);
        assertThatThrownBy(() -> service.updateRecipe(-1L, getCreateRecipeDto()))
                .isInstanceOf(InvalidIdException.class);
    }

    @Test
    void updateRecipeWithBlankNameIsInvalid() {
        // Given
        long id = service.createRecipe(getCreateRecipeDto());
        CreateRecipeDto updateDto = getCreateRecipeDtoWithName("");

        // When/Then
        assertThatThrownBy(() -> service.updateRecipe(id, updateDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void recipesWithBlankNameAreInvalid() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithName("");

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void recipesWithNullNameAreInvalid() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithName(null);

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void recipesWithoutIngredientsAreInvalid() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(List.of());

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void recipesWithNullIngredientsAreInvalid() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(null);

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void recipesWithoutInstructionsAreInvalid() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithInstructions(List.of());

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void recipesWithNullInstructionsAreInvalid() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithInstructions(null);

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void ingredientNumeratorCantBeZero() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("0/3", "cups", "sugar"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void ingredientNumeratorCantBeNegative() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("-2/3", "cups", "sugar"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void ingredientsDenominatorCantBeZero() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("3/0", "cups", "sugar"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void ingredientsDenominatorCantBeNegative() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("3/-1", "cups", "sugar"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void ingredientQuantityCantBeNull() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto(null, "cups", "sugar"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void ingredientDecimalQuantityGetsConvertedToFraction() {
        // Given
        String expectedQuantityDecimal = "2.5";
        String expectedQuantityFraction = "5 / 2";
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto(expectedQuantityDecimal, "cups", "sugar"))
        );

        // When
        long id = service.createRecipe(recipeDto);

        // Then
        RecipeResponseDto actualRecipe = service.readRecipe(id);
        assertThat(actualRecipe.ingredients().getFirst().quantity()).isEqualTo(expectedQuantityFraction);
    }

    @Test
    void unitCantBeNull() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("1", null, "sugar"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void unitCantBeEmptyString() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("1", "", "sugar"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void unitCantBeWhitespace() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("1", "\t", "sugar"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void ingredientListCantBeNull() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(null);

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void ingredientCantBeNull() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("1", "cup", null))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void ingredientCantBeEmptyString() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("1", "cup", ""))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void ingredientCantBeWhitespace() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithIngredients(
                List.of(new CreateIngredientDto("1", "cup", "\s"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void instructionListCantBeNull() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithInstructions(null);

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void instructionCantBeNull() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithInstructions(
                List.of(new CreateInstructionDto(null))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void instructionCantBeEmptyString() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithInstructions(
                List.of(new CreateInstructionDto(""))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void instructionCantBeWhitespace() {
        // Given
        CreateRecipeDto recipeDto = getCreateRecipeDtoWithInstructions(
                List.of(new CreateInstructionDto("\n"))
        );

        // When/Then
        assertThatThrownBy(() -> service.createRecipe(recipeDto)).isInstanceOf(ConstraintViolationException.class);
    }

    private CreateRecipeDto getCreateRecipeDto() {
        CreateIngredientDto ingredientDto = new CreateIngredientDto("1", "cup", "flour");
        CreateInstructionDto instructionDto = new CreateInstructionDto("Sift it.");
        return new CreateRecipeDto("Sifted Flour Recipe", List.of(ingredientDto), List.of(instructionDto));
    }

    private CreateRecipeDto getCreateRecipeDtoWithName(String name) {
        CreateIngredientDto ingredientDto = new CreateIngredientDto("1", "cup", "flour");
        CreateInstructionDto instructionDto = new CreateInstructionDto("Sift it.");
        return new CreateRecipeDto(name, List.of(ingredientDto), List.of(instructionDto));
    }

    private CreateRecipeDto getCreateRecipeDtoWithIngredients(List<CreateIngredientDto> ingredients) {
        CreateInstructionDto instructionDto = new CreateInstructionDto("Sift it.");
        return new CreateRecipeDto("Sifted Flour Recipe", ingredients, List.of(instructionDto));
    }

    private CreateRecipeDto getCreateRecipeDtoWithInstructions(List<CreateInstructionDto> instructions) {
        CreateIngredientDto ingredientDto = new CreateIngredientDto("1", "cup", "flour");
        return new CreateRecipeDto("Sifted Flour Recipe", List.of(ingredientDto), instructions);
    }
}