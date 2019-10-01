package de.craxy.pitemp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;

/**
 * Provides methods to parse arguments of the start parameter
 * Start Parameter: -argument=value
 *
 * @author Craxy
 */
public class ArgsParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArgsParser.class);

    private HashMap<String, String> arguments = new HashMap<>();

    /**
     * Creates a new Argument Parser which helps to evaluate the arguments with which the program has been started.
     *
     * @param args The argument Array of the program.
     */
    public ArgsParser(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("-")) {
                String[] split = arg.replaceFirst("-", "").split("=");
                arguments.put(split[0], split.length > 1 ? split[1] : null);
            }
        }
    }

    /**
     * Checks if a given argument exists.
     *
     * @param argument The name of the argument
     * @return true if the argument exists, false if it doesn't
     */
    public boolean hasArgument(String argument) {
        return arguments.containsKey(argument);
    }

    /**
     * Gives the according argument as a String.
     *
     * @param argument the argument of which the value should be returned.
     * @return an optional that is either empty or contains the value for the given argument.
     */
    public Optional<String> getStringValue(String argument) {
        if (hasArgument(argument)) {
            return Optional.of(arguments.get(argument));
        }
        return Optional.empty();
    }

    /**
     * Gives the according argument as a String.
     *
     * @param argument the argument of which the value should be returned.
     * @return an optional that is either empty or contains the integer value for the given argument.
     */
    public Optional<Integer> getIntValue(String argument) {
        if (hasArgument(argument)) {
            try {
                return Optional.of(Integer.parseInt(arguments.get(argument)));
            } catch (NumberFormatException e) {
                LOGGER.error("Error while converting value for argument '" + argument + "' into an integer.", e);
            }
        }
        return Optional.empty();
    }

    /**
     * Gives the according argument as a String.
     *
     * @param argument the argument of which the value should be returned.
     * @return an optional that is either empty or contains the boolean value for the given argument.
     */
    public Optional<Boolean> getBooleanValue(String argument) {
        if (hasArgument(argument)) {
            try {
                return Optional.of(Boolean.parseBoolean(arguments.get(argument)));
            } catch (NumberFormatException e) {
                LOGGER.error("Error while converting value for argument '" + argument + "' into a boolean.", e);
            }
        }
        return Optional.empty();
    }

}

