package com.warzone.team08.VM.game_play.services;

import com.jakewharton.fliptables.FlipTable;
import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.*;
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
 * This test class tests the game_play's ShowMapService class functions
 *
 * @author MILESH
 */
public class ShowMapServiceTest {
    private ShowMapService d_showMapService;
    private List<Player> d_playerList;
    private static URL d_TestFilePath;

    /**
     * Setting up the context by loading the map file before testing the class methods.
     */
    @BeforeClass
    public static void beforeClass() {
        Application l_application = new Application();
        l_application.handleApplicationStartup();

        d_TestFilePath = ShowMapServiceTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }

    /**
     * This method will initialise the ShowMapService object before running each test cases.
     *
     * @throws InvalidInputException     Throws if provided argument and its value(s) are not valid.
     * @throws EntityNotFoundException   Throws if entity not found while searching.
     * @throws URISyntaxException        Throws if file name could not be parsed as a URI reference.
     * @throws AbsentTagException        Throws if tag is absent in .map file.
     * @throws InvalidMapException       Throws if map file is invalid.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if provided argument and its value(s) are not valid.
     * @throws EntityNotFoundException   Throws if entity not found while searching.
     */
    @Before
    public void before() throws InvalidInputException, EntityNotFoundException, URISyntaxException, InvalidMapException, ResourceNotFoundException, AbsentTagException {
        // (Re)initialise the VM.
        VirtualMachine.getInstance().initialise();

        EditMapService l_editMapService = new EditMapService();
        d_showMapService = new ShowMapService();
        assertNotNull(d_TestFilePath);
        String l_url = new URI(d_TestFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);

        DistributeCountriesService l_distributeCountriesService = new DistributeCountriesService();
        PlayerService l_playerService = new PlayerService();
        l_playerService.add("User_1", StrategyType.HUMAN.getJsonValue());
        l_playerService.add("User_2", StrategyType.HUMAN.getJsonValue());

        l_distributeCountriesService.distributeCountries();
        d_playerList = VirtualMachine.getGameEngine().getGamePlayEngine().getPlayerList();
    }

    /**
     * It tests the showPlayerContent method which returns the String of player information
     */
    @Test
    public void testShowPlayerContent() {
        String[] l_header1 = {"USER_1", "Mercury-South", "Mercury-East", "Mercury-West", "Mercury-North", "Venus-South"};
        String[] l_playerContent1 = {"Army Count", "0", "0", "0", "0", "0"};

        String l_PlayerExpectedData = FlipTable.of(l_header1, new String[][]{l_playerContent1});
        String l_playerActualData = d_showMapService.showPlayerContent(d_playerList.get(0));
        assertEquals(l_PlayerExpectedData, l_playerActualData);
    }
}