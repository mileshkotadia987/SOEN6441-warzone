package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.exceptions.VMException;

import java.util.List;

/**
 * This class implements the behaviour of Adapter class to load the map file.
 *
 * @author CHARIT
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class EditMapAdapter extends EditMapService {

    private final EditConquestMapService d_editConquestMapService;

    /**
     * Constructor to initialize <code>EditConquestMapService</code> class object.
     *
     * @param p_editConquestMapService Object of the EditConquestMapService.java class.
     */
    public EditMapAdapter(EditConquestMapService p_editConquestMapService) {
        d_editConquestMapService = p_editConquestMapService;
    }

    /**
     * Overrides the <code>execute()</code> method of the EditMapService.java(Target class in adapter pattern).
     *
     * @param p_commandValues Represents the values passed while running the command.
     * @return Value of string acknowledging user that the file is loaded or not.
     * @throws VMException Throws if error occurs in VM Engine operation.
     */
    @Override
    public String execute(List<String> p_commandValues) throws VMException {
        return d_editConquestMapService.execute(p_commandValues);
    }
}
