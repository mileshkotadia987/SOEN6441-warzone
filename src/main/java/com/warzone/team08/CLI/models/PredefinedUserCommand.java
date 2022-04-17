package com.warzone.team08.CLI.models;

import com.warzone.team08.CLI.constants.specifications.ArgumentSpecification;
import com.warzone.team08.CLI.constants.specifications.CommandSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class represents the predefined structure and specification for the commands
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class PredefinedUserCommand {
    /**
     * Prefix to be used for the argument keys.
     */
    private static final String D_ARG_PREFIX = "-";

    /**
     * Represents the user command
     */
    private String d_headCommand;

    /**
     * Represents the arguments passed with the command
     */
    private final List<CommandArgument> d_commandArgumentList;

    /**
     * Includes the command specification
     */
    private CommandSpecification d_commandSpecification;

    /**
     * Specifies how many values (or keys with its value(s)) are required for this command. Can be used with
     * <code>CommandSpecification#CAN_RUN_ALONE_WITH_VALUE</code> or <code>CommandSpecification#NEED_KEYS</code>.
     * The default value the command can have is one.
     *
     * @see CommandSpecification#CAN_RUN_ALONE_WITH_VALUE
     * @see CommandSpecification#NEEDS_KEYS
     */
    private int d_numOfKeysOrValues = 1;

    /**
     * Used for <code>CommandSpecification#NEED_KEYS</code>, to specify how many keys the entered command should have.
     */
    private ArgumentSpecification d_commandKeySpecification = ArgumentSpecification.MIN;

    /**
     * Command represents that <code>GameEngine</code> is had requested CLI for user input.
     */
    private boolean d_isGameEngineCommand = false;

    /**
     * Command represents the order command.
     */
    private boolean d_isOrderCommand = false;

    /**
     * Represents the name of the method to be called for this user command.
     */
    private String d_gamePhaseMethodName;

    /**
     * Initialises the data members.
     */
    public PredefinedUserCommand() {
        // Initialise references
        d_commandArgumentList = new ArrayList<>();
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
     * Sets the head of command for this user command
     *
     * @param p_headCommand head of the command
     */
    public void setHeadCommand(String p_headCommand) {
        d_headCommand = p_headCommand;
    }

    /**
     * Gets the list of CommandArgument
     *
     * @return the list of CommandArgument
     */
    public List<CommandArgument> getCommandArgumentList() {
        return d_commandArgumentList;
    }

    /**
     * Adds element into the list of CommandArgument
     *
     * @param p_commandArgument argument key and its value object.
     */
    public void pushCommandArgument(CommandArgument p_commandArgument) {
        d_commandArgumentList.add(p_commandArgument);
    }

    /**
     * Gets the list of argument keys
     *
     * @return Value of the list of argument keys
     */
    public List<String> getArgumentKeys() {
        return this.d_commandArgumentList.stream().map((CommandArgument::getArgumentKey))
                .collect(Collectors.toList());
    }

    /**
     * Matches argument key with available arguments for this command.
     *
     * @param p_argumentKey Value of matched argument with the provided key.
     * @return Value of the list of available arguments for this command
     */
    public CommandArgument matchCommandArgument(String p_argumentKey) {
        // Returns only one element
        return this.d_commandArgumentList.stream().filter((p_p_argumentKey) ->
                p_argumentKey.equals(PredefinedUserCommand.D_ARG_PREFIX.concat(p_p_argumentKey.getArgumentKey()))
        ).collect(Collectors.toList()).get(0);
    }

    /**
     * Checks if the provided string is the key of this command
     *
     * @param p_argKey String to be checked if it is the key of this command
     * @return Value of true if the key belongs to this command; false otherwise
     */
    public boolean isKeyOfCommand(String p_argKey) {
        if (!p_argKey.startsWith(PredefinedUserCommand.D_ARG_PREFIX))
            return false;
        return this.getArgumentKeys().stream().anyMatch((p_p_argKey) ->
                p_argKey.equals(PredefinedUserCommand.D_ARG_PREFIX.concat(p_p_argKey))
        );
    }

    /**
     * Sets the specification for the command.
     *
     * @param p_commandSpecification New value of the command specification.
     */
    public void setCommandSpecification(CommandSpecification p_commandSpecification) {
        this.d_commandSpecification = p_commandSpecification;
    }

    /**
     * Gets the specification for the command.
     *
     * @return Value of the command specification.
     */
    public CommandSpecification getCommandSpecification() {
        return d_commandSpecification;
    }

    /**
     * Gets the number of values to be used with this command.
     *
     * @return Value of number of values.
     */
    public int getNumOfKeysOrValues() {
        return d_numOfKeysOrValues;
    }

    /**
     * Sets the number of values to be used with this command.
     *
     * @param p_numOfKeysOrValues Value of number of values.
     */
    public void setNumOfKeysOrValues(int p_numOfKeysOrValues) {
        d_numOfKeysOrValues = p_numOfKeysOrValues;
    }

    /**
     * <code>VM#GameEngine</code> needs input from user.
     *
     * @return True if the <code>VM#GameEngine</code> had requested.
     */
    public boolean isGameEngineCommand() {
        return d_isGameEngineCommand;
    }

    /**
     * Sets if the <code>VM#GameEngine</code> can ask input from user.
     *
     * @param p_gameEngineCommand Value of true if only <code>VM#GameEngine</code> can ask for this type of user
     *                            command.
     */
    public void setGameEngineCommand(boolean p_gameEngineCommand) {
        d_isGameEngineCommand = p_gameEngineCommand;
    }

    /**
     * Checks if the command belongs to order by the player.
     *
     * @return True if the command is order command.
     */
    public boolean isOrderCommand() {
        return d_isOrderCommand;
    }

    /**
     * Sets true if the command belongs to order by the player.
     *
     * @param p_orderCommand the command type.
     */
    public void setOrderCommand(boolean p_orderCommand) {
        d_isOrderCommand = p_orderCommand;
    }

    /**
     * Gets the method name to be called for this user command.
     *
     * @return Value of the method name.
     */
    public String getGamePhaseMethodName() {
        return d_gamePhaseMethodName;
    }

    /**
     * Sets the method name to be called for this user command.
     *
     * @param p_gamePhaseMethodName Value of the method name.
     */
    public void setGamePhaseMethodName(String p_gamePhaseMethodName) {
        d_gamePhaseMethodName = p_gamePhaseMethodName;
    }

    /**
     * Checks the head of the command and its argument key only
     *
     * @param l_p_o UserCommand need to checked with
     * @return true if both objects are equal
     */
    @Override
    public boolean equals(Object l_p_o) {
        if (this == l_p_o) return true;
        if (l_p_o == null || getClass() != l_p_o.getClass()) return false;
        PredefinedUserCommand l_that = (PredefinedUserCommand) l_p_o;
        return Objects.equals(d_headCommand, l_that.d_headCommand) &&
                Objects.equals(d_commandArgumentList, l_that.d_commandArgumentList);
    }

    /**
     * Sets the specification for the number of required keys for <code>CommandSpecification#NEED_KEYS</code> command.
     *
     * @param d_commandKeySpecification Specification for <code>CommandSpecification#NEED_KEYS</code> command.
     */
    public void setCommandKeySpecification(ArgumentSpecification d_commandKeySpecification) {
        this.d_commandKeySpecification = d_commandKeySpecification;
    }

    /**
     * Gets the specification for the number of required keys for <code>CommandSpecification#NEED_KEYS</code> command.
     *
     * @return Specification for <code>CommandSpecification#NEED_KEYS</code> command.
     */
    public ArgumentSpecification getCommandKeySpecification() {
        return d_commandKeySpecification;
    }
}
