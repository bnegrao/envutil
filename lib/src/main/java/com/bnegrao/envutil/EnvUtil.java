package com.bnegrao.envutil;

import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this utility is to facilitate testing of code that relies on environment variables to run properly.
 * When this class is loaded it saves all the existing environment variables in a modifiable map allowing
 * you to set/update variables when writing unit tests.
 * <br>
 * The methods {@code getEnv(varName)} and {@code getEnv(varName, "")} are meant to be used by regular code when trying
 * to access the environment variables.
 * <br>
 * The methods {@code overrideEnv(varName, value)}, {@code setEnv(varName, value)}
 * and {@code resetEnvironment()} should be used  by unit tests that need to manipulate the environment
 * for test cases.
 */
public class EnvUtil {

    /**
     * a modifiable map to represent the environment variables
     */
    private static Map<String, String> envMap = loadEnvironment();

    /**
     * Returns a map with all the environment variables available in the runtime process
     */
    private static Map<String, String> loadEnvironment() {
        HashMap<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        return environment;
    }

    private EnvUtil(){}

    /**
     * Returns the value of the given environment variable. Throws exceptions to indicate when the variable doesn't exist or is empty.
     * If your variable can be empty, use {@code getEnv(varName, "")} instead.
     * @param variableName variable name
     * @throws EnvironmentVariableIsEmptyException if the value of the variable is an empty string.
     * @throws EnvironmentVariableNotFoundException if the variable is not set
     */
    public static String getEnv(String variableName) throws EnvironmentVariableIsEmptyException, EnvironmentVariableNotFoundException {
        if (envMap.containsKey(variableName)){
            String value = envMap.get(variableName);
            if (value.length() == 0){
                throw new EnvironmentVariableIsEmptyException(variableName);
            }
            return value;
        }
        throw new EnvironmentVariableNotFoundException(variableName);
    }

    /**
     * Returns the value of the given variable or returns the defaultValue if the variable does not exist.
     * @param variableName variable name
     * @param defaultValue value to be returned if the variable does not exist
     */
    public static String getEnv(String variableName, String defaultValue) {
        if (envMap.containsKey(variableName)){
            return envMap.get(variableName);
        }
        return defaultValue;
    }

    /**
     * sets a new variable or overrides an existing variable with a new value
     * This is meant to be used in unit tests.
     * @param variableName name of the variable to be created or overridden
     * @param value new value
     */
    public static void setEnv(String variableName, String value){
        envMap.put(variableName, value);
    }

    /**
     * Overrides the value of an environment variable that should exist, if the variable doesn't exist an exception is
     * thrown.
     * This is meant to be used in unit tests.
     * @param variableName name of an existing variable that is being overridden
     * @param value new value
     * @throws EnvironmentVariableNotFoundException thrown in a variable with the given name does not exist
     */
    public static void overrideEnv(String variableName, String value) throws EnvironmentVariableNotFoundException {
        if (!envMap.containsKey(variableName)){
            throw new EnvironmentVariableNotFoundException(variableName);
        }
        envMap.put(variableName, value);
    }


    /**
     * Resets internal state and reloads the environment variables from the runtime process.
     * This is meant to be used in unit tests, to reset the environment back to the original state
     * between each test.
     */
    public static void resetEnvironment() {
        envMap = loadEnvironment();
    }

    /**
     * Returns a string showing all the variable/value pairs currently stored in the internal map
     * formatted as "VARIABLE_NAME:VARIABLE_VALUE\n";
     */
    public static String printEnv() {
        StringBuilder envStr = new StringBuilder();
        envMap.forEach((k, v) -> envStr.append(k + ":" + v + "\n"));
        return envStr.toString();
    }
}
