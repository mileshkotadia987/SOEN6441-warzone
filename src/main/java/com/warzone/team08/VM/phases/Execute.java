package com.warzone.team08.VM.phases;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.ResourceNotFoundException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.game_play.services.ExecuteOrderService;

/**
 * Implements the method available for this phase of game.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class Execute extends MainPlay {
    /**
     * Parameterised constructor to create an instance of <code>Fortify</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    Execute(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinforce() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void issueOrder() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fortify() throws ResourceNotFoundException, InvalidInputException {
        ExecuteOrderService l_executeOrderService = new ExecuteOrderService();
        l_executeOrderService.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextState() {
        GamePlayEngine.incrementEngineIndex();
        d_gameEngine.setGamePhase(new Reinforcement(d_gameEngine));
    }
}
