package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidMapException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.map_editor.MapEditorEngine;

import java.util.List;

/**
 * This class contains methods for the validation of the map and handles `validatemap` user command.
 *
 * @author Deep Patel
 * @author Brijesh Lakkad
 */
public class ValidateConquestMapService implements SingleCommand {
    /**
     * Engine to store and retrieve map data.
     */
    private final MapEditorEngine d_mapEditorEngine;

    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Default constructor to retrieve the singleton instance of <code>MapEditorEngine</code> and
     * <code>LogEntryBuffer</code>.
     */
    public ValidateConquestMapService() {
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Checks that Continent has correct control value.
     *
     * @param p_continentList contains the list of all the continents.
     * @return True if the validation passes.
     */
    private boolean validationControlValue(List<Continent> p_continentList) {
        boolean l_isValid = true;

        for (Continent l_continent : p_continentList) {
            if (l_continent.getContinentControlValue() < 0) {
                l_isValid = false;
                break;
            }
        }
        return l_isValid;
    }

    /**
     * Initiate all the validation procedures. Checks all the validation and replies to the execute method.
     *
     * @param p_commandValues Values of command entered by user if any.
     * @return Value of the response.
     * @throws InvalidMapException     If the map is not valid.
     * @throws EntityNotFoundException If the entity not found.
     */
    @Override
    public String execute(List<String> p_commandValues) throws InvalidMapException, EntityNotFoundException {
        String l_logResponse = "\n---VALIDATEMAP---\n";
        //Checks map has at least 1 continent
        if (d_mapEditorEngine.getContinentList().size() > 0) {
            //Control value should be as per the warzone rules
            if (validationControlValue(d_mapEditorEngine.getContinentList())) {
                //Check for the minimum number of countries required
                if (d_mapEditorEngine.getCountryList().size() > 1) {
                    //check that every continent should have at least 1 country
                    if (d_mapEditorEngine.getCountryList().size() >= d_mapEditorEngine.getContinentList().size()) {
                        d_logEntryBuffer.dataChanged("validatemap", "Map validation passed successfully!");
                        return "Map validation passed successfully!";
                    } else {
                        d_logEntryBuffer.dataChanged("validatemap", "Total continents must be lesser or equal to the countries!");
                        throw new InvalidMapException("Total continents must be lesser or equal to the countries!");
                    }
                } else {
                    d_logEntryBuffer.dataChanged("validatemap", "At least one country required!");
                    throw new InvalidMapException("At least one country required!");
                }
            } else {
                d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "ControlValue is not valid!");
                throw new InvalidMapException("ControlValue is not valid!");
            }
        } else {
            d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "At least one continent required!");
            throw new InvalidMapException("At least one continent required!");
        }
    }
}