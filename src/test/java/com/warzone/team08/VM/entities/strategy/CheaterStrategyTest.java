package com.warzone.team08.VM.entities.strategy;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.game_play.services.DistributeCountriesService;
import com.warzone.team08.VM.map_editor.services.EditMapService;
import com.warzone.team08.VM.repositories.CountryRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class test the execution of the Aggressive Strategy.
 *
 * @author Deep Patel
 */
public class CheaterStrategyTest {
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
        d_TestFilePath = CheaterStrategy.class.getClassLoader().getResource("test_map_files/test_cheaterStrategy.map");
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
        l_player.getAssignedCountries().get(0).setNumberOfArmies(1);

        CountryRepository l_countryRepository = new CountryRepository();
        Country l_temp = l_countryRepository.findFirstByCountryName("Nepal");
        l_temp.setNumberOfArmies(3);
        //First this country has to be oppositions country
        assertNotEquals(l_player, l_temp.getOwnedBy());
        l_player.setReinforcementCount(1);
        VirtualMachine.getGameEngine().setTournamentMode(true);

        CheaterStrategy l_cheatStrategy = new CheaterStrategy(l_player);
        l_cheatStrategy.execute();
        //After execution same country is owned by the cheater player
        assertEquals(l_player, l_temp.getOwnedBy());
        //armies count will be doubled, If it has enemy neighbour
        assertEquals(6, l_temp.getNumberOfArmies());
    }
}