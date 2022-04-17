package com.warzone.team08.VM.common.services;

import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.interfaces.Card;
import com.warzone.team08.VM.entities.cards.*;

import java.util.List;
import java.util.Random;

/**
 * This class handles the operation to randomly assign the card to player.
 *
 * @author CHARIT
 */
public class CardService {

    /**
     * List of CardType objects indicating the card type.
     */
    public static List<CardType> d_CardList = CardType.usableCardList();

    /**
     * Randomly assigns card to the player.
     *
     * @return Card name
     */
    public static Card randomCard() {
        Random rand = new Random();
        // Getting a card-type using random index and creating a card using the type.
        return createCard(d_CardList.get(rand.nextInt(d_CardList.size())));
    }

    /**
     * Creates card using the type of card.
     *
     * @param p_cardType Type of card.
     * @return Card name
     */
    public static Card createCard(CardType p_cardType) {
        if (p_cardType == CardType.AIRLIFT) {
            return new AirliftCard();
        }
        if (p_cardType == CardType.BOMB) {
            return new BombCard();
        }
        if (p_cardType == CardType.BLOCKADE) {
            return new BlockadeCard();
        }
        if (p_cardType == CardType.DIPLOMACY) {
            return new DiplomacyCard();
        }
        return new EmptyCard();
    }
}
