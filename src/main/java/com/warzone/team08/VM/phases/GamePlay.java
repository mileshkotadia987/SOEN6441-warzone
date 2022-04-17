package com.warzone.team08.VM.phases;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.game_play.services.ShowMapService;

import java.util.List;
import java.util.Map;

/**
 * ConcreteState of the State pattern. In this example, defines behavior for commands that are valid in this state, and
 * for the others signifies that the command is invalid.
 * <p>
 * This state represents a group of states, and defines the behavior that is common to all the states in its group. All
 * the states in its group need to extend this class.
 *
 * @author Joey Paquet (From lecture code)
 * @author Brijesh Lakkad
 * @version 1.0
 */
public abstract class GamePlay extends Phase {
    /**
     * Parameterised constructor to create an instance of <code>Play</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    GamePlay(GameEngine p_gameEngine) {
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
    public String showMap(List<String> p_arguments) throws VMException {
        ShowMapService l_showMapService = new ShowMapService();
        return l_showMapService.execute(p_arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editMap(List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String saveMap(List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editContinent(String l_serviceType, List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editCountry(String l_serviceType, List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editNeighbor(String l_serviceType, List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validateMap(List<String> p_arguments) throws VMException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame(List<String> p_arguments) throws VMException {
        d_gameEngine.setGamePhase(new End(d_gameEngine));
    }
}
