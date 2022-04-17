package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.utils.FileUtil;
import com.warzone.team08.VM.utils.PathResolverUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is to save the edited Map File.
 * <p>
 * The service handles `savemap` user command.
 *
 * @author Rutwik
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class SaveMapService implements SingleCommand {
    /**
     * Engine to store and retrieve map data.
     */
    private final MapEditorEngine d_mapEditorEngine;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Fetches the singleton instance of <code>MapEditorEngine</code>
     */
    public SaveMapService() {
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Reads from map editor engine and saves the data to file.
     *
     * @param p_fileObject File path to create if not exists and write into files.
     * @return Value of response of the request.
     * @throws InvalidInputException Throws if the file write operation was not successful.
     */
    public String saveToFile(File p_fileObject) throws InvalidInputException {
        try (Writer l_writer = new FileWriter(p_fileObject)) {
            l_writer.write("[" + "Continents" + "]\n");

            for (Continent continents : d_mapEditorEngine.getContinentList()) {
                l_writer.write(continents.getContinentName() + " " + continents.getContinentControlValue() + "\n");
            }

            l_writer.write("\n[" + "Countries" + "]\n");

            for (Country country : d_mapEditorEngine.getCountryList()) {
                l_writer.write(country.getCountryId() + " " +
                        country.getCountryName() + " " +
                        country.getContinent().getContinentId() + " " +
                        country.getXCoordinate() + " " +
                        country.getYCoordinate() + " " +
                        "\n");
            }

            l_writer.write("\n[" + "borders" + "]\n");

            for (Map.Entry<Integer, Set<Integer>> entry : d_mapEditorEngine.getCountryNeighbourMap().entrySet()) {
                int key = entry.getKey();
                Set<Integer> neighbour = entry.getValue();
                l_writer.write(key + " ");
                for (Integer a : neighbour) {
                    l_writer.write(a + " ");
                }
                l_writer.write("\n");
            }
            // Re-initialise map editor engine data
            d_mapEditorEngine.initialise();

            // Logging
            String l_fileName = p_fileObject.getName();
            int l_index = l_fileName.lastIndexOf('\\');
            String l_loggingMessage = l_fileName.substring(l_index + 1) + " saved successfully!\n";
            d_logEntryBuffer.dataChanged("savemap", l_loggingMessage);

            return "File saved successfully";
        } catch (IOException p_ioException) {
            throw new InvalidInputException("Error while saving the file!");
        }
    }

    /**
     * Takes the path of the file which user wants to save(edited file).
     *
     * @param p_commandValues Value of parameters entered by the user.
     * @return Value of string acknowledging user that the file is saved or not.
     * @throws VMException Throws if error occurs in VM Engine operation.
     */
    @Override
    public String execute(List<String> p_commandValues) throws
            VMException {
        if (p_commandValues.get(1).equals("conquest")) {
            // Validates the map before saving the file.
            ValidateMapService l_validateObj = new ValidateMapService();
            l_validateObj.execute(null);
        }

        // Validates the file, gets the file object, and writes the data into it.
        return saveToFile(FileUtil.retrieveMapFile(PathResolverUtil.resolveFilePath(p_commandValues.get(0))));
    }
}
