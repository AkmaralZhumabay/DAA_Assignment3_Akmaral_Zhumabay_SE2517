package com.daa.metrics;

public class Metrics {

    public long dsuOps;
    public long edgeScans;
    public long comparisons;
    public int maxDepth;

    public Metrics(long dsuOps, long edgeScans,
                   long comparisons, int maxDepth) {
        this.dsuOps = dsuOps;
        this.edgeScans = edgeScans;
        this.comparisons = comparisons;
        this.maxDepth = maxDepth;
    }
}
