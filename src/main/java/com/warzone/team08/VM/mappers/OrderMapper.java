package com.warzone.team08.VM.mappers;

import com.warzone.team08.VM.constants.enums.OrderType;
import com.warzone.team08.VM.constants.interfaces.Order;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.entities.orders.*;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidArgumentException;
import com.warzone.team08.VM.exceptions.InvalidCommandException;
import com.warzone.team08.VM.exceptions.InvalidGameException;
import com.warzone.team08.VM.responses.CommandResponse;
import org.json.JSONObject;

/**
 * The class to map <code>CommandResponse</code> to <code>Order</code>.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class OrderMapper {
    /**
     * Maps the <code>CommandResponse</code> to <code>Order</code> object.
     * <p>This method returns a new order instance created from <code>CommandResponse</code>.
     *
     * @param p_commandResponse Command object received at VM which represents the user command input.
     * @param p_player          Player who has issued the order.
     * @return Value of the new order created from user command response.
     * @throws EntityNotFoundException  Throws if the target entity not found.
     * @throws InvalidCommandException  Throws if the required command values are not present.
     * @throws InvalidArgumentException Throws if the argument can not be converted to the required type.
     */
    public Order toOrder(CommandResponse p_commandResponse, Player p_player)
            throws EntityNotFoundException,
            InvalidCommandException,
            InvalidArgumentException {
        OrderType l_orderType = OrderType.valueOf(p_commandResponse.getHeadCommand().toLowerCase());
        try {
            if (l_orderType == OrderType.advance) {
                return new AdvanceOrder(p_commandResponse.getCommandValues().get(0), p_commandResponse.getCommandValues().get(1), p_commandResponse.getCommandValues().get(2), p_player);
            } else if (l_orderType == OrderType.airlift) {
                return new AirliftOrder(p_commandResponse.getCommandValues().get(0), p_commandResponse.getCommandValues().get(1), p_commandResponse.getCommandValues().get(2), p_player);
            } else if (l_orderType == OrderType.blockade) {
                return new BlockadeOrder(p_commandResponse.getCommandValues().get(0), p_player);
            } else if (l_orderType == OrderType.bomb) {
                return new BombOrder(p_commandResponse.getCommandValues().get(0), p_player);
            } else if (l_orderType == OrderType.deploy) {
                return new DeployOrder(p_commandResponse.getCommandValues().get(0), p_commandResponse.getCommandValues().get(1), p_player);
            } else if (l_orderType == OrderType.negotiate) {
                return new NegotiateOrder(p_player, p_commandResponse.getCommandValues().get(0));
            }
            // If not known order, throws an exception.
        } catch (ArrayIndexOutOfBoundsException p_e) {
            // If not handled here, it will throw a InvalidCommandException.
        }
        throw new InvalidCommandException("Invalid command!");
    }

    /**
     * Maps the <code>JSONObject</code> to <code>Order</code> object.
     * <p>This method returns a new order instance created from <code>JSONObject</code>.
     *
     * @param p_jsonObject JSONObject having the information regarding the order to be created.
     * @param p_player     Player who has issued the order.
     * @return Value of the new order created from user command response.
     * @throws InvalidGameException Throws if JSON data is corrupted or required information was not present.
     */
    public Order toOrder(JSONObject p_jsonObject, Player p_player)
            throws InvalidGameException {
        OrderType l_orderType = p_jsonObject.getEnum(OrderType.class, "type");
        if (l_orderType == OrderType.advance) {
            return AdvanceOrder.fromJSON(p_jsonObject, p_player);
        } else if (l_orderType == OrderType.airlift) {
            return AirliftOrder.fromJSON(p_jsonObject, p_player);
        } else if (l_orderType == OrderType.blockade) {
            return BlockadeOrder.fromJSON(p_jsonObject, p_player);
        } else if (l_orderType == OrderType.bomb) {
            return BombOrder.fromJSON(p_jsonObject, p_player);
        } else if (l_orderType == OrderType.deploy) {
            return DeployOrder.fromJSON(p_jsonObject, p_player);
        } else if (l_orderType == OrderType.negotiate) {
            return NegotiateOrder.fromJSON(p_jsonObject, p_player);
        }
        throw new InvalidGameException();
    }

    /**
     * Overloading function to create a future-order from <code>JSONObject</code>.
     *
     * @param p_jsonObject     JSONObject having the information regarding the order to be created.
     * @param p_player         Player who has issued the order.
     * @param p_executionIndex Execution index of the order.
     * @param p_expiryIndex    Expiry index of the order.
     * @return Value of the new order created from user command response.
     * @throws InvalidGameException Throws if JSON data is corrupted or required information was not present.
     */
    public Order toOrder(JSONObject p_jsonObject, Player p_player, int p_executionIndex, int p_expiryIndex)
            throws InvalidGameException {
        OrderType l_orderType = p_jsonObject.getEnum(OrderType.class, "type");
        if (l_orderType == OrderType.negotiate) {
            return NegotiateOrder.fromJSON(p_jsonObject, p_player, p_executionIndex, p_expiryIndex);
        }
        return this.toOrder(p_jsonObject, p_player);
    }
}
