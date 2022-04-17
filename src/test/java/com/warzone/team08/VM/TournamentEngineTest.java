package com.warzone.team08.VM;

import com.warzone.team08.Application;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.VMException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class tests tournament engine.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class TournamentEngineTest {
    private static final Application d_Application = new Application();
    private static final List<String> d_mapFiles = new ArrayList<>();
    private TournamentEngine d_tournamentEngine;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     *
     * @throws URISyntaxException If any path to file is not valid.
     */
    @BeforeClass
    public static void beforeClass() throws URISyntaxException {
        d_Application.handleApplicationStartup();
        d_mapFiles.add(TournamentEngineTest.class.getClassLoader().getResource("test_map_files/test_earth.map").toURI().getPath());
        d_mapFiles.add(TournamentEngineTest.class.getClassLoader().getResource("test_map_files/test_craters.map").toURI().getPath());
    }

    /**
     * Re-initializes the continent list before test case run.
     */
    @Before
    public void beforeTestCase() {
        // (Re)initialise Virtual Machine.
        VirtualMachine.getInstance().initialise();

        // Setup TournamentEngine.
        d_tournamentEngine = VirtualMachine.TOURNAMENT_ENGINE();
        d_tournamentEngine.setMapFileList(d_mapFiles);
        d_tournamentEngine.setMaxNumberOfTurns(3);
        d_tournamentEngine.setNumberOfGames(20);
        d_tournamentEngine.addPlayer(new Player("Player_1", StrategyType.AGGRESSIVE));
        d_tournamentEngine.addPlayer(new Player("Player_2", StrategyType.BENEVOLENT));
    }

    /**
     * Tests if the tournament iterates through provided map files and given numbers of turn, and provides the results
     * without any exception.
     *
     * @throws VMException If any exception while executing the tournament.
     */
    @Test(expected = Test.None.class)
    public void testTournament() throws VMException {
        d_tournamentEngine.onStart(true);
    }
}