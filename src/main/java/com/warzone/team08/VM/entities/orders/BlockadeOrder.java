package com.warzone.team08.VM.entities.orders;

import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.enums.OrderType;
import com.warzone.team08.VM.constants.interfaces.Card;
import com.warzone.team08.VM.constants.interfaces.Order;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.CardNotFoundException;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidGameException;
import com.warzone.team08.VM.exceptions.InvalidOrderException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.repositories.CountryRepository;
import org.json.JSONObject;

import java.util.List;

/**
 * This class implements the operations required to be perform when the Blockade card is used. When blockade card is
 * used then it simply multiplies the number of armies by certain constant value and make that country a neutral
 * country. A player can perform blockade operation on its own country.
 *
 * @author CHARIT
 * @version 2.0
 */
public class BlockadeOrder extends Order {
    private final Country d_targetCountry;
    /**
     * Constant to multiply the armies count.
     */
    public static final int CONSTANT = 3;

    /**
     * To find the country using its data members.
     */
    private final CountryRepository d_countryRepository = new CountryRepository();

    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Sets the country name and current player object.
     *
     * @param p_targetCountry Country name.
     * @param p_owner         Player who has initiated this order.
     * @throws EntityNotFoundException Throws if the given country is not found in the list of available countries.
     */
    public BlockadeOrder(String p_targetCountry, Player p_owner)
            throws EntityNotFoundException {
        super(p_owner);
        d_targetCountry = d_countryRepository.findFirstByCountryName(p_targetCountry);
    }

    /**
     * Performs actual blockade operation.
     *
     * @throws InvalidOrderException If the order can not be performed due to an invalid country, an invalid number * of
     *                               armies, or other invalid input.
     * @throws CardNotFoundException Card doesn't found in the player's card list.
     */
    public void execute() throws InvalidOrderException, CardNotFoundException {
        Country l_country;
        List<Country> l_countryList;
        Card l_requiredCard;
        if (d_targetCountry.getOwnedBy().equals(this.getOwner())) {
            l_requiredCard = this.getOwner().getCard(CardType.BLOCKADE);
        } else {
            throw new InvalidOrderException("You have selected opponent player's country to perform blockade operation.");
        }

        l_country = d_targetCountry;
        l_countryList = this.getOwner().getAssignedCountries();
        try {
            l_country.setNumberOfArmies(l_country.getNumberOfArmies() * CONSTANT);
            l_countryList.remove(l_country);
        } catch (Exception e) {
            throw new InvalidOrderException("You can not perform blockade operation as you don't own this country");
        }
        this.getOwner().setAssignedCountries(l_countryList);
        this.getOwner().removeCard(l_requiredCard);

        // Logging
        StringBuilder l_logResponse = new StringBuilder();
        l_logResponse.append("Blockade card to triple the armies in " + d_targetCountry.getCountryName() + "\n");
        d_logEntryBuffer.dataChanged("blockade", l_logResponse.toString());
    }

    /**
     * Returns the order type.
     *
     * @return Order type.
     */
    @Override
    public OrderType getType() {
        return OrderType.blockade;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expire() {
        // Does nothing.
    }

    /**
     * Returns the string describing player order.
     *
     * @return String representing player orders.
     */
    @Override
    public String toString() {
        return String.format("%s %s", getType().getJsonValue(), d_targetCountry.getCountryName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_order = new JSONObject();
        l_order.put("target", d_targetCountry.getCountryName());
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
    public static BlockadeOrder fromJSON(JSONObject p_jsonObject, Player p_player) throws InvalidGameException {
        try {
            return new BlockadeOrder(p_jsonObject.getString("target"),
                    p_player);
        } catch (EntityNotFoundException p_entityNotFoundException) {
            throw new InvalidGameException();
        }
    }
}
