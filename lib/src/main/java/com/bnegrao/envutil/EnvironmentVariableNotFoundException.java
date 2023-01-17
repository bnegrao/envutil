package com.bnegrao.envutil;

public class EnvironmentVariableNotFoundException extends RuntimeException {

    /**
     * Thrown to indicate that an environment variable with the given name does not exist.
     * @param variableName variable name
     */
    public EnvironmentVariableNotFoundException(String variableName){
        super(String.format("Environment variable '%s' does not exist.", variableName));
    }
}
