package com.warzone.team08.VM.game_play;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.TournamentEngine;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.interfaces.Engine;
import com.warzone.team08.VM.constants.interfaces.JSONable;
import com.warzone.team08.VM.constants.interfaces.Order;
import com.warzone.team08.VM.entities.GameResult;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidGameException;
import com.warzone.team08.VM.mappers.OrderMapper;
import com.warzone.team08.VM.repositories.PlayerRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manages players and their orders runtime information; Responsible for executing orders in round-robin fashion.
 *
 * @author Brijesh Lakkad
 * @author MILESH
 * @version 1.0
 */
public class GamePlayEngine implements Engine, JSONable {
    /**
     * Players of the game.
     */
    private List<Player> d_playerList;

    /**
     * Current turn of the player for issuing the order.
     */
    private int d_currentPlayerTurn = 0;

    /**
     * Keeps the track of the first player who was selected by the engine while <code>GAME_LOOP#ISSUE_ORDER</code>
     * state.
     */
    private int d_currentPlayerForIssuePhase = 0;

    /**
     * Keeps the track of the first player who was selected by the engine while <code>GAME_LOOP#EXECUTE_ORDER</code>
     * state.
     */
    private int d_currentPlayerForExecutionPhase = 0;

    /**
     * Thread created by <code>GamePlayEngine</code>. This thread should be responsive to interruption.
     */
    private Thread d_LoopThread;

    /**
     * Main game loop.
     */
    private GameLoop d_gameLoop = new GameLoop(this);

    /**
     * Keeps track of the execution-index; it helps to decide order execution and expiration phase.
     */
    private static int d_currentExecutionIndex = 0;

    /**
     * List of the future orders which are supposed to be executed later in the future iterations.
     */
    private final List<Order> d_futurePhaseOrders = new ArrayList<>();

    /**
     * Result of the game. It will be null until the game is over.
     */
    private GameResult d_gameResult;

    private final static PlayerRepository d_PLAYER_REPOSITORY = new PlayerRepository();

    /**
     * Instance can not be created outside the class.
     */
    public GamePlayEngine() {
        this.initialise();
    }

    /**
     * Parameterised constructor to set the execution-index using offset.
     *
     * @param p_offset Offset to be set for the execution-index.
     */
    public GamePlayEngine(int p_offset) {
        this.initialise();
        d_currentExecutionIndex = p_offset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        d_playerList = new ArrayList<>();
    }

    /**
     * Adds the player to the list.
     *
     * @param p_player Player to be added.
     */
    public void addPlayer(Player p_player) {
        d_playerList.add(p_player);
    }

    /**
     * Removes the player from the list.
     *
     * @param p_player Player to be removed.
     */
    public void removePlayer(Player p_player) {
        d_playerList.remove(p_player);
    }

    /**
     * Gets the players of the game.
     *
     * @return Value of the player list.
     */
    public List<Player> getPlayerList() {
        return d_playerList;
    }

    /**
     * Sets the players of the game.
     *
     * @param p_playerList Value of the player list.
     */
    public void setPlayerList(List<Player> p_playerList) {
        d_playerList = p_playerList;
    }

    /**
     * Get the player who's turn for issuing the order.
     *
     * @return Value of the player which will issue the order.
     */
    public Player getCurrentPlayer() {
        Player l_currentPlayer = d_playerList.get(d_currentPlayerTurn);
        d_currentPlayerTurn++;
        // Round-robin fashion
        if (d_currentPlayerTurn >= d_playerList.size()) {
            d_currentPlayerTurn = 0;
        }
        return l_currentPlayer;
    }

    /**
     * Gets the index of current player.
     *
     * @return Value of index of current player.
     */
    public int getCurrentPlayerTurn() {
        return d_currentPlayerTurn;
    }

    /**
     * Sets the index of current player.
     *
     * @param p_currentPlayerTurn Value of index of current player.
     */
    public void setCurrentPlayerTurn(int p_currentPlayerTurn) {
        d_currentPlayerTurn = p_currentPlayerTurn;
    }

    /**
     * Gets the previously-stored player index whose turn is to issue an order.
     *
     * @return Value of the index.
     */
    public int getCurrentPlayerForIssuePhase() {
        return d_currentPlayerForIssuePhase;
    }

    /**
     * Sets the player index whose turn is going to issue an order in the next iteration.
     *
     * @param p_currentPlayerForIssuePhase Value of the index to be set.
     */
    public void setCurrentPlayerForIssuePhase(int p_currentPlayerForIssuePhase) {
        d_currentPlayerForIssuePhase = p_currentPlayerForIssuePhase;
    }

    /**
     * Gets the previously-stored player index to get an order of the player for execution.
     *
     * @return Value of the index.
     */
    public int getCurrentPlayerForExecutionPhase() {
        return d_currentPlayerForExecutionPhase;
    }

