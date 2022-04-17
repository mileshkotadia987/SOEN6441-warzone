package com.warzone.team08.VM.exceptions;


import com.warzone.team08.VM.VirtualMachine;

/**
 * Catches the exception occurred in the Thread execution.
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    /**
     * {@inheritDoc}
     */
    public void uncaughtException(Thread t, Throwable e) {
        VirtualMachine.getInstance().stderr("Something went wrong!");
    }
}