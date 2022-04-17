package com.warzone.team08.VM.phases;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.exceptions.VMException;

import java.util.List;
import java.util.Map;

/**
 * ConcreteState of the State pattern. In this example, defines behavior for commands that are valid in this state, and
 * for the others signifies that the command is invalid.
 * <p>
 * This state represents a group of states, and defines the behavior that is common to all the states in its group. All
 * the states in its group need to extend this class.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public abstract class MainPlay extends GamePlay {
    /**
     * Parameterised constructor to create an instance of <code>MainPlay</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    MainPlay(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String prepareTournament(List<Map<String, List<String>>> p_arguments) throws VMException{
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String loadMap(List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String setPlayers(String serviceType, List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String assignCountries(List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }
}
