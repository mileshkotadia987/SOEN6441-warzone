package com.warzone.team08.VM.exceptions;

/**
 * Exception to show that map file is invalid.
 *
 * @author CHARIT
 */
public class InvalidMapException extends VMException {
    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string
     */
    public InvalidMapException(String p_message) {
        super(p_message);
    }

    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string.
     * @param p_cause Cause is the exception that causes the current exception.
     */
    public InvalidMapException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }
}
