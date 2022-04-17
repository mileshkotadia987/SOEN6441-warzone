package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.FileType;
import com.warzone.team08.VM.constants.enums.MapModelType;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.repositories.ContinentRepository;
import com.warzone.team08.VM.repositories.CountryRepository;
import com.warzone.team08.VM.utils.FileUtil;
import com.warzone.team08.VM.utils.PathResolverUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class act as an Adaptee. It loads the conquest map into different java objects.
 *
 * @author CHARIT
 */
public class EditConquestMapService implements SingleCommand {

    private final HashMap<String, String> d_MapDetails;
    /**
     * Engine to store and retrieve map data.
     */
    private final MapEditorEngine d_mapEditorEngine;
    private final CountryRepository d_countryRepository;
    private final ContinentRepository d_continentRepository;
    private final ContinentService d_continentService;
    private final CountryService d_countryService;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Initializes variables required to load map into different objects.
     */
    public EditConquestMapService() {
        d_MapDetails = new HashMap<>();
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        d_countryRepository = new CountryRepository();
        d_continentRepository = new ContinentRepository();
        d_continentService = new ContinentService();
        d_countryService = new CountryService();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * This method reads user provided map file. It reads data from file and stores into different java objects.
     *
     * @param p_filePath      Path of the file to be read.
     * @param shouldCreateNew True if this method should create a new file if it doesn't exists.
     * @return Value of the response.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if type cast was not successful.
     * @throws InvalidMapException       Throws if file does not have valid map data.
     * @throws AbsentTagException        Throws if there is missing tag.
     * @throws EntityNotFoundException   Throws if the referred entity is not found.
     */
    public String loadConquestMap(String p_filePath, boolean shouldCreateNew)
            throws ResourceNotFoundException,
            InvalidInputException,
            InvalidMapException,
            AbsentTagException,
            EntityNotFoundException {
        // (Re) initialise engine.
        d_mapEditorEngine.initialise();
        d_mapEditorEngine.setLoadingMap(true);
        if (new File(p_filePath).exists()) {
            try {
                // Try to retrieve the file
                FileUtil.retrieveMapFile(p_filePath);
                // Will throw exception if the file path is not valid
                BufferedReader l_reader = new BufferedReader(new FileReader(p_filePath));

                // The value of the current line
                String l_currentLine;
                while ((l_currentLine = l_reader.readLine()) != null) {
                    if (l_currentLine.startsWith("[")) {
                        // Parsing the [Map] portion of the map file
                        if (this.doLineHasModelData(l_currentLine, MapModelType.MAP)) {
                            readMapDetails(l_reader);
                        }
                        // Parsing the [continents] portion of the map file
                        else if (this.doLineHasModelData(l_currentLine, MapModelType.CONTINENT)) {
                            readContinents(l_reader);
                        }
                        // Parsing the [Territories] portion of the map file
                        else if (this.doLineHasModelData(l_currentLine, MapModelType.TERRITORY)) {
                            readTerritories(l_reader);
                        }
                    }
                }
                return "File (Conquest map) successfully loaded";
            } catch (IOException e) {
                throw new ResourceNotFoundException("File not found!");
            }
        } else if (shouldCreateNew) {
            // Throws exception if file doesn't have required extension.
            FileUtil.checksIfFileHasRequiredExtension(p_filePath, FileType.MAP);

            FileUtil.createFileIfNotExists(p_filePath);
            return "New file created!";
        } else {
            throw new InvalidMapException("Please check if file exists. This may happen due to error while processing.");
        }
    }

    /**
     * This method is used to read the map details stored in [Map] tag. It stores this details in the HashMap.
     *
     * @param p_reader Object of BufferedReader
     * @throws InvalidMapException Throws if error while reading file.
     */
    private void readMapDetails(BufferedReader p_reader) throws InvalidMapException {
        String l_currentLine;
        try {
            while ((l_currentLine = p_reader.readLine()) != null && !l_currentLine.startsWith("[")) {
                if (!l_currentLine.isEmpty()) {
                    String[] maps_entry = l_currentLine.split("=");
                    d_MapDetails.put(maps_entry[0], maps_entry[1]);
                    p_reader.mark(0);
                }
            }
            p_reader.reset();
            d_mapEditorEngine.setMapDetails(d_MapDetails);
        } catch (IOException e) {
            throw new InvalidMapException("Invalid map file");
        }
    }

    /**
     * This method is used to read continent data from map file. It reads the continent name, control value, and color
     * and stores those values in Continent class object using Continent class methods. This object is later stored in
     * the list.
     *
     * @param p_reader Object of BufferedReader
     * @throws InvalidInputException Throws if the continent control value is not integer.
     * @throws InvalidMapException   Throws if error while reading file.
     * @throws AbsentTagException    Throws if the required element in line is not available.
     */
    private void readContinents(BufferedReader p_reader) throws InvalidInputException, InvalidMapException, AbsentTagException {
        String l_currentLine;
        try {
            while ((l_currentLine = p_reader.readLine()) != null && !l_currentLine.startsWith("[")) {
                if (l_currentLine.trim().isEmpty()) {
                    // If line is empty string.
                    continue;
                }
                List<String> l_continentComponentList = this.getModelComponents(l_currentLine);
                if (l_continentComponentList.size() >= 2) {
                    d_continentService.add(l_continentComponentList.get(0), l_continentComponentList.get(1));
                } else {
                    throw new AbsentTagException("Missing continent value!");
                }
                p_reader.mark(0);
            }
            p_reader.reset();
        } catch (IOException e) {
            throw new InvalidMapException("Error while processing!");
        }
    }

    /**
     * This method is used to read country data from map file. It reads the country name, corresponding continent name
     * and stores those values in Country class object using Country class methods. This object is later stored in the
     * list. It also reads the name of the neighboring countries and stores them in a list and assign it as a list of
     * neighboring countries to the current country.
     *
     * @param p_reader Object of BufferedReader
     * @throws InvalidMapException     Throws if error while reading file.
     * @throws EntityNotFoundException Throws if the continent of the country not found.
     */
    public void readTerritories(BufferedReader p_reader) throws InvalidMapException, EntityNotFoundException {
        String l_territories;
        try {
            while ((l_territories = p_reader.readLine()) != null && !l_territories.startsWith("[")) {
                if (!l_territories.isEmpty()) {
                    String l_countryName, l_continentName;
                    String l_xCoordinate;
                    String l_yCoordinate;
                    List<Country> l_neighbourNodes = new ArrayList<>();
                    String[] l_terrProperties = l_territories.split(",");
                    l_countryName = l_terrProperties[0];
                    l_xCoordinate = l_terrProperties[1];
                    l_yCoordinate = l_terrProperties[2];
                    l_continentName = l_terrProperties[3];
                    Continent l_continent = d_continentRepository.findFirstByContinentName(l_continentName);
                    for (int i = 4; i <= l_terrProperties.length - 1; i++) {
                        String l_neighbourCountryName = l_terrProperties[i];
                        Country l_neighbourCountry;
                        try {
                            l_neighbourCountry = d_countryRepository.findFirstByCountryName(l_neighbourCountryName);
                            l_neighbourCountry.setContinent(l_continent);
                            l_neighbourNodes.add(l_neighbourCountry);
                        } catch (EntityNotFoundException e) {
                            l_neighbourCountry = new Country(l_neighbourCountryName);
                            l_neighbourCountry.setContinent(l_continent);
                            l_neighbourNodes.add(l_neighbourCountry);
                        }
                    }
                    d_countryService.add(l_countryName, l_continent, l_neighbourNodes, l_xCoordinate, l_yCoordinate);
                }
            }
        } catch (IOException e) {
            throw new InvalidMapException("Error while processing!");
        }
    }

    /**
     * Extracts the model components from the line.
     *
     * @param p_line Line to be interpreted.
     * @return Value of the list of components.
     */
    public List<String> getModelComponents(String p_line) {
        try {
            if (!p_line.isEmpty()) {
                List<String> l_continentComponentList = Arrays.asList(p_line.split("="));
                if (!l_continentComponentList.isEmpty()) {
                    l_continentComponentList = l_continentComponentList.stream().map(String::trim)
                            .collect(Collectors.toList());
                    if (!(l_continentComponentList.contains(null) || l_continentComponentList.contains(""))) {
                        return l_continentComponentList;
                    }
                }
            }
        } catch (Exception e) {
            // If error while parsing, ignore the exception and return empty array list.
        }
        return new ArrayList<>();
    }

    /**
     * The overloading method of {@link EditConquestMapService#loadConquestMap(String, boolean)} This overloading method
     * calls the overloaded method with a variable indicating that create a new file if it doesn't exists.
     *
     * @param p_filePath Path of the file to be read.
     * @return Value of the response.
     * @throws AbsentTagException        Throws if there is missing tag.
     * @throws InvalidMapException       Throws if file does not have valid map data.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if type cast was not successful.
     * @throws EntityNotFoundException   Throws if the referred entity is not found.
     */
    public String loadConquestMap(String p_filePath) throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException {
        return this.loadConquestMap(p_filePath, true);
    }

    /**
     * Checks if the line is the starting point of model data.
     *
     * <pre>
     * Model data can be of the below type:
     * 1. Map
     * 2. Continents
     * 3. Territories
     * </pre>
     *
     * <p>
     * These modal type data will be read from the input file.
     *
     * @param p_currentLine  Value of the current line to be read.
     * @param p_mapModelType Value of the model data type
     * @return True if the line represents the title of model to be read in the following lines; false otherwise.
     */
    private boolean doLineHasModelData(String p_currentLine, MapModelType p_mapModelType) {
        return p_currentLine.substring(p_currentLine.indexOf("[") + 1, p_currentLine.indexOf("]"))
                .equalsIgnoreCase(p_mapModelType.getJsonValue());
    }

    /**
     * Takes the command and executes the function.
     *
     * @param p_commandValues Represents the values passed while running the command.
     * @return Value of string acknowledging user that the file is loaded or not.
     * @throws VMException Throws if error occurs in VM Engine operation.
     */
    @Override
    public String execute(List<String> p_commandValues) throws VMException {
        String l_response = "";
        if (!p_commandValues.isEmpty()) {
            String l_resolvedPathToFile = PathResolverUtil.resolveFilePath(p_commandValues.get(0));
            int l_index = l_resolvedPathToFile.lastIndexOf('\\');
            l_response = this.loadConquestMap(l_resolvedPathToFile);
            d_logEntryBuffer.dataChanged("editmap", l_resolvedPathToFile.substring(l_index + 1) + " " + l_response);
            d_mapEditorEngine.setLoadingMap(false);
            return l_response;
        } else {
            throw new InvalidInputException("File name is empty!");
        }
    }
}
