package com.warzone.team08.CLI.constants.states;

/**
 * This class describes the different state of the user interaction: Whether the user is waiting for the execution to
 * complete or the program is waiting for the user input
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public enum UserInteractionState {
    /**
     * If the program is waiting for the user input
     */
    WAIT("wait"),
    /**
     * If the user is waiting for the program to finish the execution of the previous command
     */
    IN_PROGRESS("in_progress"),
    /**
     * <code>GameEngine</code> has the control over request for user input.
     */
    GAME_ENGINE("game_engine");

    /**
     * Value of the state.
     */
    public String d_jsonValue;

    /**
     * Parameterised constructor to set the (json) string value of the enum member.
     *
     * @param p_jsonValue
     */
    private UserInteractionState(String p_jsonValue) {
        this.d_jsonValue = p_jsonValue;
    }

    /**
     * Gets the string value of the enum
     *
     * @return Value of the enum
     */
    public String getJsonValue() {
        return d_jsonValue;
    }
}
