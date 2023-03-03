package me.egg82.gpt.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
    private TimeUtil() { }

    private static final Pattern timePattern = Pattern.compile("^(\\d+)\\s*(\\w+)$");

    public static @Nullable Time getTime(@NotNull String input) {
        Matcher timeMatcher = timePattern.matcher(input);
        if (!timeMatcher.matches()) {
            return null;
        }

        long time = Long.parseLong(timeMatcher.group(1));

        String unit = timeMatcher.group(2);
        return switch (unit) {
            case "n", "ns", "nanos", "nano", "nanoseconds", "nanosecond" -> new Time(time, TimeUnit.NANOSECONDS);
            case "micros", "micro", "microseconds", "microsecond" -> new Time(time, TimeUnit.MICROSECONDS);
            case "ms", "millis", "milli", "milliseconds", "millisecond" -> new Time(time, TimeUnit.MILLISECONDS);
            case "s", "secs", "sec", "seconds", "second" -> new Time(time, TimeUnit.SECONDS);
            case "m", "mins", "min", "minutes", "minute" -> new Time(time, TimeUnit.MINUTES);
            case "h", "hrs", "hr", "hours", "hour" -> new Time(time, TimeUnit.HOURS);
            case "d", "days", "day" -> new Time(time, TimeUnit.DAYS);
            default -> null;
        };
    }

    public static @Nullable String getTimeString(@NotNull Time time) {
        if (time.unit == TimeUnit.DAYS) {
            return time.time + (time.time == 1 ? "day" : "days");
        } else if (time.unit == TimeUnit.HOURS) {
            return time.time + (time.time == 1 ? "hour" : "hours");
        } else if (time.unit == TimeUnit.MINUTES) {
            return time.time + (time.time == 1 ? "minute" : "minutes");
        } else if (time.unit == TimeUnit.SECONDS) {
            return time.time + (time.time == 1 ? "second" : "seconds");
        } else if (time.unit == TimeUnit.MILLISECONDS) {
            return time.time + "ms";
        } else if (time.unit == TimeUnit.MICROSECONDS) {
            return time.time + (time.time == 1 ? "micro" : "micros");
        } else if (time.unit == TimeUnit.NANOSECONDS) {
            return time.time + "ns";
        }
        return null;
    }

    public static class Time {
        private final long time;
        private final TimeUnit unit;

        private final int hc;

        public Time(long time, @NotNull TimeUnit unit) {
            this.time = time;
            this.unit = unit;

            this.hc = Objects.hash(time, unit);
        }

        public long getTime() { return time; }

        public @NotNull TimeUnit getUnit() { return unit; }

        public long getMillis() { return unit.toMillis(time); }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Time)) {
                return false;
            }
            Time time1 = (Time) o;
            return time == time1.time &&
                    unit == time1.unit;
        }

        @Override
        public int hashCode() { return hc; }
    }
}
