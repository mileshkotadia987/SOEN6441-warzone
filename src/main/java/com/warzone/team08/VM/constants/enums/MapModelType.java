package com.warzone.team08.VM.constants.enums;

/**
 * Enum represents the type of model for the data being read from the file.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public enum MapModelType {
    /**
     * If map file contains Map tag.
     */
    MAP("Map"),
    /**
     * If model data belongs to continents of map
     */
    CONTINENT("continents"),
    /**
     * If model data belongs to counties
     */
    COUNTRY("countries"),
    /**
     * If model data belongs to neighbours of the countries
     */
    BORDER("borders"),
    /**
     * If the model data belongs to territories.
     */
    TERRITORY("Territories");
    /**
     * Variable to set enum value.
     */
    public String d_jsonValue;

    /**
     * Sets the string value of the enum.
     *
     * @param p_jsonValue Value of the enum.
     */
    private MapModelType(String p_jsonValue) {
        this.d_jsonValue = p_jsonValue;
    }

    /**
     * Gets the string value of the enum
     *
     * @return Value of the enum
     */
    public String getJsonValue() {
        return d_jsonValue;
    }
}
