package com.warzone.team08.VM.game_play;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.exceptions.GameLoopIllegalStateException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.phases.Execute;
import com.warzone.team08.VM.phases.IssueOrder;
import com.warzone.team08.VM.phases.PlaySetup;
import com.warzone.team08.VM.phases.Reinforcement;

/**
 * Manages players and their orders runtime information; Responsible for executing orders in round-robin fashion.
 *
 * @author Brijesh Lakkad
 * @author MILESH
 * @version 1.0
 */
public class GameLoop {
    private GamePlayEngine d_gamePlayEngine;
    public volatile boolean d_isAlive = false;

    public GameLoop(GamePlayEngine p_gamePlayEngine) {
        d_gamePlayEngine = p_gamePlayEngine;
    }

    public void run() {
        if (d_isAlive) {
            return;
        }
        d_isAlive = true;
        GameEngine l_gameEngine = VirtualMachine.getGameEngine();
        try {
            if (l_gameEngine.getGamePhase().getClass().equals(PlaySetup.class)) {
                l_gameEngine.getGamePhase().nextState();
            } else if (l_gameEngine.getGamePhase().getClass().equals(IssueOrder.class)) {
                // When the game is loaded and it was in IssueOrder when saved.
            } else {
                throw new GameLoopIllegalStateException("Illegal state transition!");
            }
            // Responsive to thread interruption.
            while (d_isAlive) {
                if (l_gameEngine.getGamePhase().getClass().equals(Reinforcement.class)) {
                    l_gameEngine.getGamePhase().reinforce();
                }
                if (l_gameEngine.getGamePhase().getClass().equals(IssueOrder.class)) {
                    l_gameEngine.getGamePhase().issueOrder();
                }
                if (l_gameEngine.getGamePhase().getClass().equals(Execute.class)) {
                    l_gameEngine.getGamePhase().fortify();
                }
                l_gameEngine.getGamePhase().nextState();
                if (d_gamePlayEngine.checkIfGameIsOver()) {
                    // If the game is over, break the main-game-loop.
                    break;
                }
            }
        } catch (VMException p_vmException) {
            VirtualMachine.getInstance().stderr(p_vmException.getMessage());
        } finally {
            // This will set CLI#UserInteractionState to WAIT
            VirtualMachine.getInstance().stdout("GAME_ENGINE_STOPPED");
        }
    }

    public void stop() {
        d_isAlive = false;
    }

    public boolean isAlive() {
        return d_isAlive;
    }
}