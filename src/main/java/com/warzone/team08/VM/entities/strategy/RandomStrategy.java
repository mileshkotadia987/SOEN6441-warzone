package com.warzone.team08.VM.entities.strategy;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.constants.interfaces.Card;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.entities.orders.*;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidArgumentException;
import com.warzone.team08.VM.logger.LogEntryBuffer;

import java.util.List;
import java.util.Random;

/**
 * This class defines the behavior of random player.
 *
 * @author Deep Patel
 * @author Milesh
 */
public class RandomStrategy extends PlayerStrategy {
    private List<Country> d_ownedCountries;
    private Country d_randomCountry;
    private Country d_oppositeCountry;
    private final Random d_random = new Random();
    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Parameterised constructor to set the player.
     *
     * @param p_player Player of this strategy.
     */
    public RandomStrategy(Player p_player) {
        super(p_player);
    }

    /**
     * This method finds opposite player's country, that is neighbour of the randomly selected Country.
     *
     * @param p_randomCountry Random selected country.
     */
    public void findOppositionCountry(Country p_randomCountry) {
        for (Country l_neighbourCountry : p_randomCountry.getNeighbourCountries()) {
            if (!l_neighbourCountry.getOwnedBy().equals(d_player)) {
                d_oppositeCountry = l_neighbourCountry;
                break;
            }
        }
    }

    /**
     * This method is useful for create orders of the cards.
     *
     * @param p_card Card to be used.
     * @throws EntityNotFoundException  throws If not found.
     * @throws InvalidArgumentException throws If enter invalid input.
     */
    public void consumeCard(Card p_card) throws EntityNotFoundException, InvalidArgumentException {
        if (p_card.getType() == CardType.BOMB) {
            this.d_player.addOrder(new BombOrder(d_oppositeCountry.getCountryName(), d_player));
            return;
        }
        if (p_card.getType() == CardType.AIRLIFT) {
            Country d_targetCountry = d_ownedCountries.get(d_random.nextInt(d_ownedCountries.size()));
            this.d_player.addOrder(new AirliftOrder(d_randomCountry.getCountryName(), d_targetCountry.getCountryName(), String.valueOf(d_randomCountry.getNumberOfArmies() - 1), d_player));
            return;
        }
        if (p_card.getType() == CardType.BLOCKADE) {
            this.d_player.addOrder(new BlockadeOrder(d_oppositeCountry.getCountryName(), d_player));
            return;
        }
        if (p_card.getType() == CardType.DIPLOMACY) {
            this.d_player.addOrder(new NegotiateOrder(d_player, d_oppositeCountry.getOwnedBy().getName()));
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws InvalidArgumentException, EntityNotFoundException {
        d_ownedCountries = d_player.getAssignedCountries();
        if (d_ownedCountries.size() <= 0) {
            return;
        }
        d_randomCountry = d_ownedCountries.get(d_random.nextInt(d_ownedCountries.size()));
        findOppositionCountry(d_randomCountry);
        if (VirtualMachine.getGameEngine().isTournamentModeOn() && d_player.getRemainingReinforcementCount() > 0) {
            DeployOrder l_deployOrder = new DeployOrder(d_randomCountry.getCountryName(), String.valueOf(d_player.getRemainingReinforcementCount()), d_player);
            this.d_player.addOrder(l_deployOrder);
        }
        d_logEntryBuffer.dataChanged("issue_order", String.format("%s player's turn to Issue Order", this.d_player.getName()));

        if (d_oppositeCountry != null) {
            if (d_player.hasCard(CardType.BOMB) || d_player.hasCard(CardType.AIRLIFT) || d_player.hasCard(CardType.DIPLOMACY) || d_player.hasCard(CardType.BLOCKADE)) {
                Card l_card = d_player.getCards().get(d_random.nextInt(d_player.getCards().size()));
                consumeCard(l_card);
            }
            AdvanceOrder l_advanceOrder = new AdvanceOrder(
                    d_randomCountry.getCountryName(),
                    d_oppositeCountry.getCountryName(),
                    String.valueOf(d_randomCountry.getNumberOfArmies() + d_player.getRemainingReinforcementCount() - 1),
                    d_player);
            this.d_player.addOrder(l_advanceOrder);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StrategyType getType() {
        return StrategyType.RANDOM;
    }
}
