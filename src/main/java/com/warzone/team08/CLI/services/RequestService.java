package com.warzone.team08.CLI.services;

import com.warzone.team08.CLI.constants.specifications.CommandSpecification;
import com.warzone.team08.CLI.exceptions.InvalidArgumentException;
import com.warzone.team08.CLI.exceptions.InvalidCommandException;
import com.warzone.team08.CLI.models.UserCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * This class decides the <code>API</code> to call based on interpreted the <code>UserCommand</code>.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class RequestService {
    private final String CLASS_PATH = "com.warzone.team08.VM.VirtualMachine";

    /**
     * Calls the suggested function of the mapped class. Uses the mappings of the command text to the class name. Here,
     * the suggested function is the same as the argument key provided by the user, and its values are the argument to
     * be passed to the function call. (Mappings for this can be created if the both are not the same?)
     *
     * @param p_userCommand Value of the object (instance) which is equivalent to the user text input.
     * @throws InvalidArgumentException Raised if the command not found.
     * @throws InvalidCommandException  Raised if the argument key(s) not found or its value(s) are not provided.
     */
    public void takeAction(UserCommand p_userCommand) throws InvalidArgumentException, InvalidCommandException {
        // Gets the mapped class of the command and calls its function; With arguments, if any.
        try {
            Class<?> l_class = Class.forName(CLASS_PATH);
            // If the command does not have any argument keys
            Method l_getGamePhase = l_class.getMethod("getGamePhase");
            Object l_gamePhase = l_getGamePhase.invoke(null);
            if (p_userCommand.getPredefinedUserCommand().getCommandSpecification()
                    != CommandSpecification.NEEDS_KEYS) {
                // Call the default method of the instance with the arguments
                this.handleMethodInvocation(l_gamePhase, p_userCommand.getPredefinedUserCommand().getGamePhaseMethodName(), null, p_userCommand.getCommandValues());
            } else if (p_userCommand.getPredefinedUserCommand().getCommandSpecification()
                    == CommandSpecification.NEEDS_KEYS &&
                    p_userCommand.getPredefinedUserCommand().getNumOfKeysOrValues() != 1) {
                // Call method will the list of keys and its values.
                this.handleMethodInvocation(l_gamePhase, p_userCommand.getPredefinedUserCommand().getGamePhaseMethodName(), null, p_userCommand.getUserArguments());
            } else {
                // If the method needs each key as a separate function call.
                // Iterate over the user arguments
                for (Map<String, List<String>> entryMap : p_userCommand.getUserArguments()) {
                    for (Map.Entry<String, List<String>> entry : entryMap.entrySet()) {
                        String l_argKey = entry.getKey();
                        List<String> p_argValues = entry.getValue();

                        // If the argument key does not have any value, it will send empty list
                        this.handleMethodInvocation(l_gamePhase, p_userCommand.getPredefinedUserCommand().getGamePhaseMethodName(), l_argKey, p_argValues);
                    }
                }
            }
        } catch (NullPointerException | ClassNotFoundException |
                IllegalAccessException p_e) {
            throw new InvalidCommandException("Command not found!");
        } catch (NoSuchMethodException |
                InvocationTargetException p_e) {
            // If belongs to VMException
            if (isVMException(p_e.getCause())) {
                throw new InvalidCommandException(p_e.getCause().getMessage());
            } else {
                throw new InvalidArgumentException("Unrecognized argument and/or its values");
            }
        }

    }

    /**
     * This method handles the actual call of the specific method at runtime. Prepares two arrays of Class and Object
     * for the argument type and the value respectively. Uses these arrays to find the method and call the method with
     * the value(s).
     *
     * @param p_object    An instance of the object which specifies the class for being called method
     * @param p_argKey    Value of the argument key passed by the user; This represents also the method name of the
     *                    object.
     * @param p_argValues Value of the argument values or Value of the list of argument key - value pair; This
     *                    represents the arguments to be passed to the method call.
     * @throws NoSuchMethodException     Raised if the method doesn't exist at the object.
     * @throws InvocationTargetException Raised if invoked a method that throws an underlying exception itself.
     * @throws IllegalAccessException    Raised if the method is not accessible by the caller.
     */
    private void handleMethodInvocation(Object p_object,
                                        String p_methodName,
                                        String p_argKey,
                                        List<?> p_argValues)
            throws NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {
        Object l_responseObject;
        if (p_argKey == null || p_argKey.isEmpty()) {
            // Get the reference and call the method with arguments
            Method l_methodReference = p_object.getClass().getMethod(p_methodName, List.class);
            l_responseObject = l_methodReference.invoke(p_object, p_argValues);
        } else {
            // Create two arrays:
            // 1. For type of the value
            // 2. For the values
            Class<?>[] l_valueTypes = new Class[]{String.class, List.class};

            // Get the reference and call the method with arguments
            Method l_methodReference = p_object.getClass().getMethod(p_methodName, l_valueTypes);
            l_responseObject = l_methodReference.invoke(p_object, p_argKey, p_argValues);
        }
        try {
            String l_responseValue = (String) l_responseObject;
            if (!l_responseValue.isEmpty()) {
                System.out.println(l_responseValue);
            }
        } catch (
                Exception l_ignored) {
            // Ignore exception if the object does not represent the String value.
        }
    }

    /**
     * This method handles the actual call of the specific method at runtime. Prepares two arrays of Class and Object
     * for the argument type and the value respectively. Uses these arrays to find the method and call the method with
     * the value(s).
     *
     * @param p_object        An instance of the object which specifies the class for being called method.
     * @param p_userArguments Value of the list of argument key - value pair; This represents the arguments to be passed
     *                        to the method call.
     * @throws NoSuchMethodException     Raised if the method doesn't exist at the object.
     * @throws InvocationTargetException Raised if invoked a method that throws an underlying exception itself.
     * @throws IllegalAccessException    Raised if the method is not accessible by the caller.
     */
    private void handleMethodInvocation(Object p_object,
                                        String p_methodName,
                                        List<Map<String, List<String>>> p_userArguments)
            throws NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {
        // Create two arrays:
        // 1. For type of the value
        // 2. For the values
        Class<?>[] l_valueTypes = new Class[]{List.class};

        // Get the reference and call the method with arguments
        Method l_methodReference = p_object.getClass().getMethod(p_methodName, l_valueTypes);
        Object l_responseObject = l_methodReference.invoke(p_object, p_userArguments);
        try {
            String l_responseValue = (String) l_responseObject;
            if (!l_responseValue.isEmpty()) {
                System.out.println(l_responseValue);
            }
        } catch (
                Exception l_ignored) {
            // Ignore exception if the object does not represent the String value.
        }
    }

    /**
     * Checks whether the class of the cause or its superclass is <code>VMException</code>.
     *
     * @param p_cause Cause to be checked.
     * @return True if the type of cause is <code>VMException</code>.
     */
    private boolean isVMException(Throwable p_cause) {
        return p_cause != null && p_cause.getClass() != null &&
                (p_cause.getClass().getName().equals("com.warzone.team08.VM.exceptions.VMException") || (
                        p_cause.getClass().getSuperclass() != null &&
                                p_cause.getClass().getSuperclass().getName().equals("com.warzone.team08.VM.exceptions.VMException")));
    }
}
