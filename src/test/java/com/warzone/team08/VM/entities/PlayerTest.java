package com.warzone.team08.VM.entities;

import com.warzone.team08.Application;
import com.warzone.team08.CLI.CommandLineInterface;
import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.map_editor.services.EditMapService;
import com.warzone.team08.VM.phases.PlaySetup;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;

/**
 * Tests if the player can issue the order correctly.
 *
 * @author Brijesh Lakkad
 * @author MILESH
 * @version 1.0
 */
public class PlayerTest {
    private static Application d_Application;
    private CommandLineInterface d_commandLineInterface;
    private EditMapService d_editMapService;
    private MapEditorEngine d_mapEditorEngine;
    private static URL d_TestFilePath;

    /**
     * Set the application environment.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application = new Application();
        d_Application.handleApplicationStartup();
        d_TestFilePath = PlayerTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }

    /**
     * Loads different objects and performs necessary operations required to execute testcase.
     *
     * @throws AbsentTagException        Throws if any tag is missing in map file.
     * @throws InvalidMapException       Throws if map is invalid.
     * @throws ResourceNotFoundException Throws if map file not found.
     * @throws InvalidInputException     Throws if input command is invalid.
     * @throws EntityNotFoundException   Throws if entity not found.
     * @throws URISyntaxException        If error while parsing the string representing the path.
     * @see EditMapService#handleLoadMap If any exception thrown.
     */
    @Before
    public void beforeTestCase() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
        // CLI to read and interpret the user input
        d_commandLineInterface = new CommandLineInterface(d_Application);

        // (Re)initialise the VM.
        VirtualMachine.getInstance().initialise();

        // EditMap service to load the map
        d_editMapService = new EditMapService();
        assertNotNull(d_TestFilePath);
        String l_url = new URI(d_TestFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);

        GameEngine l_gameEngine = VirtualMachine.getGameEngine();

        // Set the game state to GAME_PLAY
        l_gameEngine.setGamePhase(new PlaySetup(l_gameEngine));
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();

        List<Country> l_assignedCountries = d_mapEditorEngine.getCountryList().subList(0, Math.min(4, d_mapEditorEngine.getCountryList().size()));
        Player l_player1 = new Player("User_1", StrategyType.HUMAN);
        l_player1.setAssignedCountries(l_assignedCountries);
        l_player1.setReinforcementCount(10);

        Player l_player2 = new Player("User_2", StrategyType.HUMAN);
        l_player2.setAssignedCountries(l_assignedCountries);
        l_player2.setReinforcementCount(10);

        VirtualMachine.getGameEngine().getGamePlayEngine().addPlayer(l_player1);
        VirtualMachine.getGameEngine().getGamePlayEngine().addPlayer(l_player2);
    }

    /**
     * Tests the player issue order functionality. An order is tested against the user input and it will be stored in
     * the player's order list.
     *
     * @throws VMException          Throws if any exception while processing the issue order request.
     * @throws ExecutionException   Throws if error occurs in execution.
     * @throws InterruptedException Throws if interruption occurs during normal execution.
     */
    @Test
    public void testIssueOrder() throws VMException, ExecutionException, InterruptedException {
        // User input text.
        String l_orderInput = "deploy Mercury-South 5";

        d_commandLineInterface.setIn(new ByteArrayInputStream(l_orderInput.getBytes()));
        VirtualMachine.getGameEngine().getGamePlayEngine().getPlayerList().get(0).issueOrder();
    }
}

