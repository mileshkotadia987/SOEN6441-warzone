package com.warzone.team08.CLI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warzone.team08.Application;
import com.warzone.team08.CLI.constants.states.UserInteractionState;
import com.warzone.team08.CLI.exceptions.InvalidArgumentException;
import com.warzone.team08.CLI.exceptions.InvalidCommandException;
import com.warzone.team08.CLI.mappers.UserCommandMapper;
import com.warzone.team08.CLI.models.UserCommand;
import com.warzone.team08.CLI.services.RequestService;
import com.warzone.team08.UserInterfaceMiddleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 * This class represents the command line user interface where:
 * User asked to enter the text, interpret the text, and take action accordingly.
 * </pre>
 * The motivation behind this is to let different interfaces use with the same instance of Virtual Machine.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class CommandLineInterface implements Runnable, UserInterfaceMiddleware {
    /**
     * Interprets user input text and converts it into the form which can be understood
     */
    private static UserCommandMapper d_UserCommandMapper;

    /**
     * Keeps track of user interaction
     */
    private UserInteractionState d_interactionState = UserInteractionState.WAIT;

    /**
     * Gets the state of user interaction
     *
     * @return Value of the state of user interaction
     */
    public UserInteractionState getInteractionState() {
        return d_interactionState;
    }

    /**
     * Sets the state of user interaction whether: 1. the program is waiting for user to enter the input 2. User is
     * waiting for the execution to finish
     *
     * @param p_interactionState the state of user interaction
     */
    public void setInteractionState(UserInteractionState p_interactionState) {
        this.d_interactionState = p_interactionState;
    }

    /**
     * Represents the created thread of this class implementing Runnable interface
     */
    public final Thread d_thread;

    /**
     * Stores the user command to be processed in <code>run</code> method in hthe next iteration of this thread.
     */
    private final Queue<UserCommand> d_userCommandQueue = new LinkedList<>();

    /**
     * Service to take action for the user command.l
     */
    private final RequestService d_requestService;

    /**
     * <code>ReentrantLock</code> to lock the shared data.
     */
    ReentrantLock d_reentrantLock = new ReentrantLock();

    /**
     * Instance of <code>Application</code>.
     */
    private final Application d_application;

    /**
     * Default constructor. Initializes thread, <code>UserCommandMapper</code>, and <code>RequestService</code>.
     *
     * @param p_application Instance of the class which has the entry point <code>main</code> method.
     */
    public CommandLineInterface(Application p_application) {
        d_application = p_application;
        d_thread = new Thread(this);
        d_UserCommandMapper = new UserCommandMapper();
        d_requestService = new RequestService();
    }

    /**
     * <code>InputStream</code> channel for user to provide the input.
     *
     * @param p_inputStream Value of any <code>InputStream</code>
     */
    public void setIn(InputStream p_inputStream) {
        System.setIn(p_inputStream);
    }

    /**
     * Waits for user input from command line interface
     *
     * @return Value of user text command
     * @throws IOException If any interruption occurs while waiting for user
     */
    private String waitForUserInput() throws IOException {
        // Enter data using BufferReader
        BufferedReader l_bufferedReader =
                new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine and trim the input string
        return l_bufferedReader.readLine().trim();
    }

    /**
     * Method to be called when thread steps
     */
    public void run() {
        while (d_application.isRunning()) {
            try {
                d_reentrantLock.lockInterruptibly();
                try {
                    if (this.getInteractionState() == UserInteractionState.WAIT) {
                        try {
                            String l_userInput = this.waitForUserInput();

                            // Takes user input and interprets it for further processing
                            UserCommand l_userCommand = d_UserCommandMapper.toUserCommand(l_userInput);

                            this.setInteractionState(UserInteractionState.IN_PROGRESS);
                            // Takes action according to command instructions.
                            d_requestService.takeAction(l_userCommand);

                            if (this.getInteractionState() == UserInteractionState.IN_PROGRESS)
                                this.setInteractionState(UserInteractionState.WAIT);
                        } catch (IOException p_e) {
                        }
                    }
                    if (!d_userCommandQueue.isEmpty()) {
                        UserCommand l_userCommand = d_userCommandQueue.poll();
                        // Takes action according to command instructions.
                        d_requestService.takeAction(l_userCommand);
                    }
                } catch (InvalidArgumentException | InvalidCommandException p_exception) {
                    // Show exception message
                    // In Graphical User Interface, we can show different modals respective to the exception.
                    System.out.println(p_exception.getMessage());

                    if (this.getInteractionState() == UserInteractionState.IN_PROGRESS) {
                        this.setInteractionState(UserInteractionState.WAIT);
                    }
                } finally {
                    d_reentrantLock.unlock();
                }
            } catch (InterruptedException l_ignored) {
            }
        }
    }

    /**
     * Asks the user for command-input. Represented exchange messages in string for communication.
     *
     * @param p_message Message to be shown before asking for input.
     * @return Value of interpreted user command;
     */
    @Override
    public String askForUserInput(String p_message) {
        try {
            d_reentrantLock.lockInterruptibly();
            try {
                // Print the message if any.
                if (p_message != null && !p_message.isEmpty()) {
                    System.out.println(p_message);
                }
                ObjectMapper mapper = new ObjectMapper();
                UserCommand l_userCommand = d_UserCommandMapper.toUserCommand(this.waitForUserInput());
                if (l_userCommand.getPredefinedUserCommand().isOrderCommand()) {
                    return mapper.writeValueAsString(l_userCommand);
                } else if (l_userCommand.getPredefinedUserCommand().isGameEngineCommand()) {
                    d_userCommandQueue.add(l_userCommand);
                } else {
                    this.stderr("Invalid command!");
                }
                return "";
            } catch (IOException p_ioException) {
                return "";
            } catch (InvalidCommandException | InvalidArgumentException p_exception) {
                this.stderr(p_exception.getMessage());
                return "";
            }
        } catch (InterruptedException p_e) {
            return "";
        } finally {
            d_reentrantLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param p_message Represents the message.
     */
    public void stdout(String p_message) {
        if (p_message.equals("GAME_ENGINE_STARTED")) {
            d_thread.interrupt();
            this.setInteractionState(UserInteractionState.GAME_ENGINE);
        } else if (p_message.equals("GAME_ENGINE_STOPPED")) {
            d_thread.interrupt();
            this.setInteractionState(UserInteractionState.WAIT);
        } else {
            System.out.println(p_message);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param p_message Represents the error message.
     */
    public void stderr(String p_message) {
        System.out.println(p_message);
    }
}
