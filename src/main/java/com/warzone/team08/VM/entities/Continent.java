package com.warzone.team08.VM.entities;

import com.warzone.team08.VM.constants.interfaces.JSONable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class provides different getter-setter methods to perform different operation on Continent entity.
 *
 * @author CHARIT
 * @author Brijesh Lakkad
 * @author Rutwik
 */
public class Continent implements JSONable {
    /**
     * Auto-generated ID of the continent.
     */
    private Integer d_continentId;
    private String d_continentName;
    private Integer d_continentControlValue;
    private ArrayList<Country> d_countryList;

    /**
     * Used to keep the track of unique IDs for the continent.
     */
    public static int d_SerialNumber = 0;

    /**
     * Assigns Continent id to the continent and creates the member countries list.
     */
    public Continent() {
        this.d_continentId = ++d_SerialNumber;
        d_countryList = new ArrayList<>();
    }

    /**
     * Get the id of the continent which is the index where the continent is located.
     *
     * @return Value of the continent ID.
     */
    public Integer getContinentId() {
        return d_continentId;
    }

    /**
     * Sets the value of continent name.
     *
     * @param p_continentName Name of the continent.
     */
    public void setContinentName(String p_continentName) {
        d_continentName = p_continentName;
    }

    /**
     * Gets continent name.
     *
     * @return continent name.
     */
    public String getContinentName() {
        return d_continentName;
    }

    /**
     * Sets the continent control value.
     *
     * @param p_continentControlValue Value of the continent control.
     */
    public void setContinentControlValue(int p_continentControlValue) {
        d_continentControlValue = p_continentControlValue;
    }

    /**
     * Gets the continent control value.
     *
     * @return Value of the continent control.
     */
    public int getContinentControlValue() {
        return d_continentControlValue;
    }

    /**
     * Gets the countries of this continent.
     *
     * @return Value of the list of countries
     */
    public ArrayList<Country> getCountryList() {
        return d_countryList;
    }

    /**
     * Sets the country list for this continent.
     *
     * @param p_countryList Value of the list.
     */
    public void setCountryList(ArrayList<Country> p_countryList) {
        d_countryList = p_countryList;
    }

    /**
     * Adds a single country to the list of countries belonging to this continent.
     *
     * @param p_country Value of the country to be added.
     */
    public void addCountry(Country p_country) {
        // Set will not have any duplicate elements.
        d_countryList.add(p_country);
    }

    /**
     * Removes country from the list.
     *
     * @param p_country Value of the country to be removed.
     */
    public void removeCountry(Country p_country) {
        // Set will not have any duplicate elements.
        d_countryList.remove(p_country);
    }

    /**
     * Resets the serial number to zero. Used when the map engine is being reset.
     */
    public static void resetSerialNumber() {
        d_SerialNumber = 0;
    }

    /**
     * Checks if both objects are the same using continent id of the object.
     *
     * @param l_p_o Value of the second element to be checked with.
     * @return True if the both are same.
     */
    @Override
    public boolean equals(Object l_p_o) {
        if (this == l_p_o) return true;
        if (l_p_o == null || getClass() != l_p_o.getClass()) return false;
        Continent l_that = (Continent) l_p_o;
        return d_continentId.equals(l_that.d_continentId);
    }

    /**
     * Returns the hash value of the continent.
     *
     * @return Hash value of the continent.
     */
    @Override
    public int hashCode() {
        return Objects.hash(d_continentId);
    }


    /**
     * Creates <code>JSONObject</code> using the runtime information stored in data members of this class.
     *
     * @return Created <code>JSONObject</code>.
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_continentJSON = new JSONObject();
        l_continentJSON.put("name", d_continentName);
        l_continentJSON.put("controlValue", d_continentControlValue);
        JSONArray l_countryJSONList = new JSONArray();
        for (Country l_country : getCountryList()) {
            l_countryJSONList.put(l_country.toJSON());
        }
        l_continentJSON.put("countries", l_countryJSONList);
        return l_continentJSON;
    }

    /**
     * Creates an instance of this class and assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @return Created instance of this class using the provided JSON data.
     */
    public static Continent fromJSON(JSONObject p_jsonObject) {
        Continent l_continent = new Continent();
        l_continent.setContinentName(p_jsonObject.getString("name"));
        l_continent.setContinentControlValue(p_jsonObject.getInt("controlValue"));

        // Create countries.
        JSONArray l_countries = p_jsonObject.getJSONArray("countries");
        for (int l_countryIndex = 0; l_countryIndex < l_countries.length(); l_countryIndex++) {
            JSONObject l_countryJSON = l_countries.getJSONObject(l_countryIndex);
            // Create new country instance.
            Country l_countryObject = Country.fromJSON(l_countryJSON);
            l_continent.addCountry(l_countryObject);
        }

        return l_continent;
    }
}
