package com.warzone.team08.VM.exceptions;

/**
 * Exception to show required tag is absent in .map file
 *
 * @author CHARIT
 * @author Brijesh Lakkad
 */
public class AbsentTagException extends VMException {
    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string
     */
    public AbsentTagException(String p_message) {
        super(p_message);
    }

    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string.
     * @param p_cause Cause is the exception that causes the current exception.
     */
    public AbsentTagException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }
}
