package me.elephant1214.paperfixes.manager;

import io.papermc.paper.RollingAverage;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class TickManager {
    public static final int TARGET_TPS = 20;
    public static final long NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1L);
    public static final long NANOS_PER_MILLI = TimeUnit.MILLISECONDS.toNanos(1L);
    public static final long OVERLOADED_THRESHOLD = 30L * NANOS_PER_SECOND / 20L;
    public static final long OVERLOADED_WARNING_INTERVAL = 10L * NANOS_PER_SECOND;
    public static final long NANOS_PER_TICK = NANOS_PER_SECOND / 20L;

    public static final BigDecimal TPS_BASE = new BigDecimal("1E9").multiply(new BigDecimal(TARGET_TPS));
    public static final RollingAverage TPS_5S = new RollingAverage(5);
    public static final RollingAverage TPS_1 = new RollingAverage(60);
    public static final RollingAverage TPS_5 = new RollingAverage(60 * 5);
    public static final RollingAverage TPS_15 = new RollingAverage(60 * 15);

    public static int currentTick = 0;

    public static double[] getTPS() {
        return new double[]{
                TPS_5S.getAverage(),
                TPS_1.getAverage(),
                TPS_5.getAverage(),
                TPS_15.getAverage()
        };
    }

    public static long getNanos() {
        return System.nanoTime();
    }

    public static long getMillis() {
        return getNanos() / 1000000L;
    }
}
