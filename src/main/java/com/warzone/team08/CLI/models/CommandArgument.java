package com.warzone.team08.CLI.models;

import com.warzone.team08.CLI.constants.specifications.ArgumentSpecification;

import java.util.Objects;

/**
 * Predefined structure for the command arguments and its value(s) It also provides the specification to: 1. Validate
 * the number of values provided by the user 2. Validate each value with Regex
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class CommandArgument {
    private String d_argumentKey;
    private int d_numOfValues;
    private ArgumentSpecification d_specification;

    /**
     * Parameterized constructor to initialize variables.
     *
     * @param p_argumentKey   Value of command argument key.
     * @param p_numOfValues   Values of number of required values.
     * @param p_specification Value of specification to be used for this command argument.
     */
    public CommandArgument(String p_argumentKey, int p_numOfValues, ArgumentSpecification p_specification) {
        d_argumentKey = p_argumentKey;
        d_numOfValues = p_numOfValues;
        d_specification = p_specification;
    }

    /**
     * Gets the argument key.
     *
     * @return Value of the argument key.
     */
    public String getArgumentKey() {
        return d_argumentKey;
    }

    /**
     * Sets the argument key.
     *
     * @param p_argumentKey Value of the argument key.
     */
    public void setArgumentKey(String p_argumentKey) {
        d_argumentKey = p_argumentKey;
    }

    /**
     * Gets the list of values for this command argument.
     *
     * @return Value of the list of command argument.
     */
    public int getNumOfValues() {
        return d_numOfValues;
    }

    /**
     * Sets the list of values for this command argument.
     *
     * @param p_numOfValues Value of the list of command argument.
     */
    public void setNumOfValues(int p_numOfValues) {
        d_numOfValues = p_numOfValues;
    }

    /**
     * Gets specification for this command argument.
     *
     * @return Value of specification for this command argument.
     */
    public ArgumentSpecification getSpecification() {
        return d_specification;
    }

    /**
     * Sets specification for this command argument.
     *
     * @param p_specification Value of specification for this command argument.
     */
    public void setSpecification(ArgumentSpecification p_specification) {
        d_specification = p_specification;
    }

    /**
     * Checks if the command arguments are equal.
     *
     * @param l_p_o The command argument to be checked with.
     * @return True if both are the same.
     */
    @Override
    public boolean equals(Object l_p_o) {
        if (this == l_p_o) return true;
        if (l_p_o == null || getClass() != l_p_o.getClass()) return false;
        CommandArgument l_that = (CommandArgument) l_p_o;
        return d_numOfValues == l_that.d_numOfValues &&
                Objects.equals(d_argumentKey, l_that.d_argumentKey) &&
                d_specification == l_that.d_specification;
    }
}
