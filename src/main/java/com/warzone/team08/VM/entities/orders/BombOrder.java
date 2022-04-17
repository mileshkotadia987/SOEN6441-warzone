package com.warzone.team08.VM.entities.orders;

import com.jakewharton.fliptables.FlipTable;
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

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the operations required to be perform when the Bomb card is used. When bomb card is used then
 * it simple halves the number of armies available in the country on which the bomb operation is performed. A player can
 * perform bomb operation on the countries owned by the other players only.
 *
 * @author CHARIT
 * @version 2.0
 */
public class BombOrder extends Order {
    private final Country d_targetCountry;

    /**
     * To find the country using its data members.
     */
    private final CountryRepository d_countryRepository = new CountryRepository();

    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Sets the country name and current player object.
     *
     * @param p_targetCountry Name of target country.
     * @param p_owner         Current player object.
     * @throws EntityNotFoundException Throws if the target country not found.
     */
    public BombOrder(String p_targetCountry, Player p_owner) throws EntityNotFoundException {
        super(p_owner);
        // Get the target country from repository.
        d_targetCountry = d_countryRepository.findFirstByCountryName(p_targetCountry);
    }

    /**
     * Performs actual bomb operation.
     *
     * @throws InvalidOrderException If the order can not be performed due to an invalid country, an invalid number of
     *                               armies, or other invalid input.
     * @throws CardNotFoundException Card doesn't found in the player's card list.
     */
    public void execute() throws InvalidOrderException, CardNotFoundException {
        StringBuilder l_logResponse = new StringBuilder();
        l_logResponse.append("\n" + "Executing " + this.getOwner().getName() + " Order:" + "\n");
        Country l_country;
        List<Country> l_countryList;
        Card l_requiredCard;
        if (d_targetCountry.getOwnedBy() != this.getOwner()) {
            l_requiredCard = this.getOwner().getCard(CardType.BOMB);
        } else {
            throw new InvalidOrderException("You have selected your own country to perform bomb operation. Please select opponent player's country");
        }

        if (this.getOwner().isNotNegotiation(d_targetCountry.getOwnedBy())) {
            List<Country> l_neighbourCountryList = new ArrayList<>();
            l_country = d_targetCountry;
            l_countryList = this.getOwner().getAssignedCountries();
            for (Country l_co : l_countryList) {
                l_neighbourCountryList.addAll(l_co.getNeighbourCountries());
            }
            if (l_neighbourCountryList.contains(l_country)) {
                int l_finalArmies = l_country.getNumberOfArmies() / 2;
                l_country.setNumberOfArmies(l_finalArmies);
                //Remove card from list
                this.getOwner().removeCard(l_requiredCard);

                // Logging
                l_logResponse.append(this.getOwner().getName() + " used Bomb card to half the army count of " + l_country.getCountryName() + "\n");
                String[] l_header = {"COUNTRY", "ARMY COUNT"};
                String[][] l_changeContent = {
                        {l_country.getCountryName(), String.valueOf(l_country.getNumberOfArmies())}
                };
                l_logResponse.append("\n Order Effect\n" + FlipTable.of(l_header, l_changeContent));
                d_logEntryBuffer.dataChanged("bomb", l_logResponse.toString());
            } else {
                throw new InvalidOrderException("Invalid Country Name is provided!! Country must be a neighboring country.");
            }
        }
    }

    /**
     * Returns the order type.
     *
     * @return Order type.
     */
    @Override
    public OrderType getType() {
        return OrderType.bomb;
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
    public static BombOrder fromJSON(JSONObject p_jsonObject, Player p_player) throws InvalidGameException {
        try {
            return new BombOrder(p_jsonObject.getString("target"),
                    p_player);
        } catch (EntityNotFoundException p_entityNotFoundException) {
            throw new InvalidGameException();
        }
    }
}