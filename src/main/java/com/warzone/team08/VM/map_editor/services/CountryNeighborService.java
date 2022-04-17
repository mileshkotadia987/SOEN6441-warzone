package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.repositories.CountryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This service handles `editneighbor` user command to set/remove neighbors of the different countries on the map.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class CountryNeighborService {
    /**
     * Engine to store and retrieve map data.
     */
    private final MapEditorEngine d_mapEditorEngine;
    private final CountryRepository d_countryRepository;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Initializes different objects.
     */
    public CountryNeighborService() {
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();;
        d_countryRepository = new CountryRepository();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Attach the neighbor to the country using the name of the countries.
     *
     * @param p_countryName         Value of the country name.
     * @param p_neighborCountryName Value of the neighbour country to be set.
     * @return Value of response of the request.
     * @throws EntityNotFoundException Throws if the either country not found.
     */
    public String add(String p_countryName, String p_neighborCountryName) throws EntityNotFoundException {
        Country l_country = d_countryRepository.findFirstByCountryName(p_countryName);
        Country l_neighborCountry = d_countryRepository.findFirstByCountryName(p_neighborCountryName);
        if (!d_mapEditorEngine.getLoadingMap()) {
            d_logEntryBuffer.dataChanged("editneighbor", p_neighborCountryName + " is set as neighbor of " + p_countryName);
        }
        return this.add(l_country, l_neighborCountry);
    }

    /**
     * Attach the neighbor to the country using the entity.
     *
     * @param p_country         Value of the country entity which will have the neighbor country.
     * @param p_neighborCountry Value of the neighbour country entity.
     * @return Value of response of the request.
     */
    public String add(Country p_country, Country p_neighborCountry) {
        p_country.addNeighbourCountry(p_neighborCountry);
        return String.format("Neighbor %s country added for %s!", p_neighborCountry.getCountryName(), p_country.getCountryName());
    }

    /**
     * Removes the neighbor for the country using the names.
     *
     * @param p_countryName         Value of the country name.
     * @param p_neighborCountryName Value of the neighbor country.
     * @return Value of response of the request.
     * @throws EntityNotFoundException Throws if the either country not found.
     */
    public String remove(String p_countryName, String p_neighborCountryName) throws EntityNotFoundException {
        Country l_country = d_countryRepository.findFirstByCountryName(p_countryName);
        Country l_neighborCountry = d_countryRepository.findFirstByCountryName(p_neighborCountryName);

        if (!d_mapEditorEngine.getLoadingMap()) {
            d_logEntryBuffer.dataChanged("editneighbor", p_neighborCountryName + " is removed as a neighbor of " + p_countryName);
        }
        return this.remove(l_country, l_neighborCountry);
    }

    /**
     * Removes the neighbor for the country using the actual entities.
     *
     * @param p_country         Value of the country entity.
     * @param p_neighborCountry Value of the neighbor country entity.
     * @return Value of response of the request.
     */
    public String remove(Country p_country, Country p_neighborCountry) {
        List<Country> l_filteredCountry = p_country.getNeighbourCountries().stream().filter(i_p_country ->
                i_p_country.equals(p_neighborCountry)
        ).collect(Collectors.toList());

        p_country.setNeighbourCountries(l_filteredCountry);
        return String.format("Neighbor %s country removed from %s!", p_neighborCountry.getCountryName(), p_country.getCountryName());
    }
}
