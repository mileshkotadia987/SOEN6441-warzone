package com.warzone.team08.VM.phases;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.game_play.services.AssignReinforcementService;

/**
 * Concrete state of the <code>Phase</code>. Extends the <code>MainPlay</code>.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class Reinforcement extends MainPlay {
    /**
     * Parameterised constructor to create an instance of <code>Preload</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    public Reinforcement(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinforce() throws VMException {
        AssignReinforcementService l_reinforcementService = new AssignReinforcementService();
        l_reinforcementService.execute();
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
    public void fortify() throws VMException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextState() {
        d_gameEngine.setGamePhase(new IssueOrder(d_gameEngine));
    }
}
