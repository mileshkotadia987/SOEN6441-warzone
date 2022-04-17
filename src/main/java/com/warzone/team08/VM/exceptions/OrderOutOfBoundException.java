package com.warzone.team08.VM.exceptions;

/**
 * Exception to indicate that the player doesn't have any remaining order for execution.
 *
 * @author Brijesh Lakkad
 */
public class OrderOutOfBoundException extends VMException {
    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string
     */
    public OrderOutOfBoundException(String p_message) {
        super(p_message);
    }

    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string.
     * @param p_cause Cause is the exception that causes the current exception.
     */
    public OrderOutOfBoundException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }
}
