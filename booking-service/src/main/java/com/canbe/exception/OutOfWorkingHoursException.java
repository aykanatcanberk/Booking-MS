package com.canbe.exception;

public class OutOfWorkingHoursException extends RuntimeException {
    public OutOfWorkingHoursException(String message) {
        super(message);
    }
}
