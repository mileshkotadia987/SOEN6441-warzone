package com.warzone.team08.VM.constants.interfaces;

import com.warzone.team08.VM.constants.enums.CardType;

import java.util.Date;

/**
 * Base abstract class of card. Cards can be played to accomplish a variety of tasks, such as gaining additional armies,
 * spying on opponents, airlifting armies around, etc.
 * <p>
 * When player successfully attack and capture an enemy or neutral territory, at the end of that turn you will receive a
 * piece of a card, regardless of whether player lose the territory later that turn.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public abstract class Card {
    private final Date d_createdTime;


    /**
     * Constructor to assign its data members.
     */
    public Card() {
        d_createdTime = new Date();
    }

    /**
     * Returns the type of this card.
     *
     * @return Value of type of card.
     */
    public abstract CardType getType();

    /**
     * Gets the created time for this class.
     *
     * @return Value of timestamp.
     */
    public long getCreatedTime() {
        return this.d_createdTime.getTime();
    }
}
