// Return current UTC time
package com.example.fintech.util;

import java.time.Instant;

public final class TimeUtils {

    private TimeUtils() {}

    public static Instant now() {
        return Instant.now();
    }
}

