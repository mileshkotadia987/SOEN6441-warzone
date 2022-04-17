package com.warzone.team08.VM.constants.interfaces;

/**
 * This class represents the various engines which stores the runtime information of players and the game.
 *
 * <pre>
 *  To get the instance, use the static `getInstance` method of the inheriting class.
 *  The inheriting class only should create one instance per game.
 * </pre>
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public interface Engine {
    /**
     * Initialise the engine and all its data members.
     */
    void initialise();

    /**
     * Stop and exit from all the threads  of <code>Engine</code>.
     */
    void shutdown();
}
