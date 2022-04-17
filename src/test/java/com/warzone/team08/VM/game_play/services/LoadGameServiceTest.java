package com.warzone.team08.VM.game_play.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.common.services.LoadGameService;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.map_editor.services.EditMapService;
import com.warzone.team08.VM.repositories.PlayerRepository;
import com.warzone.team08.VM.utils.FileUtil;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * This game tests if the file is loaded successfully.
 *
 * @author Rutwik
 */
public class LoadGameServiceTest {
    private static Application d_application;
    private static URL d_testFilePath;
    private static URL d_testSavedFilePath;
    private static GamePlayEngine d_gamePlayEngine;
    private final PlayerRepository d_PLAYER_REPOSITORY = new PlayerRepository();

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_application = new Application();
        d_application.handleApplicationStartup();
        VirtualMachine.getInstance().initialise();

        d_gamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();
        d_testFilePath = LoadGameServiceTest.class.getClassLoader().getResource("test_map_files/test_earth.map");
        d_testSavedFilePath = LoadGameServiceTest.class.getClassLoader().getResource("test_game_files/test_earth.warzone");
    }

    /**
     * Sets the context of the test case for loading the game.
     *
     * @throws InvalidMapException       Throws if the map was not valid.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if the user command is invalid.
     * @throws AbsentTagException        Throws if any tag is missing in map file.
     * @throws EntityNotFoundException   Throws if entity is missing.
     * @throws URISyntaxException        If any path to file is not valid.
     */
    @Before
    public void before() throws InvalidInputException,
            EntityNotFoundException,
            InvalidMapException,
            ResourceNotFoundException,
            AbsentTagException,
            URISyntaxException {
        d_gamePlayEngine.initialise();

        EditMapService l_editMapService = new EditMapService();
        assertNotNull(d_testFilePath);

        String l_url = new URI(d_testFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);
    }

    /**
     * Tests the loading of a game file. This test case tests the information loaded into the game engines are valid or
     * not.
     *
     * @throws IOException If any exception while reading the file.
     * @throws VMException If any error while loading the game.
     */
    @Test(expected = Test.None.class)
    public void testLoadFile() throws VMException, IOException {
        LoadGameService l_loadGameService = new LoadGameService();
        File l_targetFile = FileUtil.retrieveGameFile(d_testSavedFilePath.getPath());
        StringBuilder l_fileContentBuilder = new StringBuilder();
        try (BufferedReader l_bufferedReader = new BufferedReader(new FileReader(l_targetFile))) {
            String l_currentLine;
            while ((l_currentLine = l_bufferedReader.readLine()) != null) {
                l_fileContentBuilder.append(l_currentLine);
            }
        }
        JSONObject jsonObject = new JSONObject(l_fileContentBuilder.toString());
        l_loadGameService.loadGameState(jsonObject);
        assertEquals(GamePlayEngine.getCurrentExecutionIndex(), 0);

        MapEditorEngine l_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        assertEquals(l_mapEditorEngine.getContinentList().size(), 2);

        Player l_player1 = d_PLAYER_REPOSITORY.findByPlayerName("player_1");
        assertEquals(l_player1.getAssignedCountries().size(), 4);
    }
}
