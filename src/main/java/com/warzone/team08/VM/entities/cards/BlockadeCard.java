package com.warzone.team08.VM.entities.cards;

import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.interfaces.Card;

/**
 * The blockade card works similarly to the emergency blockade card, however it is less effective. Instead of happening
 * at the beginning of your turn, it happens at the end. This means any attacks, airlifts, or other actions happen
 * before the territory changes into a neutral and increases its armies.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class BlockadeCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public BlockadeCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.BLOCKADE;
    }
}
