package com.warzone.team08.VM.logger;

import com.warzone.team08.VM.exceptions.ResourceNotFoundException;
import com.warzone.team08.VM.utils.FileUtil;
import com.warzone.team08.VM.utils.PathResolverUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

/**
 * This class acts as a Observer to update the data in a log file.
 *
 * @author MILESH
 * @author RUTVIK
 * @author Brijesh Lakkad
 */
public class LogWriter extends Observer {
    private final File d_targetFile;

    /**
     * Constructor to initialize the file name and object.
     *
     * @param p_observable Observable object for observer
     * @throws ResourceNotFoundException Throws if not able to find the file.
     */
    public LogWriter(Observable p_observable) throws ResourceNotFoundException {
        super(p_observable);
        String l_timestamp = String.valueOf(new Date().getTime());
        String l_FileName = l_timestamp.concat("_log_file.log");
        String l_pathToFile = PathResolverUtil.resolveLogPath(l_FileName);
        d_targetFile = FileUtil.createFileIfNotExists(l_pathToFile);
    }

    /**
     * This method implements the update method of Observer interface to write data in file.
     *
     * @param p_observable Observable object with whom this observer in attached.
     */
    @Override
    public void update(Observable p_observable) {
        if (d_targetFile == null) {
            return;
        }
        String l_message = ((LogEntryBuffer) p_observable).getMessage();
        try (Writer l_writer = new FileWriter(d_targetFile, true)) {
            l_writer.append(l_message);
        } catch (IOException p_ioException) {
        }
    }
}