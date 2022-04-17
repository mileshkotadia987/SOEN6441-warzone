package com.warzone.team08.VM.phases;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.InvalidOrderException;
import com.warzone.team08.VM.exceptions.ResourceNotFoundException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.game_play.services.IssueOrderService;

/**
 * Implements the method available for this phase of game.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class IssueOrder extends MainPlay {
    /**
     * Parameterised constructor to create an instance of <code>Attack</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    public IssueOrder(GameEngine p_gameEngine) {
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
    public void issueOrder() throws ResourceNotFoundException, InvalidInputException, InvalidOrderException {
        IssueOrderService l_issueOrderService = new IssueOrderService();
        l_issueOrderService.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fortify() throws VMException {
        invalidCommand();
    }

    /**
     * Call this method to go the the next state in the sequence.
     */
    public void nextState() {
        d_gameEngine.setGamePhase(new Execute(d_gameEngine));
    }
}
