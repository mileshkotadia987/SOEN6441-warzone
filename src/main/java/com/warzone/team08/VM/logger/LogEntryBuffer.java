package com.warzone.team08.VM.logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Observable to notify the observers about the changes happen during any actions.
 *
 * @author MILESH
 * @author Brijesh Lakkad
 */
public class LogEntryBuffer implements Observable {
    private final List<Observer> d_observerList;
    private String d_message;
    private String d_headCommand;
    private static LogEntryBuffer d_instance;

    /**
     * Constructor to initialize the data members
     */
    private LogEntryBuffer() {
        d_observerList = new ArrayList<>();
    }

    /**
     * Gets the single instance of the class.
     *
     * @return Value of the instance.
     */
    public static LogEntryBuffer getLogger() {
        if (d_instance == null) {
            d_instance = new LogEntryBuffer();
        }
        return d_instance;
    }

    /**
     * This method adds the observers to the list.
     *
     * @param p_observer Observer object
     */
    @Override
    public void attach(Observer p_observer) {
        d_observerList.add(p_observer);
    }

    /**
     * This method removes the observer from the list.
     *
     * @param p_observer Observer Object
     */
    @Override
    public void detach(Observer p_observer) {
        d_observerList.remove(p_observer);
    }

    /**
     * This method notifies all the observers whenever data changes.
     *
     * @param p_o Observable object
     */
    @Override
    public void notifyObservers(Observable p_o) {
        for (Observer l_observer : d_observerList) {
            l_observer.update(p_o);
        }
    }

    /**
     * Gets the message to store in file.
     *
     * @return Message to store
     */
    public String getMessage() {
        return String.format("---%s---\n%s\n", this.getHeadCommand(), d_message);
    }

    /**
     * Sets the message which will be stored in file.
     *
     * @param p_message message to store
     */
    public void setMessage(String p_message) {
        d_message = p_message;
    }

    /**
     * This method will be called whenever there will be change in any services.
     *
     * @param p_headCommand head command name.
     * @param p_message     message to save in file.
     */
    public void dataChanged(String p_headCommand, String p_message) {
        d_headCommand = p_headCommand;
        d_message = p_message;
        notifyObservers(this);
    }

    /**
     * Gets the name of head command.
     *
     * @return Head command name.
     */
    public String getHeadCommand() {
        return d_headCommand;
    }

    /**
     * Sets the head command name.
     *
     * @param p_headCommand Head command name.
     */
    public void setHeadCommand(String p_headCommand) {
        d_headCommand = p_headCommand;
    }
}