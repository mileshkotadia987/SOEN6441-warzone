package com.warzone.team08.VM.constants.enums;

/**
 * The strategy types of the player.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public enum StrategyType {
    HUMAN("human"),
    AGGRESSIVE("aggressive"),
    BENEVOLENT("benevolent"),
    RANDOM("random"),
    CHEATER("cheater");

    /**
     * Variable to set enum value.
     */
    public String d_jsonValue;

    /**
     * Sets the string value of the enum.
     *
     * @param p_jsonValue Value of the enum.
     */
    private StrategyType(String p_jsonValue) {
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
