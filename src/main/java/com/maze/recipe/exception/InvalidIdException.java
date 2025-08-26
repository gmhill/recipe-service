package com.maze.recipe.exception;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {}
    public InvalidIdException(String message) {
        super(message);
    }
}
