package com.warzone.team08.VM.game_play.services;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.constants.enums.StrategyType;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.game_play.GamePlayEngine;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.repositories.PlayerRepository;

/**
 * This class handles `gameplayer` user command to add and/or remove game player from the game.
 *
 * @author Brijesh Lakkad
 * @author MILESH
 * @version 1.0
 */
public class PlayerService {

    /**
     * Engine to store and retrieve map data.
     */
    private final GamePlayEngine d_gamePlayEngine;

    /**
     * Player repository.
     */
    private final PlayerRepository d_playerRepository;

    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Initialization of different objects.
     */
    public PlayerService() {
        d_gamePlayEngine = VirtualMachine.getGameEngine().getGamePlayEngine();
        d_playerRepository = new PlayerRepository();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Adds the player to the list stored at Game Play engine.
     *
     * @param p_playerName   Value of the player name.
     * @param p_strategyType Strategy of the player.
     * @return Value of response of the request.
     * @throws InvalidInputException Throws if processing the player creation.
     */
    public String add(String p_playerName, String p_strategyType) throws InvalidInputException {
        if (!d_playerRepository.existByPlayerName(p_playerName)) {
            try {
                StrategyType l_strategyType;
                try {
                    l_strategyType = StrategyType.valueOf(p_strategyType.toUpperCase());
                } catch (IllegalArgumentException p_e) {
                    throw new InvalidInputException("Invalid strategy type!");
                }
                Player l_player = new Player(p_playerName, l_strategyType);
                d_gamePlayEngine.addPlayer(l_player);
                // Logging
                d_logEntryBuffer.dataChanged("gameplayer", p_playerName + " player added with strategy type: "+p_strategyType);

                return String.format("%s player added!", p_playerName);
            } catch (Exception e) {
                throw new InvalidInputException("Invalid player arguments!");
            }
        } else {
            throw new InvalidInputException("Player name already exists....Please provide different name.");
        }
    }

    /**
     * Removes the player from the list using the name.
     *
     * @param p_playerName Value of the continent name.
     * @return Value of response of the request.
     * @throws EntityNotFoundException If the player with provided name not found.
     */
    public String remove(String p_playerName) throws EntityNotFoundException {
        // We can check if the continent exists before filtering?
        // Filters the continent list using the continent name
        Player l_player = d_playerRepository.findByPlayerName(p_playerName);
        d_gamePlayEngine.removePlayer(l_player);
        // Logging
        d_logEntryBuffer.dataChanged("gameplayer", p_playerName + " player removed!");

        return String.format("%s player removed!", p_playerName);
    }
}
