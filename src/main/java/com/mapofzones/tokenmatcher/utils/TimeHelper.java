package com.mapofzones.tokenmatcher.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TimeHelper {

    public static LocalDateTime millisToLocalDateTime(String time) {
        return millisToLocalDateTime(Long.parseLong(time));
    }

    public static LocalDateTime millisToLocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
    }

    public static LocalDateTime nowAroundHours() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
    }

    public static LocalDateTime nowAroundDays() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    }

}
