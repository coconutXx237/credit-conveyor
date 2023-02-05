package ru.klimkin.creditconveyor.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MaritalStatus {
    MARRIED,
    DIVORCED,
    SINGLE,
    WIDOW_WIDOWER;

    @JsonCreator
    public static MaritalStatus getMaritalStatus(String s) {
        for (MaritalStatus e : MaritalStatus.values()) {
            if (e.toString().equalsIgnoreCase(s)){
                return e;
            }
        }
        throw new IllegalArgumentException(s + " is not Gender");
    }
}
