package com.warzone.team08.VM.exceptions;

/**
 * This Exception shows that the player has entered invalid order; maybe due to the command not having the required argument or type mismatching.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class InvalidOrderException extends VMException {
    /**
     * Parameterised constructor to pass the message to super class.
     *
     * @param message Message to be passed to super class.
     */
    public InvalidOrderException(String message) {
        super(message);
    }


    /**
     * Parameterised constructor to pass the message and throwable cause of the exception to super class.
     *
     * @param message Message to be passed to super class.
     * @param cause   Cause of the exception to be passed to super class.
     */
    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}