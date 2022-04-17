package com.warzone.team08;

import com.warzone.team08.CLI.CommandLineInterface;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.utils.FileUtil;
import com.warzone.team08.VM.utils.PathResolverUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The main class of the War Zone Team08
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class Application {
    /**
     * False if user is interacting, meaning user is playing the game; true otherwise
     */
    private volatile boolean d_IsRunning = true;

    /**
     * Command-line user interface; Responsible for taking input from a user.
     */
    private CommandLineInterface d_CommandLineInterface;

    /**
     * Connects interface with method APIs; An environment for the player to store the information.
     */
    private VirtualMachine d_VirtualMachine;

    /**
     * Default constructor.
     */
    public Application() {
        // Creates interface for user interaction.
        // Just a local variable as the instance is not being used/shared with any other class.
        d_CommandLineInterface = new CommandLineInterface(this);

        // Starts the runtime engine for the game.
        // Virtual Machine will have the UI middleware.
        d_VirtualMachine = VirtualMachine.newInstance();

        // Attaches the CLI (stub) to VM.
        d_VirtualMachine.attachUIMiddleware(d_CommandLineInterface);
    }

    /**
     * Main method to handle entire game operation.
     *
     * @param args Arguments.
     * @throws InterruptedException Throws if the interrupt is occurred in normal execution.
     */
    public static void main(String[] args) throws InterruptedException {
        Application l_application = new Application();

        // Sets the environment for game.
        l_application.handleApplicationStartup();

        // Starts the CLI
        l_application.handleCLIStartUp();
    }

    /**
     * Gets the instance of virtual machine.
     *
     * @return Value of virtual machine.
     */
    public VirtualMachine VIRTUAL_MACHINE() {
        return d_VirtualMachine;
    }

    /**
     * Only for testing purpose. Restores the map files to user data directory location. Downloads the files to user
     * location.
     *
     * @throws IOException        Throws if the directory can not be created. (because of permissions?)
     * @throws URISyntaxException Throws if the directory can not be found.
     */
    public void restoreMapFiles() throws IOException, URISyntaxException {
        // Download the files at user data directory.
        Path l_sourceMapFiles = Paths.get(Objects.requireNonNull(Application.class.getClassLoader().getResource("map_files")).toURI());
        Path l_userDataDirectory = PathResolverUtil.getUserDataDirectoryPath();
        Files.walk(l_sourceMapFiles)
                .forEach(source -> FileUtil.copy(source, l_userDataDirectory.resolve(l_sourceMapFiles.relativize(source))));
    }

    /**
     * Will be called when game starts.
     */
    public void handleApplicationStartup() {
        setIsRunning(true);
    }

    /**
     * Handles the startup of CLI.
     *
     * @throws InterruptedException If CLI interrupted by another thread.
     */
    public void handleCLIStartUp() throws InterruptedException {
        d_CommandLineInterface.d_thread.start();
        // Wait till the game is over.
        d_CommandLineInterface.d_thread.join();
    }

    /**
     * Gets false if user is interacting, meaning user is playing the game; true otherwise.
     *
     * @return Value of false if user is interacting, meaning user is playing the game; true otherwise.
     */
    public boolean isRunning() {
        return d_IsRunning;
    }

    /**
     * Sets new false if user is interacting, meaning user is playing the game; true otherwise.
     *
     * @param p_isRunning New value of false if user is interacting, meaning user is playing the game; true otherwise.
     */
    public void setIsRunning(boolean p_isRunning) {
        d_IsRunning = p_isRunning;
    }
}
