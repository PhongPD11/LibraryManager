package com.example.librarymanager.Commons;

import org.hibernate.mapping.Any;

public class Commons {
    public static Boolean isNullOrEmpty(String object){
        return object == null || object.isEmpty();
    }
}
