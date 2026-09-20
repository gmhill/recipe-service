package com.maze.recipe.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlesRecipeNotFoundException() {
        ErrorResponse response = handler.handleRecipeNotFound(new RecipeNotFoundException("Unable to find recipe with id 99"));

        assertThat(response.status()).isEqualTo(404);
        assertThat(response.message()).isEqualTo("Unable to find recipe with id 99");
    }

    @Test
    void handlesInvalidIdException() {
        ErrorResponse response = handler.handleInvalidId(new InvalidIdException("Provided ID must be greater than 0"));

        assertThat(response.status()).isEqualTo(400);
        assertThat(response.message()).isEqualTo("Provided ID must be greater than 0");
    }

    @Test
    void handlesInvalidQuantityException() {
        ErrorResponse response = handler.handleInvalidQuantity(new InvalidQuantityException("Invalid quantity format: abc"));

        assertThat(response.status()).isEqualTo(400);
        assertThat(response.message()).isEqualTo("Invalid quantity format: abc");
    }

    @Test
    void handlesConstraintViolationExceptionByJoiningViolationMessages() {
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        when(violation1.getMessage()).thenReturn("recipeName must not be blank");
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
        when(violation2.getMessage()).thenReturn("ingredients must not be empty");

        ErrorResponse response = handler.handleConstraintViolation(
                new ConstraintViolationException(Set.of(violation1, violation2)));

        assertThat(response.status()).isEqualTo(400);
        assertThat(response.message()).contains("recipeName must not be blank", "ingredients must not be empty");
    }
}
