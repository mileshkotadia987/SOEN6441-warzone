package com.warzone.team08.CLI.layouts.commands;

import com.warzone.team08.CLI.constants.specifications.ArgumentSpecification;
import com.warzone.team08.CLI.constants.specifications.CommandSpecification;
import com.warzone.team08.CLI.layouts.CommandLayout;
import com.warzone.team08.CLI.models.CommandArgument;
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
public class GamePlayCommandLayout implements CommandLayout {
    /**
     * The list of user commands which can be entered at the <code>GAME_PLAY</code> state of GameState
     */
    List<PredefinedUserCommand> d_userCommands;

    /**
     * Constructor sets the predefined user commands. These commands will be used to check the structure of a command
     * entered by the user.
     */
    public GamePlayCommandLayout() {
        d_userCommands = new ArrayList<>();

        PredefinedUserCommand l_userCommand;

        // Example of the command:
        // > gameplayer -add playername -remove playername
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("gameplayer");
        l_userCommand.setCommandSpecification(CommandSpecification.NEEDS_KEYS);
        l_userCommand.pushCommandArgument(new CommandArgument(
                "add",
                2,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.pushCommandArgument(new CommandArgument(
                "remove",
                1,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.setGamePhaseMethodName("setPlayers");
        d_userCommands.add(l_userCommand);

        // Example of the command:
        // > assigncountries
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("assigncountries");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setGamePhaseMethodName("assignCountries");
        d_userCommands.add(l_userCommand);

        // Example of the command:
        // > deploy countryID num
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("deploy");
        l_userCommand.setOrderCommand(true);
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setNumOfKeysOrValues(2);
        d_userCommands.add(l_userCommand);

        // Example of the command:
        // > advance countrynamefrom countynameto numarmies
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("advance");
        l_userCommand.setOrderCommand(true);
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setNumOfKeysOrValues(3);
        d_userCommands.add(l_userCommand);


        // Example of the command:
        // > bomb countryID
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("bomb");
        l_userCommand.setOrderCommand(true);
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setNumOfKeysOrValues(1);
        d_userCommands.add(l_userCommand);

        // Example of the command:
        // > blockade countryID
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("blockade");
        l_userCommand.setOrderCommand(true);
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setNumOfKeysOrValues(1);
        d_userCommands.add(l_userCommand);

        // Example of the command:
        // > airlift sourcecountryID targetcountryID numarmies
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("airlift");
        l_userCommand.setOrderCommand(true);
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setNumOfKeysOrValues(3);
        d_userCommands.add(l_userCommand);

        // Example of the command:
        // > negotiate playerID
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("negotiate");
        l_userCommand.setOrderCommand(true);
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setNumOfKeysOrValues(1);
        d_userCommands.add(l_userCommand);

        // Example of the command:
        // > savegame filename
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("savegame");
        l_userCommand.setGameEngineCommand(true);
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setNumOfKeysOrValues(1);
        l_userCommand.setGamePhaseMethodName("saveGame");
        d_userCommands.add(l_userCommand);

        // Example of the command:
        // > loadgame filename
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("loadgame");
        l_userCommand.setGameEngineCommand(true);
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setNumOfKeysOrValues(1);
        l_userCommand.setGamePhaseMethodName("loadGame");
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
