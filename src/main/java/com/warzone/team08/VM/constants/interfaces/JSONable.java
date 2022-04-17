package com.warzone.team08.VM.constants.interfaces;

import com.warzone.team08.VM.exceptions.InvalidGameException;
import org.json.JSONObject;

/**
 * This interface provides the methods to be return <code>JSONObject</code> or to assign the data member using the
 * <code>JSONObject</code>.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public interface JSONable {
    /**
     * Creates <code>JSONObject</code> using the runtime information stored in data members of this class.
     *
     * @return Created <code>JSONObject</code>.
     */
    JSONObject toJSON();

    /**
     * Creates an instance of this class and assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @throws InvalidGameException If the information from JSONObject cannot be used because it is corrupted or missing
     *                              the values.
     */
    static void fromJSON(JSONObject p_jsonObject) throws InvalidGameException {
        throw new InvalidGameException("Concrete class didn't implement fromJSON method!");
    }
}
