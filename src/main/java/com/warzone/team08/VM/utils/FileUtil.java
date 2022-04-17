package com.warzone.team08.VM.utils;

import com.warzone.team08.VM.constants.enums.FileType;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.ResourceNotFoundException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * This class provides common (logic) service of handling file operations.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class FileUtil {
    private static final String MAP_FILE_EXTENSION = "map";

    private static final String GAME_EXTENSION = "warzone";

    /**
     * Gets the value of the valid available file extension.
     *
     * @return Value of the file extension.
     */
    public static String getFileExtension() {
        return MAP_FILE_EXTENSION;
    }

    /**
     * Checks whether the given map file name is valid or not. Uses overloading method
     * <code>FileUtil#retrieveFile(String, FileType)</code>.
     *
     * @param p_filePath Value of the path to game file.
     * @return Value of File object for the file given with path.
     * @throws InvalidInputException     Throws if the file does not exist.
     * @throws ResourceNotFoundException Throws if file can not be created.
     */
    public static File retrieveMapFile(String p_filePath) throws ResourceNotFoundException, InvalidInputException {
        return retrieveFile(p_filePath, FileType.MAP);
    }

    /**
     * Checks whether the given game file name is valid or not. Uses overloading method
     * <code>FileUtil#retrieveFile(String, FileType)</code>.
     *
     * @param p_filePath Value of the path to game file.
     * @return Value of File object for the file given with path.
     * @throws InvalidInputException     Throws if the file does not exist.
     * @throws ResourceNotFoundException Throws if file can not be created.
     */
    public static File retrieveGameFile(String p_filePath) throws ResourceNotFoundException, InvalidInputException {
        return retrieveFile(p_filePath, FileType.GAME);
    }

    /**
     * Retrieves the file using file type and its name.
     *
     * @param p_filePath Value of the path to game file.
     * @param p_fileType Type of the file.
     * @return Value of File object for the file given with path.
     * @throws InvalidInputException     Throws if the file does not exist.
     * @throws ResourceNotFoundException Throws if file can not be created.
     */
    public static File retrieveFile(String p_filePath, FileType p_fileType) throws ResourceNotFoundException, InvalidInputException {
        File l_file = new File(p_filePath);
        String l_fileName = l_file.getName();
        try {
            l_file.createNewFile();
        } catch (Exception p_exception) {
            throw new ResourceNotFoundException("Can not create a file due to file permission!");
        }

        if (checksIfFileHasRequiredExtension(l_fileName, p_fileType)) {
            return l_file;
        }

        throw new InvalidInputException("Invalid file!");
    }

    /**
     * Checks whether file has required extension or not. It uses to <code>FileType</code> to distinguish the type of
     * file.
     *
     * @param p_fileName Name of the file.
     * @param p_fileType Type of the file.
     * @return True if file has requires argument; otherwise false.
     * @throws InvalidInputException Throws if filename is invalid.
     */
    public static boolean checksIfFileHasRequiredExtension(String p_fileName, FileType p_fileType) throws InvalidInputException {
        int l_index = p_fileName.lastIndexOf('.');
        if (l_index > 0) {
            char l_prevChar = p_fileName.charAt(l_index - 1);
            if (l_prevChar != '.') {
                String l_extension = p_fileName.substring(l_index + 1);
                if (p_fileType == FileType.MAP &&
                        l_extension.equalsIgnoreCase(FileUtil.getFileExtension())) {
                    return true;
                } else if (p_fileType == FileType.GAME &&
                        l_extension.equalsIgnoreCase(FileUtil.getGameExtension())) {
                    return true;
                }
                throw new InvalidInputException("File doesn't exist!");
            }
        }
        throw new InvalidInputException("File must have an extension!");
    }

    /**
     * Creates a file if it does not exist.
     *
     * @param p_filePath file path
     * @return File object of new file
     * @throws ResourceNotFoundException Throws if file not found
     */
    public static File createFileIfNotExists(String p_filePath) throws ResourceNotFoundException {
        File l_file = new File(p_filePath);
        try {
            l_file.createNewFile();
        } catch (Exception p_exception) {
            throw new ResourceNotFoundException("File can not be created!");
        }
        return l_file;
    }

    /**
     * Checks if the file exists or not.
     *
     * @param p_fileObject Value of the file object.
     * @return True if the file exists; otherwise throws an exception.
     * @throws ResourceNotFoundException Throws if file not found.
     */
    private static boolean checkIfFileExists(File p_fileObject) throws ResourceNotFoundException {
        if (!p_fileObject.exists()) {
            throw new ResourceNotFoundException("File doesn't exist!");
        }
        return true;
    }

    /**
     * Copies the file from source path to the destination. This method replaces the existing file at destination path.
     *
     * @param p_source Source path to the file.
     * @param p_dest   Destination path to the file.
     */
    public static void copy(Path p_source, Path p_dest) {
        try {
            // Ignore if the file already exists.
            if (!(new File(p_dest.toUri().getPath()).exists())) {
                Files.copy(p_source, p_dest, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception l_ignored) {
            // Ignore the exception while copying.
        }
    }

    /**
     * Gets the game extension.
     *
     * @return Value of the game extension.
     */
    public static String getGameExtension() {
        return GAME_EXTENSION;
    }
}
