package com.warzone.team08.VM.repositories;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.entities.Player;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class finds the <code>Player</code> entity from the runtime engine.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class PlayerRepository {
    /**
     * Finds only <code>Player</code> using its name.
     *
     * @param p_playerName Value of the name of player.
     * @return Value of the first matched player.
     * @throws EntityNotFoundException Throws if the being searched entity has been not found.
     */
    public Player findByPlayerName(String p_playerName) throws EntityNotFoundException {
        List<Player> l_filteredPlayerList = VirtualMachine.getGameEngine().getGamePlayEngine().getPlayerList().stream().filter(p_player ->
                p_player.getName().equals(p_playerName)
        ).collect(Collectors.toList());
        if (l_filteredPlayerList.size() > 0)
            return l_filteredPlayerList.get(0);
        throw new EntityNotFoundException(String.format("'%s' player not found", p_playerName));
    }

    /**
     * Checks whether the player having same name already exists or not.
     *
     * @param p_playerName Player name.
     * @return True if player with same name already exists in the list of joined players; Otherwise false.
     */
    public boolean existByPlayerName(String p_playerName) {
        return VirtualMachine.getGameEngine().getGamePlayEngine().getPlayerList().stream().filter(p_player ->
                p_player.getName().equals(p_playerName)
        ).count() == 1;
    }
}
