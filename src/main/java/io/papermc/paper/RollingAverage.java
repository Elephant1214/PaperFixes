package io.papermc.paper;

import static me.elephant1214.paperfixes.TickManager.NANOS_PER_SECOND;
import static me.elephant1214.paperfixes.TickManager.TARGET_TPS;

public class RollingAverage {
    private final int size;
    private long time;
    private java.math.BigDecimal total;
    private int index = 0;
    private final java.math.BigDecimal[] samples;
    private final long[] times;

    public RollingAverage(int size) {
        this.size = size;
        this.time = size * NANOS_PER_SECOND;
        this.total = dec(TARGET_TPS).multiply(dec(NANOS_PER_SECOND)).multiply(dec(size));
        this.samples = new java.math.BigDecimal[size];
        this.times = new long[size];
        for (int i = 0; i < size; i++) {
            this.samples[i] = dec(TARGET_TPS);
            this.times[i] = NANOS_PER_SECOND;
        }
    }

    private static java.math.BigDecimal dec(long t) {
        return new java.math.BigDecimal(t);
    }
    public void add(java.math.BigDecimal x, long t) {
        time -= times[index];
        total = total.subtract(samples[index].multiply(dec(times[index])));
        samples[index] = x;
        times[index] = t;
        time += t;
        total = total.add(x.multiply(dec(t)));
        if (++index == size) {
            index = 0;
        }
    }

    public double getAverage() {
        return total.divide(dec(time), 30, java.math.RoundingMode.HALF_UP).doubleValue();
    }
}
