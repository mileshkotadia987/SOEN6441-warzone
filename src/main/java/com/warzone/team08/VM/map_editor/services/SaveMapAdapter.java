package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.VM.exceptions.VMException;

import java.util.List;

/**
 * This class implements the behaviour of Adapter class to save the map file
 *
 * @author CHARIT
 */
public class SaveMapAdapter extends SaveMapService {

    private final SaveConquestMapService d_saveConquestMapService;

    /**
     * Constructor to initialize <code>SaveConquestMapService</code> class object.
     *
     * @param p_saveConquestMapService Object of the SaveConquestMapService.java class.
     */
    public SaveMapAdapter(SaveConquestMapService p_saveConquestMapService) {
        d_saveConquestMapService = p_saveConquestMapService;
    }

    /**
     * Overrides the <code>execute()</code> method of the SaveMapService.java(Target class in adapter pattern).
     *
     * @param p_arguments Represents the values passed while running the command.
     * @return Value of string acknowledging user that the file is loaded or not.
     * @throws VMException Throws if error occurs in VM Engine operation.
     */
    @Override
    public String execute(List<String> p_arguments) throws VMException {
        return d_saveConquestMapService.execute(p_arguments);
    }
}
