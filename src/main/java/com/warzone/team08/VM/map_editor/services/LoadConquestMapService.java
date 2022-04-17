package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.utils.PathResolverUtil;

import java.util.List;

/**
 * This file loads conquest map file in the user console.
 * <p>
 * This service handles `loadmap` user command.
 *
 * @author CHARIT
 */
public class LoadConquestMapService implements SingleCommand {

    private LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Handles the load map operation for user command.
     *
     * @param p_commandValues Represents the values passed while running the command.
     * @return Value of string acknowledging user that the file is loaded or not.
     * @throws InvalidMapException       Throws if the map was not valid.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if the user command is invalid.
     * @throws AbsentTagException        Throws if any tag is missing in map file.
     * @throws EntityNotFoundException   Throws if entity is missing.
     * @see EditMapService#handleLoadMap
     */
    @Override
    public String execute(List<String> p_commandValues) throws InvalidMapException,
            ResourceNotFoundException,
            InvalidInputException,
            AbsentTagException,
            EntityNotFoundException {
        try {
            EditConquestMapService l_editConquestMapService = new EditConquestMapService();
            // Resolve file path using absolute path of user data directory.
            String l_resolvedPathToFile = PathResolverUtil.resolveFilePath(p_commandValues.get(0));
            String l_response = l_editConquestMapService.loadConquestMap(l_resolvedPathToFile, false);
            try {
                // Validates the map before saving the file.
                ValidateConquestMapService l_validateObj = new ValidateConquestMapService();
                l_validateObj.execute(null);
            } catch (InvalidMapException | EntityNotFoundException l_e) {
                VirtualMachine.getGameEngine().getMapEditorEngine().initialise();
                throw l_e;
            }
            // Logging
            d_logEntryBuffer.dataChanged("loadmap", l_response);
            return l_response;
        } catch (ArrayIndexOutOfBoundsException p_e) {
            throw new InvalidInputException("File name is empty!");
        }
    }
}