    /**
     * Sets the player index whose order is going to be executed first in the next iteration.
     *
     * @param p_currentPlayerForExecutionPhase Value of the index to be set.
     */
    public void setCurrentPlayerForExecutionPhase(int p_currentPlayerForExecutionPhase) {
        d_currentPlayerForExecutionPhase = p_currentPlayerForExecutionPhase;
    }

    /**
     * Gets the current execution index. This index helps to keep track of orders; some of those should be executed and
     * others of those should be expired during this loop iteration.
     *
     * @return Value of the index.
     */
    public static int getCurrentExecutionIndex() {
        return d_currentExecutionIndex;
    }

    /**
     * Gets the list of future orders which should be executed during this phase.
     *
     * @return Value of the list of orders.
     */
    public List<Order> getCurrentFutureOrders() {
        return d_futurePhaseOrders.stream().filter(p_futureOrder ->
                p_futureOrder.getExecutionIndex() == d_currentExecutionIndex
        ).collect(Collectors.toList());
    }

    /**
     * Gets the list of future orders which are going to be expired after this loop iteration.
     *
     * @return Value of the list of orders.
     */
    public List<Order> getExpiredFutureOrders() {
        return d_futurePhaseOrders.stream().filter(p_futureOrder ->
                p_futureOrder.getExpiryIndex() <= d_currentExecutionIndex
        ).collect(Collectors.toList());
    }

    /**
     * Adds the order to be executed in future iteration.
     *
     * @param p_futureOrder Value of the order to be added.
     */
    public void addFutureOrder(Order p_futureOrder) {
        this.d_futurePhaseOrders.add(p_futureOrder);
    }

    /**
     * Removes the order. This method is onlt being called if the order has been expired.
     *
     * @param p_futureOrder Value of the order to be added.
     */
    public void removeFutureOrder(Order p_futureOrder) {
        this.d_futurePhaseOrders.remove(p_futureOrder);
    }

    /**
     * Increments the current-execution-index.
     */
    public static void incrementEngineIndex() {
        d_currentExecutionIndex++;
    }

    /**
     * Starts the thread to iterate through various <code>GameLoopState</code> states. Channels the exception to
     * <code>stderr</code> method.
     */
    public void startGameLoop() {
        VirtualMachine.getInstance().stdout("GAME_ENGINE_STARTED");
        if (d_gameLoop.isAlive()) {
            d_gameLoop.stop();
        }
        if (d_LoopThread != null && d_LoopThread.isAlive()) {
            d_LoopThread.interrupt();
        }
        d_LoopThread = new Thread(() -> {
            d_gameLoop.run();
        });
        d_LoopThread.start();
    }

    /**
     * Returns thread representing the game loop.
     *
     * @return Thread.
     */
    public Thread getLoopThread() {
        return d_LoopThread;
    }

    /**
     * This game round will be over only when any player has won the game. If the game mode is tournament, then
     * additional condition for the game to be over is to exceed the turns of the round.
     *
     * @return True if the game is over; false otherwise.
     */
    public boolean checkIfGameIsOver() {
        if (VirtualMachine.getGameEngine().isTournamentModeOn() && d_currentExecutionIndex > TournamentEngine.getInstance().getMaxNumberOfTurns()) {
            d_gameResult = new GameResult(true, null);
            return true;
        }
        List<Player> l_playerWhoWonTheGame =
                VirtualMachine.getGameEngine().getGamePlayEngine().getPlayerList().stream().filter(Player::isWon).collect(Collectors.toList());
        if (l_playerWhoWonTheGame.size() > 0) {
            d_gameResult = new GameResult(false, l_playerWhoWonTheGame.get(0));
            return true;
        }
        return false;
    }

    /**
     * Gets the result of the game.
     *
     * @return Value of the game result.
     */
    public GameResult getGameResult() {
        return d_gameResult;
    }

    /**
     * Sets the result of the game.
     *
     * @param p_gameResult Value of the game result.
     */
    public void setGameResult(GameResult p_gameResult) {
        d_gameResult = p_gameResult;
    }

