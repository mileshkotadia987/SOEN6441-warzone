package com.warzone.team08.VM.entities.orders;

import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.enums.OrderType;
import com.warzone.team08.VM.constants.interfaces.Card;
import com.warzone.team08.VM.constants.interfaces.Order;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.CardNotFoundException;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidGameException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.repositories.PlayerRepository;
import org.json.JSONObject;


/**
 * This class implements the operations required to be perform when the Diplomacy(negotiate command) card is used.
 *
 * @author Deep Patel
 * @version 2.0
 */
public class NegotiateOrder extends Order {
    private final Player d_otherPlayer;

    /**
     * To find the player using its data members.
     */
    private final PlayerRepository d_playerRepository = new PlayerRepository();

    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Parameterised constructor initialize player with whom negotiation is happened.
     *
     * @param p_thisPlayer  First player object.
     * @param p_otherPlayer Second player object.
     * @throws EntityNotFoundException Throws if the country with the given name doesn't exist.
     */
    public NegotiateOrder(Player p_thisPlayer, String p_otherPlayer) throws EntityNotFoundException {
        super(p_thisPlayer);
        d_otherPlayer = d_playerRepository.findByPlayerName(p_otherPlayer);
    }

    /**
     * Parameterised constructor initialize player with whom negotiation is happened.
     *
     * @param p_thisPlayer     First player object.
     * @param p_otherPlayer    Second player object.
     * @param p_executionIndex Execution index of the order.
     * @param p_expiryIndex    Expiry index of the order.
     * @throws EntityNotFoundException Throws if the country with the given name doesn't exist.
     */
    public NegotiateOrder(Player p_thisPlayer,
                          String p_otherPlayer,
                          int p_executionIndex,
                          int p_expiryIndex) throws EntityNotFoundException {
        super(p_thisPlayer, p_executionIndex, p_expiryIndex);
        d_otherPlayer = d_playerRepository.findByPlayerName(p_otherPlayer);
    }

    /**
     * Executes the method for adding player in each-others negotiation list.
     *
     * @throws CardNotFoundException Card doesn't found in the player's card list.
     */
    @Override
    public void execute() throws CardNotFoundException {
        StringBuilder l_logResponse = new StringBuilder();
        l_logResponse.append("\n" + "Executing " + this.getOwner().getName() + " Order:" + "\n");
        // Get diplomacy card.
        Card l_requiredCard = this.getOwner().getCard(CardType.DIPLOMACY);
        this.getOwner().addNegotiatePlayer(d_otherPlayer);
        d_otherPlayer.addNegotiatePlayer(this.getOwner());
        this.getOwner().removeCard(l_requiredCard);

        // Logging
        l_logResponse.append("\n Order Effect\n" + "Negotiating between " + this.getOwner().getName() + " and " + d_otherPlayer.getName() + "\n");
        d_logEntryBuffer.dataChanged("negotiate", l_logResponse.toString());
    }

    /**
     * Gets the type of order.
     *
     * @return Value of the order type.
     */
    public OrderType getType() {
        return OrderType.negotiate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expire() {
        this.getOwner().removeNegotiatePlayer(d_otherPlayer);
        d_otherPlayer.removeNegotiatePlayer(this.getOwner());
    }

    /**
     * Returns the string describing player order.
     *
     * @return String representing player orders.
     */
    @Override
    public String toString() {
        return String.format("%s %s", getType().getJsonValue(), d_otherPlayer.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_order = new JSONObject();
        l_order.put("other_player", d_otherPlayer.getName());
        l_order.put("type", getType().name());
        return l_order;
    }

    /**
     * Creates an instance of this class and assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @param p_player     Player who had issued this order.
     * @return Created instance of this class using the provided JSON data.
     * @throws InvalidGameException If the information from JSONObject cannot be used because it is corrupted or missing
     *                              the values.
     */
    public static NegotiateOrder fromJSON(JSONObject p_jsonObject, Player p_player) throws InvalidGameException {
        try {
            return new NegotiateOrder(p_player, p_jsonObject.getString("other_player"));
        } catch (EntityNotFoundException p_vmException) {
            throw new InvalidGameException();
        }
    }

    /**
     * Creates an instance of this class and assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject     <code>JSONObject</code> holding the runtime information.
     * @param p_player         Player who had issued this order.
     * @param p_executionIndex Execution index of the order.
     * @param p_expiryIndex    Expiry index of the order.
     * @return Created instance of this class using the provided JSON data.
     * @throws InvalidGameException If the information from JSONObject cannot be used because it is corrupted or missing
     *                              the values.
     */
    public static NegotiateOrder fromJSON(JSONObject p_jsonObject,
                                          Player p_player,
                                          int p_executionIndex,
                                          int p_expiryIndex) throws InvalidGameException {
        try {
            return new NegotiateOrder(p_player, p_jsonObject.getString("other_player"), p_executionIndex, p_expiryIndex);
        } catch (EntityNotFoundException p_vmException) {
            throw new InvalidGameException();
        }
    }
}
