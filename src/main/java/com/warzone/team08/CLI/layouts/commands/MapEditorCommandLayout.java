package com.warzone.team08.CLI.layouts.commands;

import com.warzone.team08.CLI.constants.specifications.ArgumentSpecification;
import com.warzone.team08.CLI.constants.specifications.CommandSpecification;
import com.warzone.team08.CLI.layouts.CommandLayout;
import com.warzone.team08.CLI.models.CommandArgument;
import com.warzone.team08.CLI.models.PredefinedUserCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encompasses all the commands which can be entered by the user during the <code>MAP_EDITOR</code> game
 * state.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class MapEditorCommandLayout implements CommandLayout {
    /**
     * The list of user commands which can be entered at the <code>MAP_EDITOR</code> state of GameState
     */
    List<PredefinedUserCommand> d_userCommands;

    /**
     * Constructor sets the predefined user commands. These commands will be used to check the structure of a command
     * entered by the user.
     */
    public MapEditorCommandLayout() {
        d_userCommands = new ArrayList<>();

        // Example of the below command:
        // > editcontinent -add continentID continentvalue -remove continentID
        PredefinedUserCommand l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("editcontinent");
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
        l_userCommand.setGamePhaseMethodName("editContinent");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > editcountry -add countryID continentID -remove countryID
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("editcountry");
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
        l_userCommand.setGamePhaseMethodName("editCountry");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("editneighbor");
        l_userCommand.setCommandSpecification(CommandSpecification.NEEDS_KEYS);
        l_userCommand.pushCommandArgument(new CommandArgument(
                "add",
                2,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.pushCommandArgument(new CommandArgument(
                "remove",
                2,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.setGamePhaseMethodName("editNeighbor");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > savemap filename map_type
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("savemap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setGamePhaseMethodName("saveMap");
        l_userCommand.setNumOfKeysOrValues(2);
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > editmap filename
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("editmap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setGamePhaseMethodName("editMap");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > validatemap
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("validatemap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setGamePhaseMethodName("validateMap");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns
        l_userCommand = new PredefinedUserCommand();
        l_userCommand.setHeadCommand("tournament");
        l_userCommand.setCommandSpecification(CommandSpecification.NEEDS_KEYS);
        l_userCommand.pushCommandArgument(new CommandArgument(
                "M",
                1,
                ArgumentSpecification.MIN
        ));
        l_userCommand.pushCommandArgument(new CommandArgument(
                "P",
                2,
                ArgumentSpecification.MIN
        ));
        l_userCommand.pushCommandArgument(new CommandArgument(
                "G",
                1,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.pushCommandArgument(new CommandArgument(
                "D",
                1,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.setNumOfKeysOrValues(4);
        l_userCommand.setCommandKeySpecification(ArgumentSpecification.EQUAL);
        l_userCommand.setGamePhaseMethodName("prepareTournament");
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
