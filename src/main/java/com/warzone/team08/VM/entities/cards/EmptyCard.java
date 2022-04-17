package com.warzone.team08.VM.entities.cards;

import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.interfaces.Card;

/**
 * This card does nothing.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class EmptyCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public EmptyCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.EMPTY;
    }
}
