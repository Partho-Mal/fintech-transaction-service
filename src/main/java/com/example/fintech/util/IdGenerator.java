//Generate UUID
//Return as String
//No randomness tricks

package com.example.fintech.util;

import java.util.UUID;

public final class IdGenerator {

    private IdGenerator() {}

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}

