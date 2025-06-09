package com.sevenb.recipes_manager.Exception;

public class CannotDeleteSupplyException extends RuntimeException {
    public CannotDeleteSupplyException(String message) {
        super(message);
    }
}