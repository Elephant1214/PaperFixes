package me.elephant1214.paperfixes.util;

import java.util.concurrent.TimeUnit;

public final class TickConstants {
    public static final long TICKS_PER_SECOND = 20L;
    public static final long NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1L);
    public static final long NANOS_PER_MILLI = TimeUnit.MILLISECONDS.toNanos(1L);
    public static final long OVERLOADED_THRESHOLD = 30L * NANOS_PER_SECOND / TICKS_PER_SECOND;
    public static final long OVERLOADED_WARNING_INTERVAL = 10L * NANOS_PER_SECOND;
    public static final long NANOS_PER_TICK = NANOS_PER_SECOND / TICKS_PER_SECOND;
}
