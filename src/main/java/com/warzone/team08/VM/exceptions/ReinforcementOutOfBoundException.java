package com.warzone.team08.VM.exceptions;

/**
 * Exception to indicate that the player doesn't have number of reinforcements required enough to issue the order.
 *
 * @author Brijesh Lakkad
 */
public class ReinforcementOutOfBoundException extends VMException {
    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string
     */
    public ReinforcementOutOfBoundException(String p_message) {
        super(p_message);
    }

    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string.
     * @param p_cause Cause is the exception that causes the current exception.
     */
    public ReinforcementOutOfBoundException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }
}