    /**
     * {@inheritDoc} Shuts the <code>GamePlayEngine</code>.
     */
    public void shutdown() {
        if (d_gameLoop.isAlive()) {
            d_gameLoop.stop();
        }
        // Interrupt thread if it is alive.
        if (d_LoopThread != null && d_LoopThread.isAlive())
            d_LoopThread.interrupt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_gamePlayEngineJSON = new JSONObject();
        JSONArray l_playerJSONList = new JSONArray();
        JSONObject l_friendPlayerJSON = new JSONObject();
        for (Player l_player : getPlayerList()) {
            l_playerJSONList.put(l_player.toJSON());
            JSONArray l_friendPlayers = new JSONArray();
            for (Player l_friendPlayer : l_player.getFriendPlayers()) {
                l_friendPlayers.put(l_friendPlayer.getName());
            }
            l_friendPlayerJSON.put(l_player.getName(), l_friendPlayers);
        }
        l_gamePlayEngineJSON.put("players", l_playerJSONList);
        l_gamePlayEngineJSON.put("friendPlayerMappings", l_friendPlayerJSON);
        l_gamePlayEngineJSON.put("currentPlayerForIssuePhase", getCurrentPlayerForIssuePhase());
        l_gamePlayEngineJSON.put("currentPlayerForExecutionPhase", getCurrentPlayerForExecutionPhase());
        l_gamePlayEngineJSON.put("currentExecutionIndex", getCurrentExecutionIndex());
        JSONArray l_futureOrderJSONList = new JSONArray();
        for (Order l_order : d_futurePhaseOrders) {
            JSONObject l_orderJSON = l_order.toJSON();
            l_orderJSON.put("futureExecutionIndex", l_order.getExecutionIndex());
            l_orderJSON.put("expiryExecutionIndex", l_order.getExpiryIndex());
            l_orderJSON.put("owner", l_order.getOwner().getName());
            l_futureOrderJSONList.put(l_orderJSON);
        }
        l_gamePlayEngineJSON.put("futureOrders", l_futureOrderJSONList);
        return l_gamePlayEngineJSON;
    }

    /**
     * Assigns the data members of the concrete class using the values inside <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @param p_gameEngine Instance of target <code>GameEngine</code>.
     * @return Created instance of this class using the provided JSON data.
     * @throws InvalidGameException If the information from JSONObject cannot be used because it is corrupted or missing
     *                              the values.
     */
    public static GamePlayEngine fromJSON(JSONObject p_jsonObject, GameEngine p_gameEngine) throws
            InvalidGameException {
        GamePlayEngine l_gamePlayEngine = new GamePlayEngine(p_jsonObject.getInt("currentExecutionIndex"));
        p_gameEngine.setGamePlayEngine(l_gamePlayEngine);

        JSONArray l_playerJSONList = p_jsonObject.getJSONArray("players");
        for (int l_playerIndex = 0; l_playerIndex < l_playerJSONList.length(); l_playerIndex++) {
            Player l_player = Player.fromJSON(l_playerJSONList.getJSONObject(l_playerIndex));
            l_gamePlayEngine.addPlayer(l_player);
        }

        JSONObject l_friendPlayerJSON = p_jsonObject.getJSONObject("friendPlayerMappings");
        Set<String> l_friendPlayerNameSet = l_friendPlayerJSON.keySet();
        try {
            for (String l_playerName : l_friendPlayerNameSet) {
                Player l_player = d_PLAYER_REPOSITORY.findByPlayerName(l_playerName);
                JSONArray l_friendPlayerNames = l_friendPlayerJSON.getJSONArray(l_playerName);
                for (int l_friendPlayerIndex = 0; l_friendPlayerIndex < l_friendPlayerNames.length(); l_friendPlayerIndex++) {
                    String l_friendPlayerName = l_friendPlayerNames.getString(l_friendPlayerIndex);
                    Player l_friendPlayer = d_PLAYER_REPOSITORY.findByPlayerName(l_friendPlayerName);
                    l_player.addNegotiatePlayer(l_friendPlayer);
                }
            }
        } catch (EntityNotFoundException p_entityNotFoundException) {
            throw new InvalidGameException();
        }

        l_gamePlayEngine.setCurrentPlayerForIssuePhase(p_jsonObject.getInt("currentPlayerForIssuePhase"));
        l_gamePlayEngine.setCurrentPlayerForExecutionPhase(p_jsonObject.getInt("currentPlayerForExecutionPhase"));

        OrderMapper l_orderMapper = new OrderMapper();
        JSONArray l_futureOrderJSONList = p_jsonObject.getJSONArray("futureOrders");
        try {
            for (int l_orderIndex = 0; l_orderIndex < l_futureOrderJSONList.length(); l_orderIndex++) {
                JSONObject l_orderJSON = l_futureOrderJSONList.getJSONObject(l_orderIndex);
                Player l_player = d_PLAYER_REPOSITORY.findByPlayerName(l_orderJSON.getString("owner"));
                l_orderMapper.toOrder(l_orderJSON,
                        l_player,
                        l_orderJSON.getInt("futureExecutionIndex"),
                        l_orderJSON.getInt("expiryExecutionIndex"));
            }
        } catch (EntityNotFoundException p_entityNotFoundException) {
            throw new InvalidGameException();
        }

        return l_gamePlayEngine;
    }
}