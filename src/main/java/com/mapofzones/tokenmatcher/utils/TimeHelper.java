package com.mapofzones.tokenmatcher.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TimeHelper {

    public static LocalDateTime millisToLocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
    }

}
