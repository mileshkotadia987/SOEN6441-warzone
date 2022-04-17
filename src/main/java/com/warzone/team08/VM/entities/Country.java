package com.warzone.team08.VM.entities;

import com.warzone.team08.VM.constants.interfaces.JSONable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is to set and get the country variables
 *
 * @author RUTWIK PATEL
 * @author Brijesh Lakkad
 */
public class Country implements JSONable {
    /**
     * Used to keep the track of unique IDs for the continent.
     */
    public static int d_SerialNumber = 0;
    /**
     * Auto-generated ID of the country.
     */
    private final Integer COUNTRY_ID;
    private String d_countryName;
    private Continent d_continent;
    private List<Country> d_neighbourCountries;
    private Player d_ownedBy;
    private int d_numberOfArmies;
    private String d_xCoordinate;
    private String d_yCoordinate;

    /**
     * Assigns country id to the country and creates the neighbour countries list.
     */
    public Country() {
        this.COUNTRY_ID = ++d_SerialNumber;
        d_neighbourCountries = new ArrayList<>();
    }

    /**
     * Assigns country id to the country and creates the neighbour countries list.
     *
     * @param p_countryId Country id.
     */
    public Country(int p_countryId) {
        this.COUNTRY_ID = p_countryId;
        d_neighbourCountries = new ArrayList<>();
    }

    /**
     * Assigns country name to country and creates the neighbour countries list.
     *
     * @param p_countryName Country name.
     */
    public Country(String p_countryName) {
        this.COUNTRY_ID = ++d_SerialNumber;
        d_neighbourCountries = new ArrayList<>();
        d_countryName = p_countryName;
    }

    /**
     * Resets the serial number to zero. Used when the map engine is being reset.
     */
    public static void resetSerialNumber() {
        d_SerialNumber = 0;
    }

    /**
     * Sets the ID for the country.
     *
     * @return Value of the country ID.
     */
    public Integer getCountryId() {
        return COUNTRY_ID;
    }

    /**
     * Gets this country name.
     *
     * @return this name of the country.
     */
    public String getCountryName() {
        return d_countryName;
    }

    /**
     * Sets the country name.
     *
     * @param p_countryName Value of the country name.
     */
    public void setCountryName(String p_countryName) {
        d_countryName = p_countryName;
    }

    /**
     * Gets the continent of this country.
     *
     * @return continent of this country.
     */
    public Continent getContinent() {
        return d_continent;
    }

    /**
     * Sets the continent for this country.
     *
     * @param p_continent Represents the value of continent.
     */
    public void setContinent(Continent p_continent) {
        d_continent = p_continent;
    }

    /**
     * Gets the list of neighbor countries.
     *
     * @return Value of neighboring countries list.
     */
    public List<Country> getNeighbourCountries() {
        return d_neighbourCountries;
    }

    /**
     * Sets the list of the neighboring countries.
     *
     * @param p_neighbourCountries List of neighboring countries.
     */
    public void setNeighbourCountries(List<Country> p_neighbourCountries) {
        d_neighbourCountries = p_neighbourCountries;
    }

    /**
     * Adds the neighbor to the country.
     *
     * @param p_neighbourCountry Value of the neighbor country.
     */
    public void addNeighbourCountry(Country p_neighbourCountry) {
        d_neighbourCountries.add(p_neighbourCountry);
    }

    /**
     * Removed the neighbor from the country.
     *
     * @param p_neighbourCountry Value of the neighbor country.
     */
    public void removeNeighbourCountry(Country p_neighbourCountry) {
        d_neighbourCountries.remove(p_neighbourCountry);
    }

    /**
     * Getter method to determine country owner.
     *
     * @return country owner object.
     */
    public Player getOwnedBy() {
        return d_ownedBy;
    }

    /**
     * Setter method for country owner.
     *
     * @param p_ownedBy Country owner object.
     */
    public void setOwnedBy(Player p_ownedBy) {
        d_ownedBy = p_ownedBy;
    }

    /**
     * Gets the number of armies that are placed on this country by the player <code>getOwnedBy</code>
     *
     * @return Value of the count of armies.
     */
    public int getNumberOfArmies() {
        return d_numberOfArmies;
    }

    /**
     * Sets the number of armies for this country placed by the player.
     *
     * @param p_numberOfArmies Values of the count of armies.
     */
    public void setNumberOfArmies(int p_numberOfArmies) {
        d_numberOfArmies = p_numberOfArmies;
    }

    /**
     * Returns the X coordinate of the Country.
     *
     * @return X coordinate.
     */
    public String getXCoordinate() {
        if (d_xCoordinate == null) {
            return "0";
        }
        return d_xCoordinate;
    }

    /**
     * Sets the X coordinate of the Country.
     *
     * @param p_xCoordinate X coordinate.
     */
    public void setXCoordinate(String p_xCoordinate) {
        this.d_xCoordinate = p_xCoordinate;
    }

    /**
     * Returns the Y coordinate of the Country.
     *
     * @return Y coordinate.
     */
    public String getYCoordinate() {
        if (d_yCoordinate == null) {
            return "0";
        }
        return d_yCoordinate;
    }

    /**
     * Sets the Y coordinate of the Country.
     *
     * @param p_yCoordinate Y coordinate.
     */
    public void setYCoordinate(String p_yCoordinate) {
        this.d_yCoordinate = p_yCoordinate;
    }

    /**
     * Checks if both objects are the same using both the country and continent of the object.
     *
     * @param p_l_o Value of the second element to be checked with.
     * @return True if the both are same.
     */
    @Override
    public boolean equals(Object p_l_o) {
        if (this == p_l_o) return true;
        if (p_l_o == null || getClass() != p_l_o.getClass()) return false;
        Country l_l_country = (Country) p_l_o;
        return COUNTRY_ID.equals(l_l_country.COUNTRY_ID) &&
                d_continent.equals(l_l_country.d_continent);
    }

    /**
     * Return the hash value of the country.
     *
     * @return Hash value of the country.
     */
    @Override
    public int hashCode() {
        return Objects.hash(COUNTRY_ID, d_continent);
    }

    /**
     * Creates <code>JSONObject</code> using the runtime information stored in data members of this class.
     *
     * @return Created <code>JSONObject</code>.
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_countryJSON = new JSONObject();
        l_countryJSON.put("name", d_countryName);
        l_countryJSON.put("numberOfArmies", d_numberOfArmies);
        return l_countryJSON;
    }

    /**
     * Creates an instance of this class and assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @return Created instance of this class using the provided JSON data.
     */
    public static Country fromJSON(JSONObject p_jsonObject) {
        Country l_country = new Country();
        l_country.setCountryName(p_jsonObject.getString("name"));
        l_country.setNumberOfArmies(p_jsonObject.getInt("numberOfArmies"));
        return l_country;
    }
}
