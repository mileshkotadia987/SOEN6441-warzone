package com.warzone.team08.VM.constants.interfaces;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.OrderType;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.CardNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidOrderException;
import com.warzone.team08.VM.game_play.GamePlayEngine;

/**
 * This interface provides the methods to be implemented by different orders.
 *
 * @author Brijesh Lakkad
 * @author CHARIT
 * @version 1.0
 */
public abstract class Order implements JSONable {
    /**
     * Execution game-play-index to define when this order supposed to be executed.
     */
    private int d_executionIndex;
    /**
     * Defines when this order supposed to be expired.
     */
    private int d_expiryIndex = -1;
    private final Player d_owner;

    /**
     * Default constructor.
     *
     * @param p_player Player who issued this order.
     */
    public Order(Player p_player) {
        d_owner = p_player;
        if (this.getType() == OrderType.negotiate) {
            d_executionIndex = GamePlayEngine.getCurrentExecutionIndex() + 1;
            d_expiryIndex = d_executionIndex + 1;
            VirtualMachine.getGameEngine().getGamePlayEngine().addFutureOrder(this);
        } else {
            d_executionIndex = GamePlayEngine.getCurrentExecutionIndex();
        }
    }


    /**
     * When an order created from <code>JSONObject</code>.
     *
     * @param p_player         Player who issued this order.
     * @param p_executionIndex Execution index of the order.
     * @param p_expiryIndex    Expiry index of the order.
     */
    public Order(Player p_player, int p_executionIndex, int p_expiryIndex) {
        d_owner = p_player;
        if (this.getType() == OrderType.negotiate) {
            d_executionIndex = p_executionIndex;
            d_expiryIndex = p_expiryIndex;
            VirtualMachine.getGameEngine().getGamePlayEngine().addFutureOrder(this);
        } else {
            d_executionIndex = p_executionIndex;
        }
    }

    /**
     * Executes the order during <code>GameLoopState#EXECUTE_ORDER</code>
     *
     * @throws InvalidOrderException If the order can not be performed due to an invalid country, an invalid number of
     *                               armies, or other invalid input.
     * @throws CardNotFoundException Card doesn't found in the player's card list.
     */
    public abstract void execute() throws InvalidOrderException, CardNotFoundException;

    /**
     * Gets the type of the order.
     *
     * @return Value of the order type.
     */
    public abstract OrderType getType();

    /**
     * Gets the player who created the order.
     *
     * @return Owner of this order.
     */
    public Player getOwner() {
        return d_owner;
    }

    /**
     * Gets the execution index of this order.
     *
     * @return Value of the execution index.
     */
    public int getExecutionIndex() {
        return d_executionIndex;
    }

    /**
     * Sets the execution index of this order.
     *
     * @param p_executionIndex Value of the execution index.
     */
    public void setExecutionIndex(int p_executionIndex) {
        d_executionIndex = p_executionIndex;
    }

    /**
     * Gets the expiration index for this order.
     *
     * @return Value of the expiration index.
     */
    public int getExpiryIndex() {
        return d_expiryIndex;
    }


    /**
     * Sets the expiration index for this order.
     *
     * @param p_expiryIndex Value of the expiration index.
     */
    public void setExpiryIndex(int p_expiryIndex) {
        d_expiryIndex = p_expiryIndex;
    }

    /**
     * Reverse the effect of the order or makes the card expired which was previously been executed.
     */
    abstract public void expire();
}
