package com.warzone.team08.VM.entities.orders;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.game_play.services.DistributeCountriesService;
import com.warzone.team08.VM.map_editor.services.EditMapService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class tests various operations performed during the execution of Deploy command. Uses
 * <code>DeployOrder</code>.
 *
 * @author Brijesh Lakkad
 */
public class DeployOrderTest {
    private static Application d_Application;
    private static URL d_TestFilePath;
    private static GamePlayEngine d_GamePlayEngine;
    private DistributeCountriesService d_distributeCountriesService;
    private List<Player> d_playerList;
    private Player d_player1;
    private Player d_player2;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application = new Application();
        d_Application.handleApplicationStartup();
        // (Re)initialise the VM.
        VirtualMachine.getInstance().initialise();

        d_GamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();
        d_TestFilePath = DeployOrderTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }

    /**
     * Setting up the required objects before performing test.
     *
     * @throws AbsentTagException        Throws if any tag is missing in the map file.
     * @throws InvalidMapException       Throws if map file is invalid.
     * @throws ResourceNotFoundException Throws if the file not found.
     * @throws InvalidInputException     Throws if user input is invalid.
     * @throws EntityNotFoundException   Throws if entity not found.
     * @throws URISyntaxException        Throws if URI syntax problem.
     */
    @Before
    public void before() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
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
        d_playerList = d_GamePlayEngine.getPlayerList();
        d_distributeCountriesService = new DistributeCountriesService();
        // Distributes countries between players.
        d_distributeCountriesService.distributeCountries();

        d_player1 = d_playerList.get(0);
        d_player2 = d_playerList.get(1);
    }

    /**
     * Tests Deploy operation when source country doesn't exist. Therefore, it will raise
     * <code>EntityNotFoundException</code>.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     */
    @Test(expected = EntityNotFoundException.class)
    public void testInvalidCountry() throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException {
        // Randomly passing any country name.
        DeployOrder l_deployOrder = new DeployOrder("INDIA", "10", d_player1);
        l_deployOrder.execute();
    }

    /**
     * Tests Deploy operation when number of armies moved are invalid (negative number). It will raise
     * InvalidInputException.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if exception if any argument is invalid.
     */
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidNoOfArmies()
            throws EntityNotFoundException, InvalidArgumentException {
        // Passing negative number of armies to move.
        new DeployOrder("Mercury-South", "-10", d_player1);
    }

    /**
     * Tests the advance operation when source and destination countries are owned by the same player. So it will simply
     * adds the number of armies. There will not be any battle.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     */
    @Test(expected = Test.None.class)
    public void testCorrectDeployOrder()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException {
        Country l_country = d_player1.getAssignedCountries().get(0);

        // Assign reinforcement to player 1.
        d_player1.setReinforcementCount(30);

        // Moving more armies than available armies.
        DeployOrder l_deployOrder = new DeployOrder(l_country.getCountryName(), "20", d_player1);
        l_deployOrder.execute();
        assertEquals(10, d_player1.getRemainingReinforcementCount());
    }
}