package com.warzone.team08.VM.entities.orders;

import com.jakewharton.fliptables.FlipTable;
import com.warzone.team08.VM.constants.enums.CardType;
import com.warzone.team08.VM.constants.enums.OrderType;
import com.warzone.team08.VM.constants.interfaces.Card;
import com.warzone.team08.VM.constants.interfaces.Order;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.*;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.repositories.CountryRepository;
import org.json.JSONObject;

/**
 * This class implements the operations required to be performed when the Airlift card is used.
 *
 * @author Deep Patel
 * @version 2.0
 */
public class AirliftOrder extends Order {
    private final Country d_sourceCountry;
    private final Country d_targetCountry;
    private final int d_numOfArmies;

    /**
     * To find the country using its data members.
     */
    private final static CountryRepository d_countryRepository = new CountryRepository();

    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * sets the source and target country id along with number of armies to be airlifted and player object.
     *
     * @param p_sourceCountry source country id from which armies will be airlifted
     * @param p_targetCountry target country id where armies will be moved.
     * @param p_numOfArmies   number of armies for airlift
     * @param p_owner         current player object
     * @throws EntityNotFoundException  Throws if the country with the given name doesn't exist.
     * @throws InvalidArgumentException Throws if the input is invalid.
     */
    public AirliftOrder(String p_sourceCountry, String p_targetCountry, String p_numOfArmies, Player p_owner)
            throws EntityNotFoundException, InvalidArgumentException {
        super(p_owner);
        d_sourceCountry = d_countryRepository.findFirstByCountryName(p_sourceCountry);
        d_targetCountry = d_countryRepository.findFirstByCountryName(p_targetCountry);
        try {
            d_numOfArmies = Integer.parseInt(p_numOfArmies);
            // Checks if the number of moved armies is less than zero.
            if (d_numOfArmies < 0) {
                throw new InvalidArgumentException("Number of armies can not be negative.");
            }
        } catch (NumberFormatException p_e) {
            throw new InvalidArgumentException("Number of reinforcements is not a number!");
        }
    }

    /**
     * Performs the airlift operation by transferring armies.
     *
     * @throws InvalidOrderException If the order can not be performed due to an invalid country, an invalid number of
     *                               armies, or other invalid input.
     * @throws CardNotFoundException Card doesn't found in the player's card list.
     */
    @Override
    public void execute() throws InvalidOrderException, CardNotFoundException {
        StringBuilder l_logResponse = new StringBuilder();
        l_logResponse.append("\n" + "Executing " + this.getOwner().getName() + " Order:" + "\n");
        // Verify that all the conditions has been fulfilled for the airlift command.
        Card l_requiredCard;
        if (d_sourceCountry.getOwnedBy().equals(this.getOwner()) && d_targetCountry.getOwnedBy().equals(this.getOwner())) {
            l_requiredCard = this.getOwner().getCard(CardType.AIRLIFT);
            if (d_sourceCountry.getNumberOfArmies() < d_numOfArmies) {
                throw new InvalidOrderException("Source country not have entered amount of armies for airlift!");
            }
        } else {
            throw new InvalidOrderException("You have to select source and target country both from your owned countries!");
        }

        int l_sourceCountryArmies = d_sourceCountry.getNumberOfArmies();
        int l_targetCountryArmies = d_targetCountry.getNumberOfArmies();
        l_sourceCountryArmies -= d_numOfArmies;
        l_targetCountryArmies += d_numOfArmies;
        d_sourceCountry.setNumberOfArmies(l_sourceCountryArmies);
        d_targetCountry.setNumberOfArmies(l_targetCountryArmies);
        this.getOwner().removeCard(l_requiredCard);

        // Logging
        l_logResponse.append(this.getOwner().getName() + " used the Airlift card to move " + d_numOfArmies + " armies from " + d_sourceCountry.getCountryName() + " to " + d_targetCountry.getCountryName() + "\n");
        String[] l_header = {"COUNTRY", "ARMY COUNT"};
        String[][] l_changeContent = {
                {d_sourceCountry.getCountryName(), String.valueOf(l_sourceCountryArmies)},
                {d_targetCountry.getCountryName(), String.valueOf(l_targetCountryArmies)}
        };
        l_logResponse.append("\n Order Effect\n" + FlipTable.of(l_header, l_changeContent));
        d_logEntryBuffer.dataChanged("airlift", l_logResponse.toString());
    }

    /**
     * Gets the type of order.
     *
     * @return Value of the order type.
     */
    @Override
    public OrderType getType() {
        return OrderType.airlift;
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
        return String.format("%s %s %s %s", getType().getJsonValue(), d_sourceCountry.getCountryName(), d_targetCountry.getCountryName(), d_numOfArmies);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_order = new JSONObject();
        l_order.put("source", d_sourceCountry.getCountryName());
        l_order.put("target", d_targetCountry.getCountryName());
        l_order.put("numOfArmies", d_numOfArmies);
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
    public static AirliftOrder fromJSON(JSONObject p_jsonObject, Player p_player) throws InvalidGameException {
        try {
            return new AirliftOrder(p_jsonObject.getString("source"),
                    p_jsonObject.getString("target"),
                    String.valueOf(p_jsonObject.getInt("numOfArmies")),
                    p_player);
        } catch (EntityNotFoundException | InvalidArgumentException p_entityNotFoundException) {
            throw new InvalidGameException();
        }
    }
}
