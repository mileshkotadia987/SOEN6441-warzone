package com.warzone.team08.VM.game_play.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.map_editor.services.EditMapService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class tests various operations performed during distribution of countries among players.
 *
 * @author CHARIT
 * @author Brijesh Lakkad
 */
public class DistributeCountriesServiceTest {
    private static Application d_Application;
    private static URL d_TestFilePath;
    private DistributeCountriesService d_distributeCountriesService;
    private static GamePlayEngine d_GamePlayEngine;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     *
     * @throws IOException        If exception while coping the predefined files.
     * @throws URISyntaxException If the path to parent directory of the files doesn't exist.
     */
    @BeforeClass
    public static void createPlayersList() throws IOException, URISyntaxException {
        d_Application = new Application();
        d_Application.handleApplicationStartup();

        // (Re)initialise the VM.
        VirtualMachine.getInstance().initialise();
        d_GamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();

        d_Application.restoreMapFiles();
        d_TestFilePath = DistributeCountriesServiceTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }

    /**
     * Setting up the required objects before performing test.
     *
     * @throws VMException        Exception generated during execution.
     * @throws URISyntaxException If error while parsing the string representing the path.
     */
    @Before
    public void before() throws VMException, URISyntaxException {
        d_GamePlayEngine.initialise();
        // Loads the map
        EditMapService l_editMapService = new EditMapService();
        assertNotNull(d_TestFilePath);
        String l_url = new URI(d_TestFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);

        Player l_player1 = new Player("User_1", StrategyType.HUMAN);
        Player l_player2 = new Player("User_2", StrategyType.HUMAN);

        d_GamePlayEngine.addPlayer(l_player1);
        d_GamePlayEngine.addPlayer(l_player2);

        d_distributeCountriesService = new DistributeCountriesService();
    }

    /**
     * Tests whether the player list is empty or not. Passes if list contains players objects, otherwise fails.
     *
     * @throws InvalidInputException Throws if invalid player count.
     */
    @Test(expected = Test.None.class)
    public void testNumberOfPlayer() throws InvalidInputException {
        d_distributeCountriesService.distributeCountries();
    }

    /**
     * Tests whether the count of countries required to be assigned to the player is correct or not.
     *
     * @throws InvalidInputException Throws if player objects list is empty.
     */
    @Test(expected = Test.None.class)
    public void testPlayerCountryCount() throws InvalidInputException {
        d_distributeCountriesService.distributeCountries();
        assertEquals(5, d_GamePlayEngine.getPlayerList().get(0).getAssignedCountryCount());
    }

    /**
     * Test whether the countries are correctly assigned or not.
     *
     * @throws InvalidInputException Throws if player objects list is empty.
     */
    @Test(expected = Test.None.class)
    public void testAssignedCountriesCount() throws InvalidInputException {
        String l_response = d_distributeCountriesService.distributeCountries();
        assertNotNull(l_response);

        int l_size = d_GamePlayEngine.getPlayerList().get(0).getAssignedCountries().size();
        assertEquals(5, l_size);
    }
}