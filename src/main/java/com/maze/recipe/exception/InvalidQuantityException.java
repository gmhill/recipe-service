package com.maze.recipe.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
        super();
    }

    public InvalidQuantityException(String message) {
        super(message);
    }
}
