package com.warzone.team08.VM.game_play.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.map_editor.services.EditMapService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This Class will test the reinforced army as in the reinforced army is set correctly or not.
 *
 * @author Rutwik
 */
public class AssignReinforcementServiceTest {
    private static Application d_Application = new Application();
    private static MapEditorEngine d_MapEditorEngine;
    private static EditMapService d_EditMapService;
    private static URL d_TestFile;
    private static DistributeCountriesService d_DistributeCountriesService;
    private static AssignReinforcementService d_AssignReinforcementService;
    private static GamePlayEngine d_GamePlayEngine;


    /**
     * Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application.handleApplicationStartup();
        VirtualMachine.getInstance().initialise();
        d_GamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();
        d_MapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        d_TestFile = AssignReinforcementServiceTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }


    /**
     * Setting up the required Objects before test run.
     *
     * @throws InvalidInputException     Throws if provided argument and its value(s) are not valid.
     * @throws AbsentTagException        Throws if tag is absent in .map file.
     * @throws InvalidMapException       Throws if map file is invalid.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws EntityNotFoundException   Throws if entity not found while searching.
     * @throws URISyntaxException        If error while parsing the string representing the path.
     */
    @Before
    public void before() throws InvalidInputException, AbsentTagException, InvalidMapException, ResourceNotFoundException, EntityNotFoundException, URISyntaxException {
        d_GamePlayEngine.initialise();
        d_MapEditorEngine.initialise();
        d_MapEditorEngine.getCountryList();
        d_AssignReinforcementService = new AssignReinforcementService();

        Player l_player1 = new Player("USER_1", StrategyType.HUMAN);
        Player l_player2 = new Player("USER_1", StrategyType.HUMAN);

        d_GamePlayEngine.addPlayer(l_player1);
        d_GamePlayEngine.addPlayer(l_player2);

        d_EditMapService = new EditMapService();
        assertNotNull(d_TestFile);
        String l_url = new URI(d_TestFile.getPath()).getPath();
        d_EditMapService.handleLoadMap(l_url);
        d_DistributeCountriesService = new DistributeCountriesService();
        d_DistributeCountriesService.distributeCountries();
    }

    /**
     * This test will test the assign country list. It checks whether the assign country list is empty or not.
     */
    @Test(expected = Test.None.class)
    public void testAssignCountry() {
        for (Player l_player : d_GamePlayEngine.getPlayerList()) {
            assertNotNull(l_player.getAssignedCountries());
        }
    }

    /**
     * This Test will test the re-calculate reinforced army. It checks whether the army is reinforced properly or not.
     *
     * @throws EntityNotFoundException Throws if entity not found while searching.
     */
    @Test
    public void testingCalculatedReinforcedArmyValue() throws EntityNotFoundException {
        d_AssignReinforcementService.execute();
        int l_reinforcementArmies = d_GamePlayEngine.getPlayerList().get(0).getReinforcementCount();
        assertEquals(9, l_reinforcementArmies);

        int l_reinforcementArmies1 = d_GamePlayEngine.getPlayerList().get(1).getReinforcementCount();
        assertEquals(13, l_reinforcementArmies1);
    }
}
