package com.warzone.team08.CLI.layouts;

import com.warzone.team08.CLI.models.PredefinedUserCommand;

import java.util.List;

/**
 * Interface to define the method(s) which should be implemented for various command-layout classes. The command-layout
 * are the classes that define the available commands throughout a game state.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public interface CommandLayout {
    /**
     * Gets the list of the user commands for the inheriting class. Inheriting class must have the list of user commands
     * created in the constructor or static block.
     *
     * @return the list of commands
     */
    public List<PredefinedUserCommand> getUserCommands();
}
