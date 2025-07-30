package com.canbe.exception;

public class CategoryPermissionDeniedException extends RuntimeException {
    public CategoryPermissionDeniedException() {
        super("You don't have permission to delete this category.");
    }
}
