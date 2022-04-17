package com.warzone.team08.VM.entities.orders;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.entities.cards.AirliftCard;
import com.warzone.team08.VM.entities.cards.BombCard;
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
 * This class tests various operations performed during the execution of the airlift command.
 *
 * @author Deep Patel
 */
public class AirliftOrderTest {
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
        d_TestFilePath = BombOrderTest.class.getClassLoader().getResource("map_files/solar.map");
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
    public void testExecute()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        List<Country> l_playerAssignCountries = l_player.getAssignedCountries();
        l_playerAssignCountries.get(0).setNumberOfArmies(7);
        l_playerAssignCountries.get(1).setNumberOfArmies(5);
        int l_expected = l_playerAssignCountries.get(1).getNumberOfArmies();
        l_player.addCard(new AirliftCard());

        AirliftOrder l_airliftOrder = new AirliftOrder(l_playerAssignCountries.get(0).getCountryName(), l_playerAssignCountries.get(1).getCountryName(), "2", l_player);
        l_airliftOrder.execute();
        assertEquals(l_playerAssignCountries.get(1).getNumberOfArmies(), l_expected + 2);
    }

    /**
     * checks that player has airlift card.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     * @throws CardNotFoundException    Card doesn't found in the player's card list.
     */
    //not have airlift card
    @Test(expected = CardNotFoundException.class)
    public void testPlayerHasCard()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        List<Country> l_playerAssignCountries = l_player.getAssignedCountries();
        l_playerAssignCountries.get(0).setNumberOfArmies(7);
        l_playerAssignCountries.get(1).setNumberOfArmies(5);
        int l_expected = l_playerAssignCountries.get(1).getNumberOfArmies();
        l_player.addCard(new BombCard());

        AirliftOrder l_airliftOrder = new AirliftOrder(l_playerAssignCountries.get(0).getCountryName(), l_playerAssignCountries.get(1).getCountryName(), "2", l_player);
        l_airliftOrder.execute();
    }

    /**
     * checks that player will not airlift armies in others country.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     * @throws CardNotFoundException    Card doesn't found in the player's card list.
     */
    //try to transfer into other's country
    @Test(expected = InvalidOrderException.class)
    public void testPlayerNotAirliftInOthersCountry()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        List<Country> l_playerAssignCountries = l_player.getAssignedCountries();
        List<Country> l_playerAssignCountries1 = l_player2.getAssignedCountries();
        l_player.addCard(new AirliftCard());

        AirliftOrder l_airliftOrder = new AirliftOrder(l_playerAssignCountries.get(0).getCountryName(), l_playerAssignCountries1.get(0).getCountryName(), "2", l_player);
        l_airliftOrder.execute();
    }

    /**
     * method test that source country has entered number of armies for airlifted.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     * @throws CardNotFoundException    Card doesn't found in the player's card list.
     */
    @Test(expected = InvalidOrderException.class)
    public void testPlayerHasEnteredArmies()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        List<Country> l_playerAssignCountries = l_player.getAssignedCountries();
        l_playerAssignCountries.get(0).setNumberOfArmies(7);
        l_playerAssignCountries.get(1).setNumberOfArmies(5);
        int l_expected = l_playerAssignCountries.get(1).getNumberOfArmies();
        l_player.addCard(new AirliftCard());

        AirliftOrder l_airliftOrder = new AirliftOrder(l_playerAssignCountries.get(0).getCountryName(), l_playerAssignCountries.get(1).getCountryName(), "8", l_player);
        l_airliftOrder.execute();
    }
}