package com.warzone.team08.CLI.layouts.commands;

import com.warzone.team08.CLI.constants.specifications.CommandSpecification;
import com.warzone.team08.CLI.layouts.CommandLayout;
import com.warzone.team08.CLI.models.PredefinedUserCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encompasses all the commands which can be entered by the user during the <code>GAME_PLAY</code> game
 * state.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class CommonCommandLayout implements CommandLayout {
    /**
     * The list of user commands which can be entered at the <code>GAME_PLAY</code> state of GameState
     */
    List<PredefinedUserCommand> d_userCommands;

    /**
     * Creates the instance of <code>PredefinedUserCommand</code> and sets its variables and adds into the list of user
     * commands.
     */
    public CommonCommandLayout() {
        d_userCommands = new ArrayList<>();

        PredefinedUserCommand l_userCommand;

        // Example of the below command:
        // > loadmap filename
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("loadmap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setGamePhaseMethodName("loadMap");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > showmap
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("showmap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setGamePhaseMethodName("showMap");
        l_userCommand.setGameEngineCommand(true);
        d_userCommands.add(l_userCommand);

        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("done");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setOrderCommand(true);
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > exit
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("exit");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setGamePhaseMethodName("endGame");
        l_userCommand.setGameEngineCommand(true);
        d_userCommands.add(l_userCommand);
    }

    /**
     * {@inheritDoc}
     *
     * @return Value of the list of user commands for this class.
     */
    @Override
    public List<PredefinedUserCommand> getUserCommands() {
        return this.d_userCommands;
    }
}
