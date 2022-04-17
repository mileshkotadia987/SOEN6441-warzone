package com.warzone.team08.VM.entities.strategy;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.entities.orders.*;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidArgumentException;
import com.warzone.team08.VM.logger.LogEntryBuffer;

/**
 * This class defines the behavior of aggressive player.
 *
 * @author Deep Patel
 * @author Brijesh Lakkad
 */
public class AggressiveStrategy extends PlayerStrategy {
    private Country d_attackingCountry;
    private Country d_oppositionCountry;
    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Calling super class constructor to provide the player of this strategy.
     *
     * @param p_player defines player
     */
    public AggressiveStrategy(Player p_player) {
        super(p_player);
    }

    /**
     * This method finds the strongest country of the aggressive player.
     */
    public void deployArmies() {
        int l_currentArmies = 0;

        // Try to find the strongest country.
        for (Country l_ownedCountry : d_player.getAssignedCountries()) {
            for (Country l_neighborCountry : l_ownedCountry.getNeighbourCountries()) {
                if (l_ownedCountry.getNumberOfArmies() > l_currentArmies &&
                        !l_neighborCountry.getOwnedBy().equals(d_player)) {
                    l_currentArmies = l_ownedCountry.getNumberOfArmies();
                    d_attackingCountry = l_ownedCountry;
                    d_oppositionCountry = l_neighborCountry;
                }
            }
        }
        if (d_attackingCountry == null) {
            // Distribute the armies to a country that has a neighbor country owned by another player.
            for (Country l_country : d_player.getAssignedCountries()) {
                for (Country l_neighbourCountry : l_country.getNeighbourCountries()) {
                    if (!l_neighbourCountry.getOwnedBy().equals(d_player)) {
                        d_attackingCountry = l_country;
                        d_oppositionCountry = l_neighbourCountry;
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method is useful for craete orders of the cards
     *
     * @throws EntityNotFoundException  throws If not found
     * @throws InvalidArgumentException throws If enter invalid input
     */
    public void consumeCard() throws EntityNotFoundException, InvalidArgumentException {
        if (d_player.hasCard(CardType.BOMB)) {
            this.d_player.addOrder(new BombOrder(d_oppositionCountry.getCountryName(), d_player));
            return;
        }
        // Checking if the player has blockade card.
        if (d_player.hasCard(CardType.BLOCKADE)) {
            int l_maximumReinforcementCount = 0;
            Country l_targetCountry = null;
            for (Country l_ownedCountry : d_player.getAssignedCountries()) {
                for (Country l_traverseCountry : l_ownedCountry.getNeighbourCountries()) {
                    if (!l_traverseCountry.getOwnedBy().equals(d_player)
                            && l_traverseCountry.getNumberOfArmies() > l_maximumReinforcementCount) {
                        l_maximumReinforcementCount = l_traverseCountry.getNumberOfArmies();
                        l_targetCountry = l_ownedCountry;
                    }
                }
            }
            if (l_targetCountry != null)
                this.d_player.addOrder(new BlockadeOrder(l_targetCountry.getCountryName(), d_player));
            return;
        }
        // Checking if the player has airlift card.
        if (d_player.hasCard(CardType.AIRLIFT)) {
            int l_maximumReinforcementCount = 0;
            Country l_targetCountry = null;
            for (Country l_traverseCountry : d_player.getAssignedCountries()) {
                if (l_traverseCountry.getNumberOfArmies() > l_maximumReinforcementCount &&
                        !l_traverseCountry.equals(d_attackingCountry)) {
                    l_maximumReinforcementCount = l_traverseCountry.getNumberOfArmies();
                    l_targetCountry = l_traverseCountry;
                }
            }
            if (l_targetCountry != null)
                this.d_player.addOrder(new AirliftOrder(l_targetCountry.getCountryName(),
                        d_attackingCountry.getCountryName(),
                        String.valueOf(Math.min(l_targetCountry.getNumberOfArmies() - 1, l_targetCountry.getNumberOfArmies())),
                        d_player));
            return;
        }
        // Checking if the player has negotiate card.
        if (d_player.hasCard(CardType.DIPLOMACY)) {
            Country l_targetCountry = null;
            for (Country l_traverseCountry : d_attackingCountry.getNeighbourCountries()) {
                if (!l_traverseCountry.getOwnedBy().equals(d_player)) {
                    l_targetCountry = l_traverseCountry;
                    break;
                }
            }
            if (l_targetCountry != null)
                this.d_player.addOrder(new NegotiateOrder(d_player, l_targetCountry.getOwnedBy().getName()));
            return;
        }
    }

    /**
     * Getter method for the country from which countries deploy
     *
     * @return the Country
     */
    public Country getDeployCountry() {
        return d_attackingCountry;
    }

    /**
     * Getter method for fetch defending country
     *
     * @return the country
     */
    public Country getOppositionCountry() {
        return d_oppositionCountry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws InvalidArgumentException, EntityNotFoundException {
        deployArmies();
        d_logEntryBuffer.dataChanged("issue_order", String.format("%s player's turn to Issue Order", this.d_player.getName()));

        int l_remainingReinforcementCount = d_player.getRemainingReinforcementCount();
        if (VirtualMachine.getGameEngine().isTournamentModeOn() && l_remainingReinforcementCount > 0) {
            // Distribute the armies to a country that has a neighbor country owned by another player.
            if (d_attackingCountry != null) {
                // Create a deploy order.
                this.d_player.addOrder(new DeployOrder(d_attackingCountry.getCountryName(),
                        String.valueOf(l_remainingReinforcementCount),
                        d_player));
            }
        }

        consumeCard();

        // Create an advance order.
        AdvanceOrder l_advanceOrder = new AdvanceOrder(
                d_attackingCountry.getCountryName(),
                d_oppositionCountry.getCountryName(),
                String.valueOf(d_attackingCountry.getNumberOfArmies() + l_remainingReinforcementCount - 1),
                d_player);
        this.d_player.addOrder(l_advanceOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StrategyType getType() {
        return StrategyType.AGGRESSIVE;
    }
}
