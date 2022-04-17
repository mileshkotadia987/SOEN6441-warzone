package com.warzone.team08.VM.entities.orders;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
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
 * This class tests various operations performed during the execution of the advance command.
 *
 * @author CHARIT
 * @author Brijesh Lakkad
 */
public class AdvanceOrderTest {
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
        d_TestFilePath = AdvanceOrderTest.class.getClassLoader().getResource("test_map_files/test_map.map");
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
        //Distributes countries between players.
        d_distributeCountriesService.distributeCountries();

        d_player1 = d_playerList.get(0);
        d_player2 = d_playerList.get(1);
    }

    /**
     * Tests the advance operation when source and destination countries are invalid. Here countries are not present in
     * the list of available countries. So it will raise <code>EntityNotFoundException</code>.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     */
    @Test(expected = EntityNotFoundException.class)
    public void testInvalidCountry()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException {
        //Randomly passing country name.
        AdvanceOrder l_advanceOrder = new AdvanceOrder("INDIA", "CANADA", "10", d_player1);
        l_advanceOrder.execute();
    }

    /**
     * Tests the advance operation when number of armies moved are invalid(negative). It will raise
     * InvalidInputException.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if exception if any argument is invalid.
     */
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidNoOfArmies() throws EntityNotFoundException, InvalidArgumentException {
        //Passing negative number of armies to move.
        new AdvanceOrder("Mercury-South", "Mercury-East", "-10", d_player1);
    }

    /**
     * Tests the advance operation when the source country is invalid. Here source country is owned by another player,
     * which is invalid. It will raise InvalidInputException.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     */
    @Test(expected = InvalidOrderException.class)
    public void testAdvanceOrderAsInvalidSourceCountry()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException {
        // Passing the opponent player's country as a source country.
        // It will raise an InvalidInputException as we cannot move armies from another player's country.
        AdvanceOrder l_advanceOrder = new AdvanceOrder(d_player2.getAssignedCountries().get(0).getCountryName(), d_player2.getAssignedCountries().get(1).getCountryName(), "10", d_player1);
        l_advanceOrder.execute();
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
    public void testArmiesWhenMovedToOwnCountry()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException {
        Country l_country1 = d_player1.getAssignedCountries().get(0);
        Country l_country2 = d_player1.getAssignedCountries().get(1);

        //Randomly assigning armies to source country and destination country.
        l_country1.setNumberOfArmies(10);
        l_country2.setNumberOfArmies(20);

        //Moving more armies than available armies.
        AdvanceOrder l_advanceOrder = new AdvanceOrder(l_country1.getCountryName(), l_country2.getCountryName(), "20", d_player1);
        l_advanceOrder.execute();
        assertEquals(30, l_country2.getNumberOfArmies());
    }

    /**
     * Tests the advance operation when the owner of source and destination countries is not same.. So there will be a
     * battle. This function tests the successful battle.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     */
    @Test(expected = Test.None.class)
    public void testSuccessfulBattle()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException {
        Country l_country1 = d_player1.getAssignedCountries().get(4);
        Country l_country2 = d_player2.getAssignedCountries().get(0);

        //Randomly assigning armies to source country and destination country.
        l_country1.setNumberOfArmies(20);
        l_country2.setNumberOfArmies(5);

        // Manually assigning a card.
        d_player1.addCard(new BombCard());
        AdvanceOrder l_advanceOrder = new AdvanceOrder(l_country1.getCountryName(), l_country2.getCountryName(), "10", d_player1);
        l_advanceOrder.execute();
        assertEquals(10, l_country1.getNumberOfArmies());
        assertEquals(6, l_country2.getNumberOfArmies());
        assertEquals(d_player1, l_country2.getOwnedBy());
    }

    /**
     * Tests the advance operation when the owner of source and destination countries is not same.. So there will be a
     * battle. This function tests the unsuccessful battle.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     */
    @Test(expected = Test.None.class)
    public void testUnsuccessfulBattle()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException {
        Country l_country1 = d_player1.getAssignedCountries().get(4);
        Country l_country2 = d_player2.getAssignedCountries().get(0);

        //Randomly assigning armies to source country and destination country.
        l_country1.setNumberOfArmies(10);
        l_country2.setNumberOfArmies(5);

        // Manually assigning a card.
        d_player1.addCard(new BombCard());
        AdvanceOrder l_advanceOrder = new AdvanceOrder(l_country1.getCountryName(), l_country2.getCountryName(), "6", d_player1);
        l_advanceOrder.execute();
        assertEquals(6, l_country1.getNumberOfArmies());
        assertEquals(1, l_country2.getNumberOfArmies());
        assertEquals(d_player2, l_country2.getOwnedBy());
    }

    /**
     * test that in case of negotiation advance order will not execute.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     */
    @Test(expected = Test.None.class)
    public void testNegotiation()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException {
        Country l_country1 = d_player1.getAssignedCountries().get(4);
        Country l_country2 = d_player2.getAssignedCountries().get(0);

        //Randomly assigning armies to source country and destination country.
        l_country1.setNumberOfArmies(10);
        l_country2.setNumberOfArmies(5);

        //Negotiation between 2 players
        d_player1.addNegotiatePlayer(d_player2);

        AdvanceOrder l_advanceOrder = new AdvanceOrder(l_country1.getCountryName(), l_country2.getCountryName(), "6", d_player1);
        l_advanceOrder.execute();
        assertEquals(10, l_country1.getNumberOfArmies());
        assertEquals(5, l_country2.getNumberOfArmies());
    }
}