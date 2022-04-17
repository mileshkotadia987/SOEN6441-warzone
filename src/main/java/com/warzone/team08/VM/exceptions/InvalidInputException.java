package com.warzone.team08.VM.exceptions;

/**
 * Exception to show user that provided argument and its value(s) are not valid
 *
 * @author CHARIT
 * @author Brijesh Lakkad
 */
public class InvalidInputException extends VMException {
    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string
     */
    public InvalidInputException(String p_message) {
        super(p_message);
    }

    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string.
     * @param p_cause Cause is the exception that causes the current exception.
     */
    public InvalidInputException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }
}