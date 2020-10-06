package com.azure.backend.error;

public class SampleAzureException extends Exception {

    public SampleAzureException() {
    }

    public SampleAzureException(String message) {
        super(message);
    }
}