package com.bnegrao.envutil;

public class EnvironmentVariableIsEmptyException extends RuntimeException {

    /**
     * Thrown to indicate that an environment variable exists, but its value is
     * an empty string
     * @param variableName variable name
     */
    public EnvironmentVariableIsEmptyException(String variableName) {
        super(String.format("Environment variable '%s' is empty", variableName));
    }
}
