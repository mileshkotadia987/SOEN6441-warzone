package com.warzone.team08.VM.constants.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * This enum represents the type of card.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 * @see com.warzone.team08.VM.constants.interfaces.Card
 */
public enum CardType {
    /**
     * If card type is empty.
     */
    EMPTY("empty"),
    /**
     * If card type is airlift.
     */
    AIRLIFT("airlift"),
    /**
     * If card type is blockade.
     */
    BLOCKADE("blockade"),
    /**
     * If card type is bomb.
     */
    BOMB("bomb"),
    /**
     * If card type is diplomacy.
     */
    DIPLOMACY("diplomacy");

    /**
     * Variable to set enum value.
     */
    public String d_jsonValue;

    /**
     * Sets the string value of the enum.
     *
     * @param p_jsonValue Value of the enum.
     */
    private CardType(String p_jsonValue) {
        this.d_jsonValue = p_jsonValue;
    }

    /**
     * Gets usable card list.
     *
     * @return Value of card list.
     */
    public static List<CardType> usableCardList() {
        List<CardType> l_cardTypeList = new ArrayList<>();
        l_cardTypeList.add(CardType.AIRLIFT);
        l_cardTypeList.add(CardType.BLOCKADE);
        l_cardTypeList.add(CardType.BOMB);
        l_cardTypeList.add(CardType.DIPLOMACY);
        return l_cardTypeList;
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
