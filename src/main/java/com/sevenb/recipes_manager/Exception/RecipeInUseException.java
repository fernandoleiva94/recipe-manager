package com.sevenb.recipes_manager.Exception;

public class RecipeInUseException extends RuntimeException {
    public RecipeInUseException(String message) {
        super(message);
    }
}

