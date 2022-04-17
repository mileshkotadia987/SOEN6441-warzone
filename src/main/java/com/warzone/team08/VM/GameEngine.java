package com.warzone.team08.VM;

import com.warzone.team08.VM.constants.interfaces.JSONable;
import com.warzone.team08.VM.exceptions.InvalidGameException;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.map_editor.MapEditorEngine;
import com.warzone.team08.VM.phases.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Creates an environment for the player to store the information.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class GameEngine implements JSONable {
    /**
     * State object of the GameEngine
     */
    private Phase d_gameState;

    /**
     * <code>MapEditorEngine</code> for this game.
     * VM runtime map-editor engine to store map runtime information.
     */
    private MapEditorEngine d_mapEditorEngine;

    /**
     * <code>GamePlayEngine</code> for this game.
     * VM runtime game-play engine to store runtime after game starts.
     */
    private GamePlayEngine d_gamePlayEngine;

    private boolean d_isTournamentModeOn = false;

    /**
     * Default constructor.
     */
    public GameEngine() {
        this.initialise();
        // MAP_EDITOR ENGINE
        d_mapEditorEngine = new MapEditorEngine();
        d_mapEditorEngine.initialise();
        // GAME_PLAY ENGINE
        d_gamePlayEngine = new GamePlayEngine();
        d_gamePlayEngine.initialise();
    }

    /**
     * Sets the MapEditor and GamePlay engines for this tournament round. This method will be used at the time of
     * tournament. Calling of this method also sets the game mode to tournament.
     *
     * @param p_mapEditorEngine MapEditor engine.
     * @param p_gamePlayEngine  GamePlay engine.
     */
    public GameEngine(MapEditorEngine p_mapEditorEngine, GamePlayEngine p_gamePlayEngine) {
        this.initialise();
        d_mapEditorEngine = p_mapEditorEngine;
        d_gamePlayEngine = p_gamePlayEngine;
        d_isTournamentModeOn = true;
    }

    /**
     * Initialise all the engines to reset the runtime information.
     */
    public void initialise() {
        this.setGamePhase(new Preload(this));
    }

    /**
     * Signals its engines to shutdown.
     */
    public void shutdown() {
        d_mapEditorEngine.shutdown();
        d_gamePlayEngine.shutdown();
    }

    /**
     * Sets new phase for the game.
     *
     * @param p_gamePhase New value of the game phase.
     */
    public void setGamePhase(Phase p_gamePhase) {
        d_gameState = p_gamePhase;
    }

    /**
     * Gets the phase of game.
     *
     * @return Value of the game phase.
     */
    public Phase getGamePhase() {
        return d_gameState;
    }

    /**
     * Gets VM runtime map-editor engine to store map runtime information.
     *
     * @return Value of the map editor engine.
     */
    public MapEditorEngine getMapEditorEngine() {
        return d_mapEditorEngine;
    }

    /**
     * Sets map-editor engine having the runtime information.
     *
     * @param p_mapEditorEngine Value of the map editor engine.
     */
    public void setMapEditorEngine(MapEditorEngine p_mapEditorEngine) {
        d_mapEditorEngine = p_mapEditorEngine;
    }

    /**
     * Gets VM runtime game-play engine to store runtime after game starts.
     *
     * @return Value of the game-play engine.
     */
    public GamePlayEngine getGamePlayEngine() {
        return d_gamePlayEngine;
    }

    /**
     * Sets game-play engine having the runtime information.
     *
     * @param p_gamePlayEngine Value of the game-play engine.
     */
    public void setGamePlayEngine(GamePlayEngine p_gamePlayEngine) {
        d_gamePlayEngine = p_gamePlayEngine;
    }

    /**
     * Check if the tournament mode is on.
     *
     * @return True if the game mode is tournament; false otherwise.
     */
    public boolean isTournamentModeOn() {
        return d_isTournamentModeOn;
    }

    /**
     * Set the tournament mode
     *
     * @param p_tournamentMode True if the tournament mode is on; false otherwise.
     */
    public void setTournamentMode(boolean p_tournamentMode) {
        d_isTournamentModeOn = p_tournamentMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_gameEngineJSON = new JSONObject();
        l_gameEngineJSON.put("map_editor", this.getMapEditorEngine().toJSON());
        l_gameEngineJSON.put("game_pay", this.getGamePlayEngine().toJSON());
        l_gameEngineJSON.put("phase", this.getGamePhase().getClass().getSimpleName());
        return l_gameEngineJSON;
    }

    /**
     * Creates an instance of this class and assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @return Created instance of this class using the provided JSON data.
     * @throws InvalidGameException If the information from JSONObject cannot be used because it is corrupted or missing
     *                              the values.
     */
    public static GameEngine fromJSON(JSONObject p_jsonObject) throws InvalidGameException {
        try {
            // Create and load GameEngine.
            GameEngine l_gameEngine = new GameEngine();
            VirtualMachine.setGameEngine(l_gameEngine);

            MapEditorEngine.fromJSON(p_jsonObject.getJSONObject("map_editor"), l_gameEngine);
            GamePlayEngine l_gamePlayEngine = GamePlayEngine.fromJSON(p_jsonObject.getJSONObject("game_pay"), l_gameEngine);

            // Set the game phase.
            // The phase can be only from the following.
            String l_phaseString = p_jsonObject.getString("phase");
            if (l_phaseString.equals(Preload.class.getSimpleName())) {
                l_gameEngine.setGamePhase(new Preload(l_gameEngine));
            } else if (l_phaseString.equals(PostLoad.class.getSimpleName())) {
                l_gameEngine.setGamePhase(new PostLoad(l_gameEngine));
            } else if (l_phaseString.equals(PlaySetup.class.getSimpleName())) {
                l_gameEngine.setGamePhase(new PlaySetup(l_gameEngine));
            } else if (l_phaseString.equals(IssueOrder.class.getSimpleName())) {
                l_gameEngine.setGamePhase(new IssueOrder(l_gameEngine));
                l_gamePlayEngine.startGameLoop();
            }
            return l_gameEngine;
        } catch (JSONException p_jsonException) {
            throw new InvalidGameException("Missing values or the corrupted game file!");
        }
    }
}