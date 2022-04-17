package com.warzone.team08.VM.entities.cards;

import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.interfaces.Card;

/**
 * The Diplomacy Card enforces peace between two players for a variable number of turns. While peace is enforced,
 * neither player will be able to attack the other. The card takes effect the turn after it is played.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class DiplomacyCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public DiplomacyCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.DIPLOMACY;
    }
}
