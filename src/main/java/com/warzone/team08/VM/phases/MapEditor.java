package com.warzone.team08.VM.phases;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.InvalidMapException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.map_editor.services.LoadConquestMapService;
import com.warzone.team08.VM.map_editor.services.LoadMapService;
import com.warzone.team08.VM.map_editor.services.ShowMapService;
import com.warzone.team08.VM.utils.FileUtil;
import com.warzone.team08.VM.utils.PathResolverUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * ConcreteState of the State pattern. In this example, defines behavior for commands that are valid in this state, and
 * for the others signifies that the command is invalid.
 * <p>
 * This state represents a group of states, and defines the behavior that is common to all the states in its group. All
 * the states in its group need to extend this class.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public abstract class MapEditor extends Phase {
    /**
     * Parameterised constructor to create an instance of <code>Edit</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    MapEditor(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String loadMap(List<String> p_arguments) throws VMException {
        // Resolve file path using absolute path of user data directory.
        String l_resolvedPathToFile = PathResolverUtil.resolveFilePath(p_arguments.get(0));
        if (new File(l_resolvedPathToFile).exists()) {
            // Try to retrieve the file
            FileUtil.retrieveMapFile(l_resolvedPathToFile);
            try (BufferedReader l_reader = new BufferedReader(new FileReader(l_resolvedPathToFile));) {
                // Will throw exception if the file path is not valid

                String l_currentLine;
                // If the line is empty, go to next.
                while ((l_currentLine = l_reader.readLine()) != null) {
                    if (!l_currentLine.trim().isEmpty()) {
                        break;
                    }
                }
                if (l_currentLine != null && l_currentLine.startsWith("[")) {
                    final String l_substring = l_currentLine.substring(l_currentLine.indexOf("[") + 1, l_currentLine.indexOf("]"));
                    if (l_substring.equalsIgnoreCase("continents")) {
                        LoadMapService l_loadMapService = new LoadMapService();
                        d_gameEngine.setGamePhase(new PlaySetup(d_gameEngine));
                        return l_loadMapService.execute(p_arguments);
                    } else if (l_substring.equalsIgnoreCase("Map")) {
                        LoadConquestMapService l_loadConquestMapService = new LoadConquestMapService();
                        d_gameEngine.setGamePhase(new PlaySetup(d_gameEngine));
                        return l_loadConquestMapService.execute(p_arguments);
                    } else {
                        throw new InvalidMapException("Unrecognised map file!");
                    }
                } else {
                    throw new InvalidMapException("Invalid map file!");
                }
            } catch (IOException p_ioException) {
                throw new VMException("Error while processing the file!");
            }
        } else {
            throw new InvalidInputException("Map file doesn't exists.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String setPlayers(String p_serviceType, List<String> p_arguments) throws VMException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String assignCountries(List<String> p_arguments) throws VMException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinforce() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void issueOrder() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fortify() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame(List<String> p_arguments) throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String showMap(List<String> p_arguments) throws VMException {
        ShowMapService l_showMapService = new ShowMapService();
        return l_showMapService.execute(p_arguments);
    }
}
