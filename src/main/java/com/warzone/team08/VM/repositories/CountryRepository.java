package com.warzone.team08.VM.repositories;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class finds the Country entity from the runtime engine.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class CountryRepository {
    /**
     * Finds the country using country name.
     *
     * @param p_countryName Value of the name of country.
     * @return Value of the list of matched countries.
     */
    public List<Country> findByCountryName(String p_countryName) {
        return VirtualMachine.getGameEngine().getMapEditorEngine().getCountryList().stream().filter(p_country ->
                p_country.getCountryName().equals(p_countryName)
        ).collect(Collectors.toList());
    }

    /**
     * Finds only one country using its name.
     *
     * @param p_countryName Value of the name of country.
     * @return Value of the first matched countries.
     * @throws EntityNotFoundException Throws if the being searched entity has been not found.
     */
    public Country findFirstByCountryName(String p_countryName) throws EntityNotFoundException {
        List<Country> l_countryList = this.findByCountryName(p_countryName);
        if (l_countryList.size() > 0)
            return l_countryList.get(0);

        throw new EntityNotFoundException(String.format("'%s' country not found", p_countryName));
    }

    /**
     * Finds the country using its id.
     *
     * @param p_countryId Value of the name of country.
     * @return Value of the first matched countries.
     */
    public Country findByCountryId(Integer p_countryId) {
        List<Country> l_countries = VirtualMachine.getGameEngine().getMapEditorEngine().getCountryList().stream().filter(p_country ->
                p_country.getCountryId().equals(p_countryId)
        ).collect(Collectors.toList());
        if (!l_countries.isEmpty()) {
            return l_countries.get(0);
        } else {
            return null;
        }
    }

    /**
     * Finds the countries whose neighbor is the parameter country.
     *
     * @param p_country Country to be checked for neighbour to other countries.
     * @return List of the countries.
     */
    public List<Country> findByNeighbourOfCountries(Country p_country) {
        return VirtualMachine.getGameEngine().getMapEditorEngine().getCountryList().stream().filter(p_l_country ->
                !p_l_country.equals(p_country) && p_l_country.getNeighbourCountries().contains(p_country)
        ).collect(Collectors.toList());
    }

    /**
     * Finds the neighboring country of the given country.
     *
     * @param p_country Country object
     * @return List of country object.
     * @throws IllegalStateException Throws if returns an empty list.
     */
    public List<Country> findCountryNeighborsAndNotOwned(Country p_country) throws IllegalStateException {
        return p_country.getNeighbourCountries().stream().filter((p_l_country) ->
                p_l_country.getOwnedBy() == null
        ).collect(Collectors.toList());
    }
}
