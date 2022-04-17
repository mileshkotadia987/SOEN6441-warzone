package com.warzone.team08.VM.game_play.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.interfaces.SingleCommand;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.repositories.CountryRepository;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floor;

/**
 * This class handles the distribution of countries among all the players.
 *
 * @author CHARIT
 */
public class DistributeCountriesService implements SingleCommand {
    private List<Country> d_countryList;
    /**
     * Country repository to find the country(s) using the filters.
     */
    private CountryRepository d_countryRepository = new CountryRepository();

    private final GamePlayEngine d_gamePlayEngine;

    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructor for instantiating required objects.
     */
    public DistributeCountriesService() {
        d_countryList = VirtualMachine.getGameEngine().getMapEditorEngine().getCountryList();
        d_gamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Method to assign countries to different players.
     *
     * @return Value of response of the request.
     * @throws InvalidInputException Throws if number of players are zero.
     */
    public String distributeCountries() throws InvalidInputException {
        int l_countryCount = d_countryList.size();
        int l_playerCount = d_gamePlayEngine.getPlayerList().size();
        try {
            int l_floorVal = (int) floor(l_countryCount / l_playerCount);
            int l_remainder = l_countryCount % l_playerCount;

            for (Player l_playerObj : d_gamePlayEngine.getPlayerList()) {
                if (l_remainder > 0) {
                    l_playerObj.setAssignedCountryCount(l_floorVal + 1);
                    l_remainder--;
                } else {
                    l_playerObj.setAssignedCountryCount(l_floorVal);
                }
            }
            for (Player l_player : d_gamePlayEngine.getPlayerList()) {
                int l_playerCountryCount = l_player.getAssignedCountryCount();
                List<Country> l_assignedCountryList = assignCountry(l_player, l_playerCountryCount);
                l_player.setAssignedCountries(l_assignedCountryList);
            }
            return "Countries are successfully assigned!";
        } catch (ArithmeticException e) {
            throw new InvalidInputException("Number of players are zero");
        }
    }

    /**
     * Returns the list of countries to be assigned to player.
     *
     * @param p_player             Player object.
     * @param p_playerCountryCount Number of countries can be assigned to player.
     * @return Value of list of countries.
     */
    public List<Country> assignCountry(Player p_player, int p_playerCountryCount) {
        List<Country> l_assignedCountries = new ArrayList<>();
        List<Country> l_countryLst;
        List<Country> l_groupOfCountries;
        int l_playerCountryCount = p_playerCountryCount;

        int l_size;
        int l_iterateCountryCount = 0;
        do {
            Country selectedCountry = d_countryList.get(l_iterateCountryCount);
            if (selectedCountry.getOwnedBy() == null) {
                selectedCountry.setOwnedBy(p_player);
                l_groupOfCountries = d_countryRepository.findCountryNeighborsAndNotOwned(selectedCountry);
                l_groupOfCountries.add(0, selectedCountry);

                l_size = l_groupOfCountries.size();
                if (l_size < p_playerCountryCount) {
                    p_playerCountryCount -= l_size;
                    assignOwnerToCountry(p_player, l_groupOfCountries);
                    l_assignedCountries.addAll(l_groupOfCountries);
                } else {
                    l_countryLst = l_groupOfCountries.subList(0, p_playerCountryCount);
                    assignOwnerToCountry(p_player, l_countryLst);
                    l_assignedCountries.addAll(l_countryLst);
                }
            }
            l_iterateCountryCount++;
            if (l_iterateCountryCount >= d_countryList.size()) {
                break;
            }
        } while (l_assignedCountries.size() < l_playerCountryCount);
        return l_assignedCountries;
    }

    /**
     * Assigns owner to different countries.
     *
     * @param p_player      Player object
     * @param p_countryList List of countries
     */
    public void assignOwnerToCountry(Player p_player, List<Country> p_countryList) {
        for (Country l_con : p_countryList) {
            l_con.setOwnedBy(p_player);
        }
    }

    /**
     * Calls the distributeCountries() method of the class and returns the result.
     *
     * @param p_commandValues Represents the values passed while running the command.
     * @return Success message if function runs without error, otherwise throws exception.
     * @throws InvalidInputException Throws if number of players are zero.
     * @throws IllegalStateException Throws if returns an empty list.
     * @throws VMException           If any exception from while players in <code>GameLoop</code>.
     */
    @Override
    public String execute(List<String> p_commandValues) throws VMException, IllegalStateException {
        // Check if players have been added.
        // What if only one player is available?
        if (!d_gamePlayEngine.getPlayerList().isEmpty()) {
            String l_response = distributeCountries();
            // Logging
            d_logEntryBuffer.dataChanged("assigncountries", l_response + "\n" + this.getPlayerCountries());
            return l_response;
        } else {
            throw new EntityNotFoundException("Please, add players to show game status!");
        }
    }

    /**
     * This method return the String of countries associated with each player.
     *
     * @return string of player's countries.
     */
    public String getPlayerCountries() {
        String l_playerContent = "";
        for (Player l_player : d_gamePlayEngine.getPlayerList()) {
            List<Country> l_countries = l_player.getAssignedCountries();
            List<String> l_names = new ArrayList<>();
            for (Country l_country : l_countries) {
                l_names.add(l_country.getCountryName());
            }
            String l_countriesNames = String.join(",", l_names);
            l_playerContent += l_player.getName() + ": " + l_names + "\n";
        }
        return l_playerContent;
    }
}
