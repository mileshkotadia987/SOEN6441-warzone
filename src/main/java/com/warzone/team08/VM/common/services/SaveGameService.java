package com.warzone.team08.VM.common.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.exceptions.InvalidCommandException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.utils.FileUtil;
import com.warzone.team08.VM.utils.PathResolverUtil;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * This service saves the runtime information stored inside <code>Engines</code> attached to VM (directly or indirectly)
 * into a game file.
 *
 * @author Brijesh Lakkad
 * @author Rutwik
 * @version 1.0
 */
public class SaveGameService implements SingleCommand {
    private JSONObject d_currentGameEngine;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Initialize JSON object.
     */
    public SaveGameService() {
        d_currentGameEngine = new JSONObject();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Puts the values of each engine state into JSON Object.
     */
    public void toJSON() {
        d_currentGameEngine = VirtualMachine.getGameEngine().toJSON();
    }

    /**
     * Gets the JSONObject representing the information stored in engines.
     *
     * @return Values of JSONObject.
     */
    public JSONObject getGameEngineJSONData() {
        return d_currentGameEngine;
    }

    /**
     * This method will write the content into JSON file from GameEngine and its sub-engines:
     * <code>MapEditorEngine</code> and <code>GamePlayEngine</code>.
     *
     * @param p_commandValues Arguments containing a path to target file.
     * @return Value of response of the request.
     * @throws VMException If any error occurred while saving the engines to file.
     */
    @Override
    public String execute(List<String> p_commandValues) throws VMException {
        if (p_commandValues.size() <= 0) {
            throw new InvalidCommandException("Please provide the file name!");
        }
        this.toJSON();

        File l_targetFile = FileUtil.retrieveGameFile(
                PathResolverUtil.resolveFilePath(
                        p_commandValues.get(0).concat(".").concat(FileUtil.getGameExtension())
                ));

        try (Writer l_writer = new FileWriter(l_targetFile)) {
            l_writer.write(this.getGameEngineJSONData().toString(4));
            d_logEntryBuffer.dataChanged("savegame", "Game saved successfully with filename: "+p_commandValues.get(0));
            return "Game saved successfully!";
        } catch (IOException p_ioException) {
            throw new VMException("Error in file saving!");
        }
    }
}

