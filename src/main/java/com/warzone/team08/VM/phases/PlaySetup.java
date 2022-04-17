package com.warzone.team08.VM.phases;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.game_play.services.DistributeCountriesService;
import com.warzone.team08.VM.game_play.services.PlayerService;

import java.util.List;

/**
 * Concrete state of <code>Play</code>. This class is being used to add player(s) to the game and let players assign
 * countries.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class PlaySetup extends GamePlay {
    /**
     * Parameterised constructor to create an instance of <code>PlaySetup</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    public PlaySetup(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc}
     */
    public String loadMap(List<String> p_arguments) throws VMException {
        throw new VMException("map has been loaded");
    }

    /**
     * {@inheritDoc}
     */
    public String setPlayers(String serviceType, List<String> p_arguments) throws VMException {
        return this.invokeMethod(new PlayerService(), serviceType, p_arguments);
    }

    /**
     * {@inheritDoc}
     */
    public String assignCountries(List<String> p_arguments) throws VMException {
        DistributeCountriesService l_distributeCountriesService = new DistributeCountriesService();
        String l_responseValue = l_distributeCountriesService.execute(p_arguments);
        // Start game loop.
        this.d_gameEngine.getGamePlayEngine().startGameLoop();
        return l_responseValue;
    }

    /**
     * {@inheritDoc}
     */
    public void reinforce() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    public void issueOrder() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    public void fortify() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    public void endGame(List<String> p_arguments) throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    public void nextState() {
        d_gameEngine.setGamePhase(new Reinforcement(d_gameEngine));
    }
}
