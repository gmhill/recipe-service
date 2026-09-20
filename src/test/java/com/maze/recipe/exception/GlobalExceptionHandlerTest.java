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
