package dev.klimkin.creditconveyor.dto;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE,
    FEMALE,
    NON_BINARY;

    @JsonCreator
    public static Gender getGender(String s) {
        for (Gender e : Gender.values()) {
            if (e.toString().equalsIgnoreCase(s)){
                return e;
            }
        }
        throw new IllegalArgumentException(s + " is not Gender");
    }
}
