package com.warzone.team08.VM.phases;

import com.warzone.team08.VM.GameEngine;
import com.warzone.team08.VM.exceptions.InvalidCommandException;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.VMException;
import com.warzone.team08.VM.map_editor.services.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Concrete state of the <code>Phase</code>. Extends the <code>Edit</code>.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class PostLoad extends MapEditor {
    /**
     * Parameterised constructor to create an instance of <code>PostLoad</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    public PostLoad(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String prepareTournament(List<Map<String, List<String>>> p_arguments) throws VMException{
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editMap(List<String> p_arguments) throws VMException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editContinent(String l_serviceType, List<String> p_arguments) throws VMException {
        return this.invokeMethod(new ContinentService(), l_serviceType, p_arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editCountry(String l_serviceType, List<String> p_arguments) throws VMException {
        return this.invokeMethod(new CountryService(), l_serviceType, p_arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editNeighbor(String l_serviceType, List<String> p_arguments) throws VMException {
        return this.invokeMethod(new CountryNeighborService(), l_serviceType, p_arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validateMap(List<String> p_arguments) throws VMException {
        ValidateMapService l_validateMapService = new ValidateMapService();
        return l_validateMapService.execute(p_arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String saveMap(List<String> p_arguments) throws VMException {
        SaveMapService l_saveMapService;
        if (!p_arguments.isEmpty()) {
            if (p_arguments.get(1).equalsIgnoreCase("warzone")) {
                l_saveMapService = new SaveMapService();
            } else if (p_arguments.get(1).equalsIgnoreCase("conquest")) {
                l_saveMapService = new SaveMapAdapter(new SaveConquestMapService());
            } else {
                throw new InvalidCommandException("Map type is not valid");
            }
        } else {
            throw new InvalidInputException("Empty arguments found. Please provide required arguments.");
        }
        return l_saveMapService.execute(p_arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextState() throws VMException {
        throw new VMException("Map must be loaded!");
    }
}
