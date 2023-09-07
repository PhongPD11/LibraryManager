package com.example.librarymanager.Commons;

public class Commons {
    public static Boolean isNullOrEmpty(String object) {
        if (object == null) {
            return true;
        } else return object.isEmpty() || object.trim().isEmpty();
    }
}
