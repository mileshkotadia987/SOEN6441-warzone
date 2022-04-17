package com.warzone.team08.VM.exceptions;

/**
 * This Exception shows that the provided game file to be load into the engines is invalid and has corrupted data.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class InvalidGameException extends VMException {
    /**
     * Default constructor.
     */
    public InvalidGameException() {
        super("Invalid game file!");
    }

    /**
     * Parameterised constructor to pass the message to super class.
     *
     * @param p_message Message to be passed to super class.
     */
    public InvalidGameException(String p_message) {
        super(p_message);
    }

    /**
     * Parameterised constructor to pass create <code>InvalidGameException</code> using <code>VMException</code>.
     *
     * @param p_vmException VMException.
     */
    public InvalidGameException(VMException p_vmException) {
        super(p_vmException.getMessage());
    }

    /**
     * Parameterised constructor to pass the message and throwable cause of the exception to super class.
     *
     * @param message Message to be passed to super class.
     * @param cause   Cause of the exception to be passed to super class.
     */
    public InvalidGameException(String message, Throwable cause) {
        super(message, cause);
    }
}