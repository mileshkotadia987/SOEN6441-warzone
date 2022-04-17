package com.warzone.team08.CLI.constants.specifications;

/**
 * Specification types for command to run
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public enum CommandSpecification {
    /**
     * Command can run alone; without any argument provided.
     */
    CAN_RUN_ALONE,
    /**
     * Command can run without argument key but needs the value(s) to run. For this specification, command layout will
     * also need to use <code>ArgumentSpecification</code> to specify how many arguments it needs to run.
     */
    CAN_RUN_ALONE_WITH_VALUE,
    /**
     * Command needs one or more argument key and its values to run. For this specification, command layout will also
     * need to use <code>ArgumentSpecification</code> to specify how many arguments it needs to run.
     */
    NEEDS_KEYS
}
