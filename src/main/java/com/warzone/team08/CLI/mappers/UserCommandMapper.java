package com.warzone.team08.CLI.mappers;

import com.warzone.team08.CLI.constants.specifications.ArgumentSpecification;
import com.warzone.team08.CLI.constants.specifications.CommandSpecification;
import com.warzone.team08.CLI.exceptions.InvalidArgumentException;
import com.warzone.team08.CLI.exceptions.InvalidCommandException;
import com.warzone.team08.CLI.layouts.UserCommandLayout;
import com.warzone.team08.CLI.models.CommandArgument;
import com.warzone.team08.CLI.models.PredefinedUserCommand;
import com.warzone.team08.CLI.models.UserCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts string entity of user text to UserCommand object that can be used to call the different APIs
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class UserCommandMapper {
    /**
     * Parses user input text and returns the UserCommand object.
     * <pre>
     * The UserCommand object is equivalent to the user text which is being used to call the APIs related to the command instruction
     * </pre>
     *
     * @param p_userInput Text entered by the user.
     * @return Value of interpreted UserCommand object from user input string.
     */
    public UserCommand toUserCommand(String p_userInput) {
        // Cracks a command line string
        List<String> l_commands = Arrays.asList(p_userInput.split("\\s"));

        l_commands = l_commands.stream().filter(l_command ->
                !l_command.trim().isEmpty()
        ).collect(Collectors.toList());

        if (l_commands.size() > 0) {
            // The command entered by the user
            String l_headOfCommand = l_commands.get(0);

            // Fetched predefined structure of the user command using the head
            // Use this to save interpreted instructions given by the user.
            PredefinedUserCommand l_predefinedUserCommand =
                    UserCommandLayout.matchAndGetUserCommand(l_headOfCommand);

            // Will create the parsed user command and also set the head for the command.
            UserCommand l_parsedUserCommand = new UserCommand(l_predefinedUserCommand);

            // Represents the body of the command line
            List<String> l_argumentBody = l_commands.subList(1, l_commands.size());

            // Throws an exception if the command needs its value to run and not provided by the user
            if (validateIfCommandDoesNeedValue(l_predefinedUserCommand, l_argumentBody.size())) {
                // This UserCommand instance will be returned from here.
                l_parsedUserCommand.setCommandValues(l_argumentBody);
                return l_parsedUserCommand;
            } else if (validateIfCommandCanRunAlone(l_predefinedUserCommand, l_argumentBody.size())) {
                // Throws an exception if the command can run alone and the user has provided with some random text
            }

            // If the command can accept arguments and user have provided the arguments
            if (l_predefinedUserCommand.getCommandArgumentList().size() > 0 &&
                    l_argumentBody.size() > 0) {
                // Stores the position of the argument keys in the argument body
                List<Integer> l_positionOfArgKeyList = new ArrayList<>();

                // Index, to keep track of the iteration over the l_argumentBody
                int l_index = 0;
                for (String l_command : l_argumentBody) {
                    if (l_predefinedUserCommand.isKeyOfCommand(l_command)) {
                        l_positionOfArgKeyList.add(l_index);
                    }
                    l_index++;
                }

                // Throws an exception if the input does not have any argument
                validateIfCommandDoesNeedArgument(l_predefinedUserCommand, l_positionOfArgKeyList.size());

                // If user has provided the argument(s)
                for (int l_currentPosIndex = 0; l_currentPosIndex < l_positionOfArgKeyList.size(); l_currentPosIndex++) {
                    // The index of the argument key and the next argument key in the found-position list
                    int l_nextPosIndex = l_currentPosIndex + 1;

                    // If there is next key, find the index of that key
                    int l_indexOfNextKey;
                    if (l_nextPosIndex == l_positionOfArgKeyList.size()) {
                        l_indexOfNextKey = l_argumentBody.size();
                    } else {
                        l_indexOfNextKey = l_positionOfArgKeyList.get(l_nextPosIndex);
                    }

                    // Get argument details for the current argument key
                    String l_currentArgKey = l_argumentBody.get(l_positionOfArgKeyList.get(l_currentPosIndex));
                    CommandArgument l_commandArgument = l_predefinedUserCommand.matchCommandArgument(l_currentArgKey);
                    int l_indexOfCurrentKey = l_positionOfArgKeyList.get(l_currentPosIndex);

                    // Get the number of values provided by the user for the current argument
                    List<String> l_values = l_argumentBody.subList(l_indexOfCurrentKey + 1, l_indexOfNextKey);

                    // Checks if the user has entered the correct number of values for the argument
                    if (l_commandArgument.getSpecification() == ArgumentSpecification.EQUAL &&
                            l_commandArgument.getNumOfValues() == l_values.size()) {
                        l_parsedUserCommand.pushUserArgument(l_commandArgument.getArgumentKey(), l_values);
                        continue;
                    } else if (l_commandArgument.getSpecification() == ArgumentSpecification.MAX &&
                            l_commandArgument.getNumOfValues() >= l_values.size()) {
                        l_parsedUserCommand.pushUserArgument(l_commandArgument.getArgumentKey(), l_values);
                        continue;
                    } else if (l_commandArgument.getSpecification() == ArgumentSpecification.MIN &&
                            l_commandArgument.getNumOfValues() <= l_values.size()) {
                        l_parsedUserCommand.pushUserArgument(l_commandArgument.getArgumentKey(), l_values);
                        continue;
                    }

                    // Throw if the user has not provided the correct number of values
                    if (l_commandArgument.getSpecification() != ArgumentSpecification.MAX &&
                            l_commandArgument.getNumOfValues() >= l_values.size()
                    ) {
                        throw new InvalidArgumentException("Required argument values not provided!");
                    } else {
                        throw new InvalidArgumentException("Unrecognised argument values!");
                    }
                }
            }
            return l_parsedUserCommand;
        } else {
            // If user has entered only spaces
            throw new InvalidCommandException("Invalid user input!");
        }
    }

    /**
     * Checks if the command doesn't need any argument to run
     *
     * @param p_predefinedUserCommand Value of the command which has the specification
     * @param p_numOfKeys             Value of the number of keys entered by the user
     * @return True if the command can run alone; false otherwise
     */
    private boolean validateIfCommandCanRunAlone(PredefinedUserCommand p_predefinedUserCommand, int p_numOfKeys) {
        if (p_predefinedUserCommand.getCommandSpecification() == CommandSpecification.CAN_RUN_ALONE) {
            // Means the user has entered the not-needful text after the command
            if (p_numOfKeys > 0)
                throw new InvalidArgumentException("Unrecognized argument!");
            return true;
        }
        return false;
    }

    /**
     * Checks if the command needs value to proceed
     *
     * @param p_predefinedUserCommand Value of the command which has the specification.
     * @param p_numOfKeys             Value of the number of text entered by the user after the command.
     * @return True if the command can run alone; false otherwise.
     */
    private boolean validateIfCommandDoesNeedValue(PredefinedUserCommand p_predefinedUserCommand, int p_numOfKeys) {
        if (p_predefinedUserCommand.getCommandSpecification() == CommandSpecification.CAN_RUN_ALONE_WITH_VALUE) {
            // Means the user has not provided the value required with the command
            if (p_numOfKeys == 0)
                throw new InvalidArgumentException("Value not provided!");
            if (p_numOfKeys != p_predefinedUserCommand.getNumOfKeysOrValues()) {
                throw new InvalidArgumentException("Required values are missing!");
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if the argument body is empty and the command needs an argument to run
     *
     * @param p_predefinedUserCommand Value of the command which has the specification
     * @param p_numOfKeys             Value of the number of keys entered by the user
     * @return True if the entered command has at least one argument; false otherwise
     */
    private boolean validateIfCommandDoesNeedArgument(PredefinedUserCommand p_predefinedUserCommand, int p_numOfKeys) {
        if (p_predefinedUserCommand.getCommandSpecification() == CommandSpecification.NEEDS_KEYS) {
            // Means the user has not provided any required argument keys
            if (p_numOfKeys == 0)
                throw new InvalidArgumentException("Command requires at least one argument to run!");
            if (p_predefinedUserCommand.getCommandKeySpecification() == ArgumentSpecification.EQUAL
                    && p_numOfKeys != p_predefinedUserCommand.getNumOfKeysOrValues()) {
                throw new InvalidArgumentException("Required values are missing!");
            } else if (p_predefinedUserCommand.getCommandKeySpecification() == ArgumentSpecification.MIN
                    && p_numOfKeys < p_predefinedUserCommand.getNumOfKeysOrValues()) {
                throw new InvalidArgumentException("Required values are missing!");
            } else if (p_predefinedUserCommand.getCommandKeySpecification() == ArgumentSpecification.MAX
                    && p_numOfKeys > p_predefinedUserCommand.getNumOfKeysOrValues()) {
                throw new InvalidArgumentException("Unrecognised argument values!");
            }
            return true;
        }
        return false;
    }
}
