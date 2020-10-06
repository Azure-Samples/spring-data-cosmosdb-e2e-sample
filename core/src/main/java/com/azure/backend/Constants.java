package com.azure.backend;

public class Constants {

    public static final String KEY_VAULT_NAME = "KEYVAULT_NAME";

    public static final String KEYVAULT_NAME_ERROR_MSG = "KEYVAULT_NAME value is '{0}'"
            + " which does not meet the criteria must be 3-24 characters long, begin with a "
            + "character, may contain alphanumeric or hyphen, no repeating hyphens, and end with "
            + "alphanumeric.  Check ${KEYVAULT_NAME} in your environment variables.";

    private Constants() {
        // private constructor to hide pulbic constructor
    }
}
