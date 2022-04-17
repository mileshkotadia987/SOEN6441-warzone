package com.warzone.team08.VM.exceptions;

/**
 * This Exception shows that the user input was invalid
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class InvalidCommandException extends VMException {

    /**
     * Parameterised constructor to pass the message to super class.
     *
     * @param message Message to be passed to super class.
     */
    public InvalidCommandException(String message) {
        super(message);
    }

    /**
     * Parameterised constructor to pass the message and throwable cause of the exception to super class.
     *
     * @param message Message to be passed to super class.
     * @param cause   Cause of the exception to be passed to super class.
     */
    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}