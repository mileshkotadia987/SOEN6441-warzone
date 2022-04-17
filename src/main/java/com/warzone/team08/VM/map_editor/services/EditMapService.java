package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.FileType;
import com.warzone.team08.VM.constants.enums.MapModelType;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * This file loads map file in the user console. This file acts as a Target class in adapter pattern.
 * <p>
 * This service handles `editmap` user command.
 *
 * @author Brijesh Lakkad
 * @author CHARIT
 */
public class EditMapService implements SingleCommand {
    /**
     * Engine to store and retrieve map data.
     */
    private final MapEditorEngine d_mapEditorEngine;

    private final ContinentRepository d_continentRepository;
    private final CountryRepository d_countryRepository;
    private final ContinentService d_continentService;
    private final CountryService d_countryService;
    private final CountryNeighborService d_countryNeighborService;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Initializes variables required to load map into different objects.
     */
    public EditMapService() {
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        d_continentRepository = new ContinentRepository();
        d_countryRepository = new CountryRepository();
        d_continentService = new ContinentService();
        d_countryService = new CountryService();
        d_countryNeighborService = new CountryNeighborService();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * This method reads user provided map file. It reads data from file and stores into different java objects.
     *
     * @param p_filePath      Path of the file to be read.
     * @param shouldCreateNew True if this method should create a new file if it doesn't exists.
     * @return Value of the response.
     * @throws InvalidMapException       Throws if file does not have valid map data.
     * @throws AbsentTagException        Throws if there is missing tag.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if type cast was not successful.
     * @throws EntityNotFoundException   Throws if the referred entity is not found.
     */
    public String handleLoadMap(String p_filePath, boolean shouldCreateNew)
            throws InvalidMapException,
            AbsentTagException,
            ResourceNotFoundException,
            InvalidInputException,
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
                        // Parsing the [continents] portion of the map file
                        if (this.doLineHasModelData(l_currentLine, MapModelType.CONTINENT)) {
                            readContinents(l_reader);
                        } else if (this.doLineHasModelData(l_currentLine, MapModelType.COUNTRY)) {
                            // Parsing the [countries] portion of the map file
                            readCountries(l_reader);
                        } else if (this.doLineHasModelData(l_currentLine, MapModelType.BORDER)) {
                            // Parsing the [borders] portion of the map file
                            readNeighbours(l_reader);
                        }
                    }
                }
                return "File loaded successfully!";
            } catch (IOException p_ioException) {
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
     * The overloading method of {@link EditMapService#handleLoadMap(String, boolean)} This overloading method calls the
     * overloaded method with a variable indicating that create a new file if it doesn't exists.
     *
     * @param p_filePath Path of the file to be read.
     * @return Value of the response.
     * @throws InvalidMapException       Throws if file does not have valid map data.
     * @throws AbsentTagException        Throws if there is missing tag.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if type cast was not successful.
     * @throws EntityNotFoundException   Throws if the referred entity is not found.
     */
    public String handleLoadMap(String p_filePath) throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException {
        return this.handleLoadMap(p_filePath, true);
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
        } catch (IOException p_ioException) {
            throw new InvalidMapException("Error while processing!");
        }
    }

    /**
     * This method is used to read country data from map file. It reads the country serial number, country name,
     * corresponding continent serial number and stores those values in Country class object using Country class
     * methods. This object is later stored in the list.
     *
     * @param p_reader object of BufferedReader
     * @throws EntityNotFoundException Throws if the continent of the country not found.
     * @throws InvalidMapException     Throws if error while reading file.
     * @throws AbsentTagException      Throws if the required element in line is not available.
     */
    private void readCountries(BufferedReader p_reader) throws EntityNotFoundException, InvalidMapException, AbsentTagException {
        String l_currentLine;
        try {
            while ((l_currentLine = p_reader.readLine()) != null && !l_currentLine.startsWith("[")) {
                if (l_currentLine.trim().isEmpty()) {
                    // If line is empty string.
                    continue;
                }
                List<String> l_countryComponentList = this.getModelComponents(l_currentLine);
                if (l_countryComponentList.size() >= 3) {
                    try {
                        d_countryService.add(Integer.parseInt(l_countryComponentList.get(0)),
                                l_countryComponentList.get(1),
                                Integer.parseInt(l_countryComponentList.get(2)),
                                l_countryComponentList.get(3),
                                l_countryComponentList.get(4));
                    } catch (IndexOutOfBoundsException p_e) {
                        d_countryService.add(Integer.parseInt(l_countryComponentList.get(0)),
                                l_countryComponentList.get(1),
                                Integer.parseInt(l_countryComponentList.get(2)));
                    }
                } else {
                    throw new AbsentTagException("Missing country value!");
                }
                p_reader.mark(0);
            }
            p_reader.reset();
        } catch (IOException p_ioException) {
            throw new InvalidMapException("Error while processing!");
        }

    }

    /**
     * This method is used to read country and its neighbor data from map file. It reads the line from the file and
     * creates the list of the neighboring countries. And it adds country and list of its neighboring countries into a
     * TreeMap.
     *
     * @param p_reader object of BufferedReader
     * @throws InvalidMapException Throws if error while reading file.
     * @throws AbsentTagException  Throws if the required element in line is not available.
     */
    private void readNeighbours(BufferedReader p_reader) throws AbsentTagException, InvalidMapException {
        String l_currentLine;
        try {
            while ((l_currentLine = p_reader.readLine()) != null && !l_currentLine.startsWith("[")) {
                if (l_currentLine.trim().isEmpty()) {
                    // If line is empty string.
                    continue;
                }
                List<String> l_borderComponentList = this.getModelComponents(l_currentLine);
                if (l_borderComponentList.size() > 1) {
                    Country l_country = d_countryRepository.findByCountryId(Integer.parseInt(l_borderComponentList.get(0)));
                    if (l_country != null) {
                        for (int i = 1; i < l_borderComponentList.size(); i++) {
                            Country l_neighbourCountry = d_countryRepository.findByCountryId(Integer.parseInt(l_borderComponentList.get(i)));
                            if (l_neighbourCountry != null) {
                                d_countryNeighborService.add(l_country, l_neighbourCountry);
                            }
                        }
                    }
                } else {
                    throw new AbsentTagException("Missing border value!");
                }
            }
            p_reader.mark(0);
        } catch (IOException e) {
            throw new InvalidMapException("Error while processing!");
        }
    }

    /**
     * Checks if the line is the starting point of model data.
     *
     * <pre>
     * Model data can be of the below type:
     * 1. Continents
     * 2. Countries
     * 3. Borders
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
     * Extracts the model components from the line.
     *
     * @param p_line Line to be interpreted.
     * @return Value of the list of components.
     */
    public List<String> getModelComponents(String p_line) {
        try {
            if (!p_line.isEmpty() && p_line.contains(" ")) {
                List<String> l_continentComponentList = Arrays.asList(p_line.split("\\s"));
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
     * Takes the command and executes the function.
     *
     * @param p_commandValues Represents the values passed while running the command.
     * @throws InvalidMapException       Throws if the map was not valid.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if the user command is invalid.
     * @throws AbsentTagException        Throws if any tag is missing in map file.
     * @throws EntityNotFoundException   Throws if entity is missing.
     * @see EditMapService#handleLoadMap
     */
    @Override
    public String execute(List<String> p_commandValues)
            throws VMException {
        String l_response = "";
        if (!p_commandValues.isEmpty()) {
            // Resolve file path using absolute path of user data directory.
            String l_resolvedPathToFile = PathResolverUtil.resolveFilePath(p_commandValues.get(0));
            int l_index = l_resolvedPathToFile.lastIndexOf('\\');
            l_response = this.handleLoadMap(l_resolvedPathToFile);
            d_logEntryBuffer.dataChanged("editmap", l_resolvedPathToFile.substring(l_index + 1) + " " + l_response);
            d_mapEditorEngine.setLoadingMap(false);
            return l_response;
        } else {
            throw new InvalidInputException("File name is empty!");
        }
    }
}
