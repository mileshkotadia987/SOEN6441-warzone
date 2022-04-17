package com.warzone.team08.VM;

import com.warzone.team08.UserInterfaceMiddleware;
import com.warzone.team08.VM.exceptions.ExceptionHandler;
import com.warzone.team08.VM.exceptions.ResourceNotFoundException;
import com.warzone.team08.VM.logger.LogEntryBuffer;
import com.warzone.team08.VM.logger.LogWriter;
import com.warzone.team08.VM.phases.Phase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Handles the connection with different user interfaces. Creates an environment for the player to store the
 * information.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class VirtualMachine {
    /**
     * Singleton instance of the class.
     */
    private static VirtualMachine d_Instance;

    /**
     * GameEngine attached to this VM. This engine is changeable.
     */
    private static GameEngine d_gameEngine;

    /**
     * List of User interface middleware. (Can be a stub/skeleton)
     */
    private UserInterfaceMiddleware d_userInterfaceMiddleware;

    /**
     * To execute <code>Future</code> tasks.
     */
    private final ExecutorService d_executor = Executors.newFixedThreadPool(10);

    private LogEntryBuffer d_logEntryBuffer;
    private LogWriter d_logWriter;

    /**
     * Creates the single instance of the <code>VirtualMachine</code> class.
     *
     * @return Value of the instance.
     */
    public static VirtualMachine newInstance() {
        d_Instance = new VirtualMachine();
        d_gameEngine = new GameEngine();
        d_Instance.d_logEntryBuffer = LogEntryBuffer.getLogger();
        try {
            d_Instance.d_logWriter = new LogWriter(d_Instance.d_logEntryBuffer);
        } catch (ResourceNotFoundException p_e) {
            VirtualMachine.getInstance().stderr("LogEntryBuffer failed!");
        }
        // Default exception handler.
//        ExceptionHandler l_exceptionHandler = new ExceptionHandler();
//        Thread.setDefaultUncaughtExceptionHandler(l_exceptionHandler);
        return d_Instance;
    }

    /**
     * Initialise engine to reset the runtime information.
     */
    public void initialise() {
        // Prepare instances.
        VirtualMachine.getGameEngine().initialise();
        VirtualMachine.TOURNAMENT_ENGINE().initialise();
    }

    /**
     * Terminates gracefully. Signals its engines to terminate.
     */
    public static void exit() {
        getGameEngine().shutdown();
        TOURNAMENT_ENGINE().shutdown();
        VirtualMachine.getInstance().stdout("Shutting down...");
    }

    /**
     * Adds the user interface middleware instance to the list of <code>VM</code>.
     * <p>Stubs/Skeleton can be created if CLI and VM are on different machines.
     *
     * @param p_userInterfaceMiddleware Value of user interface middleware
     */
    public void attachUIMiddleware(UserInterfaceMiddleware p_userInterfaceMiddleware) {
        d_userInterfaceMiddleware = p_userInterfaceMiddleware;
    }

    /**
     * Gets the single instance of the <code>VirtualMachine</code> class which was created before.
     *
     * @return Value of the instance.
     * @throws NullPointerException Throws if the virtual machine instance was not created before.
     */
    public static VirtualMachine getInstance() throws NullPointerException {
        if (d_Instance == null) {
            throw new NullPointerException("Virtual Machine was not created. Something went wrong.");
        }
        return d_Instance;
    }

    /**
     * Sets the game engine to store runtime information of the game.
     *
     * @param p_gameEngine Value of the game engine.
     */
    public static void setGameEngine(GameEngine p_gameEngine) {
        d_gameEngine = p_gameEngine;
    }

    /**
     * Gets game engine to store runtime information of the game.
     *
     * @return Value of the game engine.
     */
    public static GameEngine getGameEngine() {
        return d_gameEngine;
    }

    /**
     * Gets tournament engine to store information of the game while the game mode is tournament.
     *
     * @return Value of the game engine.
     */
    public static TournamentEngine TOURNAMENT_ENGINE() {
        return TournamentEngine.getInstance();
    }

    /**
     * Gets the state of the game
     *
     * @return Value of the game state
     */
    public static Phase getGamePhase() {
        return d_gameEngine.getGamePhase();
    }

    /**
     * Asks user interface for input/action.
     * <p>Used <code>asynchronous</code> operation as some requests may take longer than expected.</p>
     *
     * @param p_message Message to be shown before asking for input.
     * @return Value of the response to the request.
     */
    public Future<String> askForUserInput(String p_message) {
        return d_executor.submit(() ->
                d_userInterfaceMiddleware.askForUserInput(p_message)
        );
    }

    /**
     * Sends the message to output channel of the user interface.
     *
     * @param p_message Represents the message.
     */
    public void stdout(String p_message) {
        if (d_userInterfaceMiddleware != null)
            d_userInterfaceMiddleware.stdout(p_message);
    }

    /**
     * Sends the message to error channel of the user interface.
     *
     * @param p_message Represents the error message.
     */
    public void stderr(String p_message) {
        if (d_userInterfaceMiddleware != null)
            d_userInterfaceMiddleware.stderr(p_message);
    }
}
