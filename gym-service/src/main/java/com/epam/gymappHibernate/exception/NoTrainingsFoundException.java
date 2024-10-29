package com.epam.gymappHibernate.exception;

public class NoTrainingsFoundException extends RuntimeException {
    public NoTrainingsFoundException(String message) {
        super(message);
    }
}
