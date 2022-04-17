package com.warzone.team08.VM.logger;

/**
 * This abstract class defines the methods to implement Observer.
 *
 * @author MILESH
 */
public abstract class Observer {
    Observable d_observable;

    /**
     * Constructor to initialize the observable object.
     *
     * @param p_observable Observable object.
     */
    public Observer(Observable p_observable) {
        d_observable = p_observable;
        d_observable.attach(this);
    }

    /**
     * Abstract method of update method.
     *
     * @param p_observable_state Observable object.
     */
    public abstract void update(Observable p_observable_state);
}