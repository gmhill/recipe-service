package com.maze.recipe.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException() {
    }

    public RecipeNotFoundException(String message) {
        super(message);
    }
}
