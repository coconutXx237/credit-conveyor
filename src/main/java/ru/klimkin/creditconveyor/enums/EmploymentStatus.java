package ru.klimkin.creditconveyor.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EmploymentStatus {
    UNEMPLOYED,
    SELF_EMPLOYED,
    EMPLOYED,
    BUSINESS_OWNER;

    @JsonCreator
    public static EmploymentStatus getEmploymentStatus(String s) {
        for (EmploymentStatus e : EmploymentStatus.values()) {
            if (e.toString().equalsIgnoreCase(s)){
                return e;
            }
        }
        throw new IllegalArgumentException(s + " is not Employment status");
    }
}
