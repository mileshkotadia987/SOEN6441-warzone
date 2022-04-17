package com.warzone.team08.VM.entities.orders;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.entities.cards.BlockadeCard;
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
 * This class tests various operations performed during the execution of the blockade command.
 *
 * @author CHARIT
 */
public class BlockadeOrderTest {
    private static Application d_Application;
    private static URL d_TestFilePath;
    private static GamePlayEngine d_GamePlayEngine;
    private DistributeCountriesService d_distributeCountriesService;
    private List<Player> d_playerList;

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
        d_TestFilePath = BlockadeOrderTest.class.getClassLoader().getResource("test_map_files/test_map.map");
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
        d_distributeCountriesService.distributeCountries();
    }

    /**
     * Tests the blockade operation for the player having blockade card.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = Test.None.class)
    public void testBlockadeOperationWithBlockadeCard()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        List<Country> l_player1AssignCountries = l_player1.getAssignedCountries();
        l_player1.addCard(new BlockadeCard());
        BlockadeOrder l_blockadeOrder = new BlockadeOrder(l_player1AssignCountries.get(0).getCountryName(), l_player1);
        Country l_country = l_player1AssignCountries.get(0);
        l_country.setNumberOfArmies(10);
        int l_armies = l_country.getNumberOfArmies();
        l_blockadeOrder.execute();
        assertEquals(l_country.getNumberOfArmies(), l_armies * 3);
    }

    /**
     * Tests the blockade operation for the player not having blockade card.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */

    @Test(expected = CardNotFoundException.class)
    public void testBlockadeOperationWithOutBlockadeCard()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        List<Country> l_player1AssignCountries = l_player1.getAssignedCountries();
        l_player1.addCard(new BombCard());
        BlockadeOrder l_blockadeOrder = new BlockadeOrder(l_player1AssignCountries.get(0).getCountryName(), l_player1);
        Country l_country = l_player1AssignCountries.get(0);
        l_country.setNumberOfArmies(10);
        int l_armies = l_country.getNumberOfArmies();
        l_blockadeOrder.execute();
        assertEquals(l_country.getNumberOfArmies(), l_armies * 3);
    }

    /**
     * Tests the blockade operation when player performs blockade operation on other player's country country.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = InvalidOrderException.class)
    public void testBlockadeOperationOnOtherPlayerOwnedCountry()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        List<Country> l_player2AssignCountries = l_player2.getAssignedCountries();
        l_player1.addCard(new BlockadeCard());
        BlockadeOrder l_blockadeOrder = new BlockadeOrder(l_player2AssignCountries.get(0).getCountryName(), l_player1);
        l_blockadeOrder.execute();
    }

    /**
     * Tests whether the first execution has removed the card from the list of cards available to player.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = CardNotFoundException.class)
    public void testCardSuccessfullyRemoved() throws
            EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        List<Country> l_player1AssignCountries = l_player1.getAssignedCountries();
        l_player1.addCard(new BlockadeCard());
        BlockadeOrder l_blockadeOrder = new BlockadeOrder(l_player1AssignCountries.get(0).getCountryName(), l_player1);
        l_blockadeOrder.execute();

        //Now during second execution player will not have a blockade card as we have assigned only one blockade card manually.
        //So it will raise InvalidCommandException.
        l_blockadeOrder.execute();
    }
}
