package com.warzone.team08.VM.map_editor;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.constants.interfaces.Engine;
import com.warzone.team08.VM.constants.interfaces.JSONable;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidGameException;
import com.warzone.team08.VM.repositories.CountryRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Manages player runtime data, such as: The continents, countries, and the neighbors.
 *
 * @author Brijesh Lakkad
 * @author CHARIT
 * @version 1.0
 */
public class MapEditorEngine implements Engine, JSONable {
    private List<Continent> d_continentList;
    private HashMap<String, String> d_MapDetails;
    private boolean d_isLoadingMap = false;

    /**
     * Instance can not be created outside the class. (private)
     */
    public MapEditorEngine() {
        this.initialise();
    }

    /**
     * {@inheritDoc}
     */
    public void initialise() {
        d_continentList = new ArrayList<>();
        // resets serial information to start from zero again for the next iteration of loading the map.
        Continent.resetSerialNumber();
        Country.resetSerialNumber();
    }

    /**
     * Gets the list of continents.
     *
     * @return d_continentList List of continents.
     */
    public List<Continent> getContinentList() {
        return d_continentList;
    }

    /**
     * Sets the value of continent list.
     *
     * @param p_continentList list of continents.
     */
    public void setContinentList(List<Continent> p_continentList) {
        d_continentList = p_continentList;
    }

    /**
     * Gets the list of the countries.
     *
     * @return list of countries.
     */
    public ArrayList<Country> getCountryList() {
        ArrayList<Country> l_countries = new ArrayList<>();
        for (Continent l_continent : d_continentList) {
            for (Country l_country : l_continent.getCountryList()) {
                if (!l_countries.contains(l_country)) {
                    l_countries.add(l_country);
                }
            }
        }
        return l_countries;
    }

    /**
     * This method returns the map consisting country as a key and list of its neighboring countries as a value.
     *
     * @return Value of the map of country and its neighbors.
     */
    public Map<Integer, Set<Integer>> getCountryNeighbourMap() {
        Map<Integer, Set<Integer>> l_continentCountryMap = new HashMap<>();
        ArrayList<Country> l_countries = this.getCountryList();
        for (Country l_country : l_countries) {
            Set<Integer> l_neighborCountryIdList = new HashSet<>();
            for (Country l_neighborCountry : l_country.getNeighbourCountries()) {
                l_neighborCountryIdList.add(l_neighborCountry.getCountryId());
            }
            l_continentCountryMap.put(l_country.getCountryId(), l_neighborCountryIdList);
        }
        return l_continentCountryMap;
    }


    /**
     * This method returns the map consisting continent name as a key and list of country names available in that
     * continent as a value.
     *
     * @return map of continent and its member countries.
     * @throws EntityNotFoundException If requested entity not found.
     */
    public Map<String, List<String>> getContinentCountryMap() throws EntityNotFoundException {
        Map<String, List<String>> l_continentCountryMap = new HashMap<>();
        for (Continent l_continent : d_continentList) {
            if (!l_continent.getCountryList().isEmpty()) {
                for (Country l_country : l_continent.getCountryList()) {
                    String continentName = l_continent.getContinentName();
                    List<String> l_countryNames;
                    if (l_continentCountryMap.containsKey(continentName)) {
                        l_countryNames = l_continentCountryMap.get(continentName);
                    } else {
                        l_countryNames = new ArrayList<>();
                    }
                    l_countryNames.add(l_country.getCountryName());
                    l_continentCountryMap.put(continentName, l_countryNames);
                }
            } else {
                throw new EntityNotFoundException("Add minimum one country in a continent!");
            }
        }
        return l_continentCountryMap;
    }

    /**
     * Adds the element to the list of continents.
     *
     * @param p_continent Value of the element.
     */
    public void addContinent(Continent p_continent) {
        d_continentList.add(p_continent);
    }

    /**
     * Gets the state of the loading-map.
     *
     * @return True if the map is being loaded using the file.
     */
    public boolean getLoadingMap() {
        return d_isLoadingMap;
    }

