package com.mycompany.myapp.domain.enumeration;

/**
 * The ClientType enumeration.
 */
public enum ClientType {
    SUPPLIER("Supplier"),
    CONSUMER("Consumer"),
    BOTH("Both"),
    OTHER("Other");

    private final String value;

    ClientType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
