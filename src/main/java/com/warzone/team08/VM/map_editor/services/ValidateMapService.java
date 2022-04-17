package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidMapException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.repositories.CountryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class contains methods for the validation of the map and handles `validatemap` user command.
 *
 * @author Deep Patel
 * @author Brijesh Lakkad
 */
public class ValidateMapService implements SingleCommand {
    /**
     * Engine to store and retrieve map data.
     */
    private final MapEditorEngine d_mapEditorEngine;

    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Default constructor to retrieve the singleton instance of <code>MapEditorEngine</code> and
     * <code>LogEntryBuffer</code>.
     */
    public ValidateMapService() {
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Checks that continent is a connected subgraph.
     *
     * @return True if validation passes.
     * @throws EntityNotFoundException Throws if continent not found.
     */
    public boolean isContinentConnectedSubgraph() throws EntityNotFoundException {
        if (d_mapEditorEngine.getContinentList().size() > 1) {
            boolean l_isInvalid = false;
            String l_continentName;
            List<String> l_countriesIntoContinent;
            CountryRepository l_countryRepository = new CountryRepository();
            int l_totalContinent = d_mapEditorEngine.getContinentList().size();
            int l_compareTotalContinent = 0;
            Country l_foundCountry = null;

            Map<String, List<String>> l_continentCountryMap = d_mapEditorEngine.getContinentCountryMap();
            for (Map.Entry<String, List<String>> entry : l_continentCountryMap.entrySet()) {
                //set countries for each Continent
                l_continentName = entry.getKey();
                l_countriesIntoContinent = entry.getValue();
                int l_otherContinentNeighbour = 0;

                //Checks that at least 1 neighbour from other continent
                for (String l_countryNameCompare : l_countriesIntoContinent) {
                    try {
                        l_foundCountry = l_countryRepository.findFirstByCountryName(l_countryNameCompare);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (l_foundCountry == null) {
                        continue;
                    }
                    List<Country> l_neighbourCountries = l_foundCountry.getNeighbourCountries();

                    for (Country l_country : l_neighbourCountries) {
                        Continent l_continent = l_country.getContinent();
                        String ContinentName = l_continent.getContinentName();
                        if (!(ContinentName.equals(l_continentName))) {
                            l_otherContinentNeighbour++;
                            break;
                        }
                    }

                    if (l_otherContinentNeighbour > 0) {
                        l_compareTotalContinent++;
                        break;
                    }
                }
            }
            //checks that total continent value is same as test passes or not.
            if (l_compareTotalContinent == l_totalContinent) {
                l_isInvalid = true;
            }
            return l_isInvalid;
        } else {
            return true;
        }
    }

    /**
     * Checks that map is a connected graph.
     *
     * @return True if validation pass.
     */
    public boolean isMapConnectedGraph() {
        boolean l_isValid = false;
        int l_connectedGraphCount = 0;

        List<Country> l_countryList = d_mapEditorEngine.getCountryList();
        for (int i = 0; i < l_countryList.size(); i++) {
            List<Country> l_visitedCountry = new ArrayList<>();
            Stack<Country> l_stack = new Stack<>();
            Country l_country = l_countryList.get(i);
            l_stack.push(l_country);

            //visiting country using the DFS logic
            while (!l_stack.isEmpty()) {
                Country l_countryGet = l_stack.pop();
                l_visitedCountry.add(l_countryGet);
                List<Country> l_neighbourCountries = l_countryGet.getNeighbourCountries();
                for (Country l_pushCountry : l_neighbourCountries) {
                    if (!l_stack.contains(l_pushCountry)) {
                        int l_counter = 0;
                        for (Country l_compareCountry : l_visitedCountry) {
                            if (l_pushCountry.equals(l_compareCountry)) {
                                l_counter++;
                            }
                        }
                        if (l_counter == 0) {
                            l_stack.push(l_pushCountry);
                        }
                    }
                }
            }
            //Check that CountryList and VisitedCountryList are same or not
            int compareCounter = 0;
            for (Country l_compareCountry : l_countryList) {
                for (Country l_compare2 : l_visitedCountry) {
                    if (l_compare2.equals(l_compareCountry)) {
                        compareCounter++;
                    }
                }
            }
            if (compareCounter == l_countryList.size()) {
                ++l_connectedGraphCount;
            }
        }
        if (l_connectedGraphCount == l_countryList.size()) {
            l_isValid = true;
        }
        return l_isValid;
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
                        //check every country is reachable
                        if (isContinentConnectedSubgraph()) {
                            //Check that continent is a connected sub-graph
                            if (isMapConnectedGraph()) {
                                d_logEntryBuffer.dataChanged("validatemap", "Map validation passed successfully!");
                                return "Map validation passed successfully!";
                            } else {
                                d_logEntryBuffer.dataChanged("validatemap", "map must be a connected graph!");
                                throw new InvalidMapException("map must be a connected graph!");
                            }
                        } else {
                            d_logEntryBuffer.dataChanged("validatemap", "Continent must be a connected sub-graph!");
                            throw new InvalidMapException("Continent must be a connected sub-graph!");
                        }
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