package com.warzone.team08.VM.entities.cards;

import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.interfaces.Card;

/**
 * The Bomb Card allows player to target an enemy or neutral territory and kill half of the armies on that territory.
 * Player can target any territory that’s adjacent to one of your own territories.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class BombCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public BombCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.BOMB;
    }
}