    /**
     * Sets the state of the loading-map functionalities: loadmap and editmap. This will be true if any of the above
     * command is being used to load the map.
     *
     * @param p_loadingMap True if the map is being loaded using the file.
     */
    public void setLoadingMap(boolean p_loadingMap) {
        d_isLoadingMap = p_loadingMap;
    }


    /**
     * {@inheritDoc} Shuts the <code>MapEditorEngine</code>.
     */
    public void shutdown() {
        // No threads created by MapEditorEngine.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_mapEditorEngineJSON = new JSONObject();
        JSONArray l_continentJSONList = new JSONArray();
        for (Continent l_continent : getContinentList()) {
            l_continentJSONList.put(l_continent.toJSON());
        }
        l_mapEditorEngineJSON.put("continents", l_continentJSONList);

        // Save neighbor details.
        JSONObject l_neighborCountryJSON = new JSONObject();
        for (Country l_country : getCountryList()) {
            JSONArray l_countryNeighbors = new JSONArray();
            for (Country l_neighbourCountry : l_country.getNeighbourCountries()) {
                l_countryNeighbors.put(l_neighbourCountry.getCountryName());
            }
            l_neighborCountryJSON.put(l_country.getCountryName(), l_countryNeighbors);
        }
        l_mapEditorEngineJSON.put("neighborCountryMappings", l_neighborCountryJSON);
        return l_mapEditorEngineJSON;
    }

    /**
     * Creates an instance of this class and assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @param p_gameEngine Instance of target <code>GameEngine</code>.
     * @return Created instance of this class using the provided JSON data.
     * @throws InvalidGameException If the information from JSONObject cannot be used because it is corrupted or missing
     *                              the values.
     */
    public static MapEditorEngine fromJSON(JSONObject p_jsonObject, GameEngine p_gameEngine) throws InvalidGameException {
        CountryRepository l_countryRepository = new CountryRepository();

        MapEditorEngine l_mapEditorEngine = new MapEditorEngine();
        p_gameEngine.setMapEditorEngine(l_mapEditorEngine);

        JSONArray l_continentJSONList = p_jsonObject.getJSONArray("continents");
        for (int l_continentIndex = 0; l_continentIndex < l_continentJSONList.length(); l_continentIndex++) {
            JSONObject l_continentJSON = l_continentJSONList.getJSONObject(l_continentIndex);
            // Create new continent.
            Continent l_continent = Continent.fromJSON(l_continentJSON);
            l_mapEditorEngine.addContinent(l_continent);
        }

        // Assign neighbors.
        JSONObject l_neighborCountryJSON = p_jsonObject.getJSONObject("neighborCountryMappings");
        Set<String> l_countryList = l_neighborCountryJSON.keySet();
        for (String l_countryName : l_countryList) {
            try {
                Country l_targetCountry = l_countryRepository.findFirstByCountryName(l_countryName);
                JSONArray l_countryNeighbors = l_neighborCountryJSON.getJSONArray(l_countryName);
                for (int l_neighborCountryIndex = 0; l_neighborCountryIndex < l_countryNeighbors.length(); l_neighborCountryIndex++) {
                    String l_neighborCountryName = l_countryNeighbors.getString(l_neighborCountryIndex);
                    try {
                        Country l_targetNeighborCountry = l_countryRepository.findFirstByCountryName(l_neighborCountryName);
                        // Add the country to the list.
                        l_targetCountry.addNeighbourCountry(l_targetNeighborCountry);
                    } catch (EntityNotFoundException p_entityNotFoundException) {
                        throw new InvalidGameException(String.format("Neighbor country of %s with name %s not found!", l_countryName, l_neighborCountryName));
                    }
                }
            } catch (EntityNotFoundException p_entityNotFoundException) {
                throw new InvalidGameException(String.format("Country with name %s not found!", l_countryName));
            }
        }
        return l_mapEditorEngine;

    }

    /**
     * Stores the information about the map file.
     *
     * @return Hashmap storing map info.
     */
    public HashMap<String, String> getMapDetails() {
        return d_MapDetails;
    }

    /**
     * Returns the hashmap storing map info.
     *
     * @param p_MapDetails mMp info.
     */
    public void setMapDetails(HashMap<String, String> p_MapDetails) {
        d_MapDetails = p_MapDetails;
    }
}
