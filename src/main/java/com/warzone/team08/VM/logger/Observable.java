package com.warzone.team08.VM.logger;

import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.ResourceNotFoundException;

import java.io.IOException;

/**
 * This interface defines the methods to implement the Observable.
 *
 * @author MILESH
 */
public interface Observable {

    /**
     * Attaches the observer to the list of available observers.
     *
     * @param o Object of observer.
     */
    void attach(Observer o);

    /**
     * Detaches the observer from the list of available observers.
     *
     * @param o Object of observer.
     */
    void detach(Observer o);

    /**
     * Notifies all the observer available in the list about the changes.
     *
     * @param p_o Object of observable
     * @throws ResourceNotFoundException Throws if observable not found.
     * @throws IOException               Throws when IOException occur during the execution.
     * @throws InvalidInputException     Throws if the input is invalid.
     */
    void notifyObservers(Observable p_o) throws ResourceNotFoundException, IOException, InvalidInputException;
}
