package com.warzone.team08.VM.entities.strategy;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.constants.interfaces.Order;
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
 * This class test the execution of the Aggressive Strategy.
 *
 * @author Deep Patel
 */
public class AggressiveStrategyTest {
    private static Application d_Application;
    private static URL d_TestFilePath;
    private static GamePlayEngine d_GamePlayEngine;
    private DistributeCountriesService d_distributeCountriesService;
    private List<Player> d_playerList;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void createPlayersList() {
        d_Application = new Application();
        d_Application.handleApplicationStartup();
        VirtualMachine.getInstance().initialise();
        d_GamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();
        d_TestFilePath = AggressiveStrategyTest.class.getClassLoader().getResource("test_map_files/test_strategy.map");
    }

    /**
     * Setting up the required objects before performing test.
     *
     * @throws AbsentTagException        Throws if any tag is missing in the map file.
     * @throws InvalidMapException       Throws if the map is invalid.
     * @throws ResourceNotFoundException Throws if resource is not available
     * @throws InvalidInputException     Throws if user input is invalid.
     * @throws EntityNotFoundException   Throws if entity not found.
     * @throws URISyntaxException        Throws if URI syntax problem.
     */
    @Before
    public void setup() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
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
        d_distributeCountriesService.distributeCountries();
    }

    /**
     * checks that execute method working properly.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     * @throws CardNotFoundException    Card doesn't found in the player's card list.
     */
    @Test
    public void testExecute() throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        for (Country traverse : l_player.getAssignedCountries()) {
            traverse.setNumberOfArmies(5);
        }
        Player l_player2 = d_playerList.get(1);
        for (Country traverse : l_player2.getAssignedCountries()) {
            traverse.setNumberOfArmies(5);
        }

        l_player.setReinforcementCount(6);
        VirtualMachine.getGameEngine().setTournamentMode(true);
        AggressiveStrategy l_check = new AggressiveStrategy(l_player);
        l_check.execute();
        for (Order l_travers : l_player.getOrders()) {
            l_travers.execute();
        }
        assertEquals(6, l_check.getOppositionCountry().getNumberOfArmies());
    }
}