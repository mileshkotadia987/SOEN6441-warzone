package com.warzone.team08.VM.common.services;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.constants.enums.FileType;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.repositories.CountryRepository;
import com.warzone.team08.VM.utils.FileUtil;
import com.warzone.team08.VM.utils.PathResolverUtil;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * This service loads the information stored inside <code>Engines</code> attached to VM (directly or indirectly) from a
 * game file.
 *
 * @author Brijesh Lakkad
 * @author Rutwik
 * @version 1.0
 */
public class LoadGameService implements SingleCommand {
    private LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Loads the game engine and its sub-engines from the provided path to JSON file.
     *
     * @param p_targetJSON File content needed to be loaded into the engines.
     * @return Value of response of the request.
     * @throws VMException If any error occurred while loading the engines.
     */
    public String loadGameState(JSONObject p_targetJSON) throws VMException {
        GameEngine.fromJSON(p_targetJSON);
        return "Game loaded successfully";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute(List<String> p_commandValues) throws VMException {
        // Check if the file has valid extension.
        FileUtil.checksIfFileHasRequiredExtension(p_commandValues.get(0), FileType.GAME);

        File l_targetFile = FileUtil.retrieveGameFile(
                PathResolverUtil.resolveFilePath(
                        p_commandValues.get(0)
                ));
        StringBuilder l_fileContentBuilder = new StringBuilder();
        try (BufferedReader l_bufferedReader = new BufferedReader(new FileReader(l_targetFile))) {
            String l_currentLine;
            while ((l_currentLine = l_bufferedReader.readLine()) != null) {
                l_fileContentBuilder.append(l_currentLine);
            }
        } catch (IOException p_ioException) {
            throw new VMException(String.format("Error while loading the game file %s!", p_commandValues.get(0)));
        }
        d_logEntryBuffer.dataChanged("loadgame", "Game loaded from file: " + p_commandValues.get(0));

        // Load the string content in JSONObject.
        return this.loadGameState(new JSONObject(l_fileContentBuilder.toString()));


    }
}
