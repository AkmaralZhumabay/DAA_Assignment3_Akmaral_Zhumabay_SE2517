package com.daa.benchmark;

import com.daa.algorithms.*;
import com.daa.graph.Graph;
import com.daa.metrics.Metrics;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;

public class Benchmark {

    private static final int[] SIZES = {
            1000, 3162, 10000, 31623, 100000
    };

    private static final int[] DENSITIES = {4, 20};

    private static final long[] EXPECTED_MST = {
            147970, 30363,
            468911, 97589,
            1486139, 309550,
            4715515, 968517,
            15012094, 3056681
    };

    private static final int[] EXPECTED_SCC = {
            27, 1,
            137, 1,
            373, 1,
            1188, 1,
            3923, 1
    };

    private static final long[] EXPECTED_DAG = {
            396274, 252411,
            1271911, 723157,
            3941828, 2381342,
            12551891, 7376612,
            40096539, 23512087
    };

    private static class RunResult {
        long result;
        Metrics metrics;

        RunResult(long result, Metrics metrics) {
            this.result = result;
            this.metrics = metrics;
        }
    }

    private static RunResult runAlgorithm(
            String algorithm, Graph graph) {

        switch (algorithm) {
            case "Kruskal": {
                Kruskal.Result r = Kruskal.run(graph);
                return new RunResult(r.totalWeight,
                        new Metrics(r.dsuOps, r.edgeScans,
                                r.comparisons, 0));
            }

            case "Prim": {
                Prim.Result r = Prim.run(graph);
                return new RunResult(r.totalWeight,
                        new Metrics(0, r.edgeScans,
                                r.comparisons, 0));
            }

            case "Kosaraju": {
                Kosaraju.Result r = Kosaraju.run(graph);
                return new RunResult(r.components,
                        new Metrics(0, r.edgeScans,
                                0, r.maxDepth));
            }

            case "Kahn": {
                Kahn.Result r = Kahn.run(graph);

                if (r.hasCycle ||
                        !Kahn.isValidOrder(graph, r.order)) {
                    throw new IllegalStateException(
                            "Invalid topological order");
                }

                return new RunResult(r.order.size(),
                        new Metrics(0, r.edgeScans, 0, 0));
            }

            case "DagShortestPath": {
                DagShortestPath.Result r =
                        DagShortestPath.run(graph);

                return new RunResult(r.sumDistances,
                        new Metrics(0, r.edgeScans,
                                r.comparisons, 0));
            }

            default:
                throw new IllegalArgumentException(
                        "Unknown algorithm: " + algorithm);
        }
    }

    private static void check(
            String name, long actual, long expected) {

        if (actual != expected) {
            throw new IllegalStateException(
                    name + ": expected " + expected
                            + ", got " + actual);
        }
    }

    private static void writeRow(
            PrintWriter writer,
            String algorithm,
            String density,
            int n,
            int m,
            Graph graph,
            long expected) {

        double[] times = new double[5];
        RunResult first = null;

        for (int run = 0; run < 5; run++) {
            long start = System.nanoTime();

            RunResult current =
                    runAlgorithm(algorithm, graph);

            long end = System.nanoTime();

            times[run] = (end - start) / 1_000_000.0;

            check(algorithm, current.result, expected);

            if (first == null) {
                first = current;
            } else {
                Metrics a = first.metrics;
                Metrics b = current.metrics;

                if (a.dsuOps != b.dsuOps
                        || a.edgeScans != b.edgeScans
                        || a.comparisons != b.comparisons
                        || a.maxDepth != b.maxDepth) {
                    throw new IllegalStateException(
                            algorithm + " counters changed between runs");
                }
            }
        }

        Arrays.sort(times);
        double median = times[2];

        Metrics metrics = first.metrics;

        writer.printf(Locale.US,
                "%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d%n",
                algorithm,
                density,
                n,
                m,
                median,
                metrics.dsuOps,
                metrics.edgeScans,
                metrics.comparisons,
                metrics.maxDepth,
                first.result);
    }

    public static void main(String[] args) throws Exception {

        File directory = new File("results");

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException(
                    "Cannot create results directory");
        }

        try (PrintWriter writer =
                     new PrintWriter("results/results.csv")) {

            writer.println(
                    "algorithm,graph,n,m,time_ms,dsu_ops,"
                            + "edge_scans,comparisons,max_depth,result");

            int index = 0;

            for (int n : SIZES) {
                for (int density : DENSITIES) {

                    int m = n * density;
                    String label = density == 4
                            ? "sparse" : "dense";

                    System.out.println();
                    System.out.println(
                            "Graph: n=" + n + ", m=" + m);

                    // MST graph
                    Graph mstGraph =
                            GraphGenerator.undirected(n, m);

                    Kruskal.Result kruskal =
                            Kruskal.run(mstGraph);

                    Prim.Result prim =
                            Prim.run(mstGraph);

                    check("Kruskal MST", kruskal.totalWeight,
                            EXPECTED_MST[index]);

                    check("Prim MST", prim.totalWeight,
                            EXPECTED_MST[index]);

                    check("MST agreement", kruskal.totalWeight,
                            prim.totalWeight);

                    check("MST edge count", kruskal.edgeCount,
                            n - 1);

                    check("Prim edge count", prim.edgeCount,
                            n - 1);

                    check("Kruskal components",
                            kruskal.components, 1);

                    check("Prim components",
                            prim.components, 1);

                    writeRow(writer, "Kruskal", label,
                            n, m, mstGraph, EXPECTED_MST[index]);

                    writeRow(writer, "Prim", label,
                            n, m, mstGraph, EXPECTED_MST[index]);

                    System.out.println(
                            "MST weight: " + kruskal.totalWeight);

                    mstGraph = null;

                    // Directed graph for SCC
                    Graph directedGraph =
                            GraphGenerator.directed(n, m);

                    Kosaraju.Result scc =
                            Kosaraju.run(directedGraph);

                    check("SCC count", scc.components,
                            EXPECTED_SCC[index]);

                    check("Kosaraju scans", scc.edgeScans,
                            2L * m);

                    writeRow(writer, "Kosaraju", label,
                            n, m, directedGraph, EXPECTED_SCC[index]);

                    System.out.println(
                            "SCC count: " + scc.components);

                    directedGraph = null;

                    // DAG for topological order and shortest paths
                    Graph dagGraph =
                            GraphGenerator.dag(n, m);

                    Kahn.Result topo = Kahn.run(dagGraph);

                    if (topo.hasCycle ||
                            !Kahn.isValidOrder(dagGraph, topo.order)) {
                        throw new IllegalStateException(
                                "Invalid DAG order");
                    }

                    check("Kahn scans", topo.edgeScans, m);

                    DagShortestPath.Result paths =
                            DagShortestPath.run(dagGraph);

                    check("DAG distance sum", paths.sumDistances,
                            EXPECTED_DAG[index]);

                    check("DAG scans", paths.edgeScans, m);

                    writeRow(writer, "Kahn", label,
                            n, m, dagGraph, n);

                    writeRow(writer, "DagShortestPath", label,
                            n, m, dagGraph, EXPECTED_DAG[index]);

                    System.out.println(
                            "DAG distance sum: "
                                    + paths.sumDistances);

                    System.out.println("Verification passed!");

                    index++;
                }
            }
        }

        System.out.println();
        System.out.println("All 10 configurations passed!");
        System.out.println("50 rows saved to results/results.csv");
    }
}
