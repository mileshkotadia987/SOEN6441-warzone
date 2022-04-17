package com.warzone.team08.CLI.constants.specifications;

/**
 * Specification types for the number of required values for the argument
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public enum ArgumentSpecification {
    /**
     * An argument should have a number of values equals to required
     */
    EQUAL,
    /**
     * An argument should have at least a minimum number of values from required
     */
    MIN,
    /**
     * An argument should have only a defined number of values
     */
    MAX
}
