package com.warzone.team08.VM.exceptions;

/**
 * Exception is thrown when the game engine tries to move to a state which is not expected.
 *
 * @author Brijesh Lakkad
 */
public class GameLoopIllegalStateException extends VMException {
    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string
     */
    public GameLoopIllegalStateException(String p_message) {
        super(p_message);
    }

    /**
     * Parameterized constructor to call parent class constructor.
     *
     * @param p_message Error message string.
     * @param p_cause Cause is the exception that causes the current exception.
     */
    public GameLoopIllegalStateException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }
}
