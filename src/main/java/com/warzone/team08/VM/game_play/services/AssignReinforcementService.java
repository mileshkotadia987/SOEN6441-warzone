package com.warzone.team08.VM.game_play.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.map_editor.MapEditorEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class will reinforce the army to respective players at each new turn.
 *
 * @author Rutwik
 */
public class AssignReinforcementService {
    /**
     * Singleton instance of <code>MapEditorEngine</code>.
     */
    public MapEditorEngine d_mapEditorEngine;

    /**
     * Singleton instance of <code>GamePlayEngine</code>.
     */
    public GamePlayEngine d_gamePlayEngine;

    /**
     * Map representing continent and its member countries.
     */
    public Map<String, List<String>> d_continentCountryList;

    /**
     * This Method will set reinforcement army to each player. It will also check whether a player completely owns a
     * continent or not. If yes then it will add Continent's control value to the reinforcement army as a part of
     * bonus.
     */
    public AssignReinforcementService() {
        d_mapEditorEngine = VirtualMachine.getGameEngine().getMapEditorEngine();
        d_gamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();
    }

    /**
     * This Method calculate the exact amount of army to be reinforced.
     *
     * @param p_player         Player's object.
     * @param p_continentValue It is the control value that has been added if a player owns a whole continent.
     * @return Method will return the army to be reinforced to the player.
     */
    private int addReinforcementArmy(Player p_player, int p_continentValue) {
        int l_AssignedCountryCount = p_player.getAssignedCountries().size();
        int l_reinforcementArmy = Math.max(3, (int) Math.ceil(l_AssignedCountryCount / 3));

        l_reinforcementArmy = l_reinforcementArmy + p_continentValue;
        return l_reinforcementArmy;
    }

    /**
     * This method will check whether a player owns a whole continent or not. If a player owns then control value of
     * respective continent is returned otherwise zero will be returned.
     *
     * @param p_playerList  Player's Object.
     * @param p_countryList List of Country of specific continent.
     * @param p_continent   Continent whose country is selected.
     * @return Method will return Continent's Control value if player owns whole continent otherwise return zero.
     */
    private int checkPlayerOwnsContinent(Player p_playerList, List<String> p_countryList, Continent p_continent) {
        List<String> l_country = new ArrayList<>();
        for (Country l_country1 : p_playerList.getAssignedCountries()) {
            l_country.add(l_country1.getCountryName());
        }
        boolean l_checkCountry = l_country.containsAll(p_countryList);
        if (l_checkCountry) {
            return p_continent.getContinentControlValue();
        }
        return 0;
    }

    /**
     * Assigns each player the correct number of reinforcement armies according to the Warzone rules.
     *
     * @throws EntityNotFoundException Throws if player is not available.
     */
    public void execute() throws EntityNotFoundException {
        d_continentCountryList = d_mapEditorEngine.getContinentCountryMap();

        for (Player l_player : d_gamePlayEngine.getPlayerList()) {
            int l_continentValue = 0;
            for (Continent l_continent : d_mapEditorEngine.getContinentList()) {

                List<String> l_countryList = new ArrayList<>(d_continentCountryList.get(l_continent.getContinentName()));
                // Method Call: Here Control Value is assessed.
                int l_returnContinentValue = checkPlayerOwnsContinent(l_player, l_countryList, l_continent);

                l_continentValue = l_continentValue + l_returnContinentValue;
            }
            // Method Call: This will add reinforcement Army to the player at each turn.
            int l_returnReinforcementArmy = addReinforcementArmy(l_player, l_continentValue);
            l_player.setReinforcementCount(l_returnReinforcementArmy);
        }
    }
}
