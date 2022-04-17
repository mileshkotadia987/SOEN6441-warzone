package com.warzone.team08.VM.repositories;

import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class finds the Continent entity from the runtime engine.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class ContinentRepository {
    /**
     * Finds the continent using continent name.
     *
     * @param p_continentName Value of the name of continent.
     * @return Value of the list of matched continents.
     */
    public List<Continent> findByContinentName(String p_continentName) {
        return VirtualMachine.getGameEngine().getMapEditorEngine().getContinentList().stream().filter(p_continent ->
                p_continent.getContinentName().equals(p_continentName)
        ).collect(Collectors.toList());
    }

    /**
     * Finds only one continent using its name.
     *
     * @param p_continentName Value of the name of continent.
     * @return Value of the first matched continents.
     * @throws EntityNotFoundException Throws if the being searched entity has been not found.
     */
    public Continent findFirstByContinentName(String p_continentName) throws EntityNotFoundException {
        List<Continent> l_continentList = this.findByContinentName(p_continentName);
        if (l_continentList.size() > 0)
            return l_continentList.get(0);
        throw new EntityNotFoundException(String.format("'%s' continent not found", p_continentName));
    }

    /**
     * Finds the continent using its id.
     *
     * @param p_continentId Value of the continent Id.
     * @return Value of the first matched continents.
     * @throws EntityNotFoundException Throws if the being searched entity has been not found.
     */
    public Continent findByContinentId(Integer p_continentId) throws EntityNotFoundException {
        List<Continent> l_continentList = VirtualMachine.getGameEngine().getMapEditorEngine().getContinentList().stream().filter(p_continent ->
                p_continent.getContinentId().equals(p_continentId)
        ).collect(Collectors.toList());
        if (!l_continentList.isEmpty()) {
            return l_continentList.get(0);
        }
        throw new EntityNotFoundException(String.format("Continent with %s id not found!", p_continentId));
    }
}
