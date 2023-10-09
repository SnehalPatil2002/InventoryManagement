package com.mycompany.myapp.domain.enumeration;

/**
 * The Status enumeration.
 */
public enum Status {
    REQUESTED("Requested"),
    APPROVED("Approved"),
    CANCELED("Canceled"),
    RECEIVED("Received"),
    COMPLETED("Completed");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
