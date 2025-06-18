package me.elephant1214.paperfixes.util;

public final class TickConstants {
    public static final long NANOS_PER_SECOND = 1_000_000_000L; // 1 s
    public static final long NANOS_PER_TICK = NANOS_PER_SECOND / 20L; // 50 ms
    public static final long NANOS_PER_MILLI = 1_000_000L; // 1 ms
    public static final long OVERLOADED_THRESHOLD = 2_500_000_000L; // 2.5 s
    public static final long OVERLOADED_WARNING_INTERVAL = 15_000_000_000L; // 15 s
}
