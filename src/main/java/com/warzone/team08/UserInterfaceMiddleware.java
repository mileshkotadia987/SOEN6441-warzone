package com.warzone.team08;

/**
 * This interface represents the middleware for user-interface and VM instances.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public interface UserInterfaceMiddleware {
    /**
     * Asks user for input/action.
     *
     * @param p_message Message to be shown before asking for input.
     * @return Value of the response to the request.
     */
    String askForUserInput(String p_message);

    /**
     * Output channel for the user interface.
     *
     * @param p_message Represents the message.
     */
    void stdout(String p_message);

    /**
     * Channel which shows the user an error message.
     *
     * @param p_message Represents the error message.
     */
    void stderr(String p_message);
}
