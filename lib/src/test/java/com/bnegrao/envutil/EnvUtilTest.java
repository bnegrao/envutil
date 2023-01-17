package com.bnegrao.envutil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvUtilTest {

    @BeforeEach
    void reloadEnv() {
        EnvUtil.resetEnvironment();
    }

    @Test
    void whenAVariableIsSetEnv_ItCanBeObtainedWithGetEnv() {
        EnvUtil.setEnv("MYVAR", "MYVAL");
        assertEquals("MYVAL", EnvUtil.getEnv("MYVAR"));
    }

    @Test
    void whenAVariableIsSet_GetEnvReturnsExistingValueInsteadOfDefaultValue() {
        String varName = "MYVAR";
        String value = "VALUE1";
        String defaultValue = "SomeDefaultValueDifferentFromOriginalValue";
        EnvUtil.setEnv(varName, value);

        assertEquals(value, EnvUtil.getEnv(varName, defaultValue));
    }

    @Test
    void whenAVariableIsNotSet_GetEnvShouldReturnTheGivenDefaultValue() {
        String varName = "MYVAR";
        String defaultValue = "SomeDefaultValue";

        assertEquals(defaultValue, EnvUtil.getEnv(varName, defaultValue));
    }

    @Test
    void whenExistingVariableIsOverridden_NewValueCanBeObtainedWithGetEnv() {
        // this test relies on the $HOME environment variable, if it doesn't exist
        // the test just returns success
        if (System.getenv("HOME") == null){
            return;
        }

        String oldValue = EnvUtil.getEnv("HOME");
        assertNotNull(oldValue);

        String newValue = "/home/user/a7e47f82-96b4-11ed-a8fc-0242ac120002";
        EnvUtil.overrideEnv("HOME", newValue);

        assertEquals(newValue, EnvUtil.getEnv("HOME"));
    }

    @Test
    void whenOverridingANonexistentVariable_AssertExceptionIsThrown() {
        assertThrows(EnvironmentVariableNotFoundException.class, () -> EnvUtil.overrideEnv("NonExistentVariable", "newValue"));
    }

    @Test
    void whenAVariableIsEmpty_AssertGetEnvThrowsException() {
        String varName = "emptyVar";
        EnvUtil.setEnv(varName, "");

        assertThrows (EnvironmentVariableIsEmptyException.class, () -> EnvUtil.getEnv(varName));
    }

    @Test
    void whenGetEnvToNonexistentVariable_AssertExceptionIsThrow() {
        assertThrows(EnvironmentVariableNotFoundException.class, () -> EnvUtil.getEnv("someNonexistentVar_d93r4dfdf93sdsdf"));
    }

    @Test
    void assertEnvUtilIsLoadedWithAllExistingEnvironmentVariables() {
        System.getenv().forEach((varName, value) -> assertEquals(value, EnvUtil.getEnv(varName)));
    }

    @Test
    void assertResetEnvironmentRemovesTestVariables() {
        // given
        String varName = "someVar";
        String value = "value1";
        EnvUtil.setEnv(varName, value);
        assertEquals(value, EnvUtil.getEnv(varName));

        // when
        EnvUtil.resetEnvironment();

        // then
        assertThrows(EnvironmentVariableNotFoundException.class, () -> EnvUtil.getEnv(varName));
    }

    @Test
    void assertPrintEnvReturnsCorrectNumberOfLines() {
        String[] lines = EnvUtil.printEnv().split("\n");

        assertEquals(System.getenv().size(), lines.length);
    }
}
