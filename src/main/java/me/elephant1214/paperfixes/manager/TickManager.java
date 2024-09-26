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
    public static final TickManager INSTANCE = new TickManager();
    public static int currentTick = 0;
    public final BigDecimal tpsBase = new BigDecimal("1E9").multiply(new BigDecimal(TARGET_TPS));
    public final RollingAverage tps5s = new RollingAverage(5);
    public final RollingAverage tps1 = new RollingAverage(60);
    public final RollingAverage tps5 = new RollingAverage(60 * 5);
    public final RollingAverage tps15 = new RollingAverage(60 * 15);

    public double[] getTPS() {
        return new double[]{
                tps5s.getAverage(),
                tps1.getAverage(),
                tps5.getAverage(),
                tps15.getAverage()
        };
    }
}
