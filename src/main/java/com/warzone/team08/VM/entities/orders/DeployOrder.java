package com.warzone.team08.VM.entities.orders;

import com.jakewharton.fliptables.FlipTable;
import com.warzone.team08.VM.constants.enums.OrderType;
import com.warzone.team08.VM.constants.interfaces.Order;
import com.warzone.team08.VM.entities.Country;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidArgumentException;
import com.warzone.team08.VM.exceptions.InvalidGameException;
import com.warzone.team08.VM.exceptions.InvalidOrderException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.repositories.CountryRepository;
import org.json.JSONObject;

/**
 * This order represents the deploy order.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class DeployOrder extends Order {
    private final Country d_targetCountry;
    private final int d_numOfArmies;
    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * To find the country using its data members.
     */
    private final CountryRepository d_countryRepository = new CountryRepository();

    /**
     * Default parameterised constructor.
     *
     * @param p_targetCountry Name of target country.
     * @param p_numOfArmies   Number of reinforcements to be deployed.
     * @param p_owner         Player who has initiated this order.
     * @throws EntityNotFoundException  Throws if the target country not found.
     * @throws InvalidArgumentException Throws if the number of armies is not a number.
     */
    public DeployOrder(String p_targetCountry, String p_numOfArmies, Player p_owner)
            throws EntityNotFoundException, InvalidArgumentException {
        super(p_owner);
        // Get the target country from repository.
        d_targetCountry = d_countryRepository.findFirstByCountryName(p_targetCountry);
        try {
            d_numOfArmies = Integer.parseInt(p_numOfArmies);
            // Checks if the number of moved armies is less than zero.
            if (d_numOfArmies < 0) {
                throw new InvalidArgumentException("Number of armies can not be negative.");
            }
        } catch (NumberFormatException p_e) {
            throw new InvalidArgumentException("Number of reinforcements is not a number.");
        }
    }

    /**
     * Executes the order during <code>GameLoopState#EXECUTE_ORDER</code>
     *
     * @throws InvalidOrderException If the order can not be performed due to an invalid country, an invalid number of
     *                               armies, or other invalid input.
     */
    public void execute() throws InvalidOrderException {
        StringBuilder l_logResponse = new StringBuilder();
        l_logResponse.append("\n" + "Executing " + this.getOwner().getName() + " Order:" + "\n");
        if (this.getOwner().getAssignedCountries().contains(d_targetCountry)) {
            int l_remainingReinforcementCount = this.getOwner().getRemainingReinforcementCount() - d_numOfArmies;
            if (l_remainingReinforcementCount < 0) {
                throw new InvalidOrderException("You don't have enough reinforcements.");
            }
            this.getOwner().setRemainingReinforcementCount(l_remainingReinforcementCount);
            d_targetCountry.setNumberOfArmies(this.d_targetCountry.getNumberOfArmies() + this.d_numOfArmies);
            this.getOwner().addExecutedOrder(this);

            // Logging
            l_logResponse.append("Deploying " + d_numOfArmies + " armies in " + d_targetCountry.getCountryName() + "\n");
            String[] l_header = {"COUNTRY", "ARMY COUNT"};
            String[][] l_changeContent = {
                    {d_targetCountry.getCountryName(), String.valueOf(d_numOfArmies)}
            };
            l_logResponse.append("\n Order Effect\n" + FlipTable.of(l_header, l_changeContent));
            d_logEntryBuffer.dataChanged("deploy", l_logResponse.toString());
        } else {
            throw new InvalidOrderException("You can deploy the reinforcements only in your assigned countries");
        }
    }

    /**
     * Gets the type of order.
     *
     * @return Value of the order type.
     */
    public OrderType getType() {
        return OrderType.deploy;
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
        return String.format("%s %s %s", getType().getJsonValue(), d_targetCountry.getCountryName(), d_numOfArmies);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_order = new JSONObject();
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
    public static DeployOrder fromJSON(JSONObject p_jsonObject, Player p_player) throws InvalidGameException {
        try {
            return new DeployOrder(p_jsonObject.getString("target"),
                    String.valueOf(p_jsonObject.getInt("numOfArmies")),
                    p_player);
        } catch (EntityNotFoundException | InvalidArgumentException p_vmException) {
            throw new InvalidGameException();
        }
    }
}
