package com.warzone.team08.VM.map_editor.services;

import com.jakewharton.fliptables.FlipTable;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.ResourceNotFoundException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.repositories.ContinentRepository;
import com.warzone.team08.VM.repositories.CountryRepository;

import java.util.*;

/**
 * This class is used to show the content of current map on user console
 *
 * @author MILESH
 * @version 1.0
 */
public class ShowMapService implements SingleCommand {
    MapEditorEngine d_mapEditorEngine;
    ContinentRepository d_continentRepository;
    CountryRepository d_countryRepository;
    List<Continent> d_continentList;
    List<Country> d_countryList;
    Map<String, List<String>> d_continentCountryMap;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Initializes different objects.
     *
     * @throws EntityNotFoundException Throws if required entity is not found.
     */
    public ShowMapService() throws EntityNotFoundException {
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        d_continentList = d_mapEditorEngine.getContinentList();
        d_countryList = d_mapEditorEngine.getCountryList();
        d_continentCountryMap = d_mapEditorEngine.getContinentCountryMap();
        d_continentRepository = new ContinentRepository();
        d_countryRepository = new CountryRepository();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * This method is used to display all the continents with its bonusValue and countries present in that continent in
     * Tabular structure
     *
     * @return String of all continents information
     */
    public String showContinentCountryContent() {

        //table header
        String[] l_header = {"Continent Name", "Control Value", "Countries"};
        List<List<String>> l_mapContent = new ArrayList<>();

        for (Map.Entry<String, List<String>> l_entry : d_continentCountryMap.entrySet()) {
            ArrayList<String> l_continentsList = new ArrayList<>();
            l_continentsList.add(l_entry.getKey());
            try {
                //fetching continent object using its name
                Continent l_continent = d_continentRepository.findFirstByContinentName(l_entry.getKey());
                l_continentsList.add(String.valueOf(l_continent.getContinentControlValue()));

                //for sorting the countries of continent
                List<String> l_sortedCountryList = new ArrayList<>(l_entry.getValue());
                Collections.sort(l_sortedCountryList);
                String l_continentCountries = String.join(",", l_sortedCountryList);
                l_continentsList.add(l_continentCountries);
                l_mapContent.add(l_continentsList);

            } catch (EntityNotFoundException p_e) {
                p_e.printStackTrace();
            }
        }

        // store continent data in 2d array
        String[][] l_continentMapMatrix = new String[l_mapContent.size()][];
        for (int i = 0; i < l_mapContent.size(); i++) {
            List<String> l_singleContinentContent = l_mapContent.get(i);
            l_continentMapMatrix[i] = l_singleContinentContent.toArray(new String[l_singleContinentContent.size()]);
        }

        return FlipTable.of(l_header, l_continentMapMatrix);
    }

    /**
     * This method is used to display the adjacency of all countries
     *
     * @return String of country's neighbour information
     */
    public String showNeighbourCountries() {
        LinkedList<String> l_countryNames = new LinkedList<>();
        String[][] l_neighbourCountryMatrix = new String[d_countryList.size() + 1][d_countryList.size() + 1];

        //for adding all country names
        for (Country l_country : d_countryList) {
            l_countryNames.add(l_country.getCountryName());
        }
        l_neighbourCountryMatrix[0][0] = "COUNTRIES";
        //Collections.sort(l_countryNames);

        //for storing country names in first row and column of matrix
        for (int l_row = 1; l_row < l_neighbourCountryMatrix.length; l_row++) {
            String l_name = l_countryNames.pollFirst();
            l_neighbourCountryMatrix[l_row][0] = l_name;
            l_neighbourCountryMatrix[0][l_row] = l_name;
        }

        //for storing neighbours of each country
        for (int l_row = 1; l_row < l_neighbourCountryMatrix.length; l_row++) {
            Country l_countryRow;
            try {
                l_countryRow = d_countryRepository.findFirstByCountryName(l_neighbourCountryMatrix[l_row][0]);
                List<Country> l_countryNeighbourList = l_countryRow.getNeighbourCountries();
                for (int l_col = 1; l_col < l_neighbourCountryMatrix.length; l_col++) {
                    Country l_countryColumn = d_countryRepository.findFirstByCountryName(l_neighbourCountryMatrix[0][l_col]);
                    if (l_countryRow.equals(l_countryColumn) || l_countryNeighbourList.contains(l_countryColumn)) {
                        l_neighbourCountryMatrix[l_row][l_col] = "X";
                    } else {
                        l_neighbourCountryMatrix[l_row][l_col] = "O";
                    }
                }
            } catch (EntityNotFoundException p_e) {
                p_e.printStackTrace();
            }
        }

        String[] l_countryCountHeader = new String[l_neighbourCountryMatrix.length];
        for (int i = 0; i < l_countryCountHeader.length; i++) {
            l_countryCountHeader[i] = "C" + i;
        }

        return FlipTable.of(l_countryCountHeader, l_neighbourCountryMatrix);
    }

    /**
     * Initiates all methods of ShowMapService file.
     *
     * @param p_commandValues Value of parameters entered by the user.
     * @return Value of string of continent and neighbour country information.
     * @throws EntityNotFoundException Throws if file not found.
     */
    @Override
    public String execute(List<String> p_commandValues) throws EntityNotFoundException, ResourceNotFoundException, InvalidInputException {
        String l_logResponse = "";
        if (!this.d_continentCountryMap.isEmpty() || !this.d_countryList.isEmpty()) {
            l_logResponse = this.showContinentCountryContent() + "\n" + this.showNeighbourCountries();
            // Logging
            d_logEntryBuffer.dataChanged("showmap", l_logResponse);
            return l_logResponse;
        } else {
            throw new EntityNotFoundException("Please select file to show!");
        }
    }
}
