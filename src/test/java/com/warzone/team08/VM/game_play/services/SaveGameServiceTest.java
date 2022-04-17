package com.warzone.team08.VM.game_play.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.common.services.SaveGameService;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.map_editor.services.EditMapService;
import com.warzone.team08.VM.phases.IssueOrder;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This file will tests whether <code>SaveGameService</code> can store the runtime information inside the file properly
 * or not.
 *
 * @author Brijesh Lakkad
 * @author Rutwik
 * @version 1.0
 */
public class SaveGameServiceTest {
    private static Application d_application;
    private static URL d_testFilePath;
    private static URL d_testSavedFilePath;
    private static GamePlayEngine d_gamePlayEngine;
    private DistributeCountriesService d_distributeCountriesService;
    private AssignReinforcementService d_assignReinforcementService;

    /**
     * Create temporary folder for test case.
     */
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    /**
     * This method runs before the test case runs. This method initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_application = new Application();
        d_application.handleApplicationStartup();
        VirtualMachine.getInstance().initialise();

        d_gamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();
        d_testFilePath = SaveGameServiceTest.class.getClassLoader().getResource("test_map_files/test_earth.map");
        d_testSavedFilePath = SaveGameServiceTest.class.getClassLoader().getResource("test_game_files/test_earth.warzone");
    }

    /**
     * Setting up the required objects before performing test.
     *
     * @throws AbsentTagException        Throws if any tag is missing in the json file.
     * @throws InvalidMapException       Throws if map file is invalid.
     * @throws ResourceNotFoundException Throws if the file not found.
     * @throws InvalidInputException     Throws if user input is invalid.
     * @throws EntityNotFoundException   Throws if entity not found.
     * @throws URISyntaxException        Throws if URI syntax problem.
     */
    @Before
    public void before() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
        d_gamePlayEngine.initialise();

        EditMapService l_editMapService = new EditMapService();
        assertNotNull(d_testFilePath);

        String l_url = new URI(d_testFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);

        Player l_player1 = new Player("player_1", StrategyType.HUMAN);
        Player l_player2 = new Player("player_2", StrategyType.HUMAN);

        d_gamePlayEngine.addPlayer(l_player1);
        d_gamePlayEngine.addPlayer(l_player2);

        d_distributeCountriesService = new DistributeCountriesService();
        // Distributes countries between players.
        d_distributeCountriesService.distributeCountries();

        d_assignReinforcementService = new AssignReinforcementService();
        d_assignReinforcementService.execute();

        VirtualMachine.getGameEngine().setGamePhase(new IssueOrder(VirtualMachine.getGameEngine()));
    }

    /**
     * Test that the content is being saved into the provided file or not.
     *
     * @throws IOException If any error occurred while saving the engines to file.
     */
    @Test(expected = Test.None.class)
    public void testSaveFile() throws IOException {
        SaveGameService l_saveGameService = new SaveGameService();
        l_saveGameService.toJSON();
        // This is the actual JSONObject.
        JSONObject l_actualJSONObject = l_saveGameService.getGameEngineJSONData();
        StringBuilder l_fileContentBuilder = new StringBuilder();
        try (BufferedReader  br = new BufferedReader(new FileReader(d_testSavedFilePath.getPath()))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                l_fileContentBuilder.append(sCurrentLine);
            }
        }
        // Load the string content in JSONObject. This is expected JSONObject.
        JSONObject l_savedJSONObject = new JSONObject(l_fileContentBuilder.toString());
        // Check if the both JSONObject are having the same content.
        boolean isEqual = l_actualJSONObject.similar(l_savedJSONObject);
        assertTrue(isEqual);
    }
}