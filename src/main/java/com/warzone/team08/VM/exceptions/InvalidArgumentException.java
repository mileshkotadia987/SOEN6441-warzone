package com.warzone.team08.VM.exceptions;

/**
 * Exception to show user that provided argument and its value(s) are not valid
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class InvalidArgumentException extends VMException {

    /**
     * Parameterised constructor to pass the message to super class.
     *
     * @param message Message to be passed to super class.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }


    /**
     * Parameterised constructor to pass the message and throwable cause of the exception to super class.
     *
     * @param message Message to be passed to super class.
     * @param cause   Cause of the exception to be passed to super class.
     */
    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}