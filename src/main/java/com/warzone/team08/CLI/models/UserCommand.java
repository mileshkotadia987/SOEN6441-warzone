package com.warzone.team08.CLI.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * This class represents the interpreted command from user input text.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class UserCommand {
    /**
     * Represents the user command.
     */
    private String d_headCommand;

    /**
     * Represents list of the arguments and its values.
     */
    private List<Map<String, List<String>>> d_userArguments;

    /**
     * Value(s) of the head of the command if any.
     */
    private List<String> d_commandValues;

    /**
     * Represents the predefined command for this command. This property will be ignored while
     * <code>ObjectMapper#writeValueAsString</code>
     */
    private PredefinedUserCommand d_predefinedUserCommand;

    /**
     * True if the user commands represents the <code>exit</code> command.
     */
    private boolean d_isExitCommand = false;

    /**
     * Parameterised constructor to set the predefined user command for the command entered by the user.
     * <p>Note that there is not <code>setter</code> method to set <code>PredefinedUserCommand</code>.
     *
     * <p>
     * This constructor also initialises its data member.
     *
     * @param p_predefinedUserCommand Value of predefined user command that will be used while interpreting the user
     *                                text.
     */
    public UserCommand(PredefinedUserCommand p_predefinedUserCommand) {
        setHeadCommand(p_predefinedUserCommand.getHeadCommand());
        d_predefinedUserCommand = p_predefinedUserCommand;
        // Initialise references
        d_userArguments = new ArrayList<>();
        d_commandValues = new ArrayList<>();
    }

    /**
     * When user wants to terminate the application.
     */
    public UserCommand() {

    }

    /**
     * Gets the head of command for this user command
     *
     * @return head of the command
     */
    public String getHeadCommand() {
        return d_headCommand;
    }

    /**
     * Sets the head of command for this user command.
     * <p>
     * Only accessible with in the class which will be used at the constructor.
     *
     * @param p_headCommand head of the command
     */
    private void setHeadCommand(String p_headCommand) {
        d_headCommand = p_headCommand;
    }

    /**
     * Gets the list of argument key and its value(s)
     *
     * @return Value of the list of argument key and its value(s)
     */
    @JsonIgnore
    public List<Map<String, List<String>>> getUserArguments() {
        return d_userArguments;
    }

    /**
     * Adds element to the list of user argument mappings.
     *
     * @param argKey Value of argument key.
     * @param values Value of the list of argument values.
     */
    public void pushUserArgument(String argKey, List<String> values) {
        Map<String, List<String>> l_newArgumentKeyValue = new HashMap<>();
        l_newArgumentKeyValue.put(argKey, values);
        d_userArguments.add(l_newArgumentKeyValue);
    }

    /**
     * Gets the values of the head of the command if any.
     *
     * @return Values of the head of the command if any.
     */
    public List<String> getCommandValues() {
        return d_commandValues;
    }

    /**
     * Sets new values for the command.
     *
     * @param d_commandValues New value for the command.
     */
    public void setCommandValues(List<String> d_commandValues) {
        this.d_commandValues = d_commandValues;
    }

    /**
     * Gets the predefined version of the user command.
     * <p>Can be used at taking action for the appropriate command.
     *
     * @return Value of predefined command.
     */
    @JsonIgnore
    public PredefinedUserCommand getPredefinedUserCommand() {
        return d_predefinedUserCommand;
    }

    /**
     * Checks the head of the command and its argument key only
     *
     * @param p_l_o UserCommand need to checked with
     * @return true if both objects are equal
     */
    @Override
    public boolean equals(Object p_l_o) {
        if (this == p_l_o) return true;
        if (p_l_o == null || getClass() != p_l_o.getClass()) return false;
        UserCommand l_that = (UserCommand) p_l_o;
        return d_isExitCommand == l_that.d_isExitCommand &&
                Objects.equals(d_headCommand, l_that.d_headCommand) &&
                d_userArguments.size() == l_that.d_userArguments.size() &&
                Objects.equals(d_predefinedUserCommand, l_that.d_predefinedUserCommand);
    }
}
