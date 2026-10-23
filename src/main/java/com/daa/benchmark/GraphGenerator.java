package com.daa.benchmark;

import com.daa.graph.Graph;

import java.util.Random;

public class GraphGenerator {

    private static final int SEED = 42;

    // Shuffle an array using Fisher-Yates
    private static void shuffle(int[] array, Random rnd) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);

            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private static int[] permutation(int n, Random rnd) {
        int[] perm = new int[n];

        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }

        shuffle(perm, rnd);
        return perm;
    }

    // Undirected connected graph for MST
    public static Graph undirected(int n, int m) {
        if (n < 1 || m < n - 1) {
            throw new IllegalArgumentException(
                    "Not enough edges for a connected graph");
        }

        Random rnd = new Random(SEED);
        Graph graph = new Graph(n, false);

        int[] perm = permutation(n, rnd);

        // First create a random spanning tree
        for (int i = 1; i < n; i++) {
            int u = perm[rnd.nextInt(i)];
            int v = perm[i];
            int weight = 1 + rnd.nextInt(1000);

            graph.addEdge(u, v, weight);
        }

        // Add remaining random edges
        for (int i = n - 1; i < m; i++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);

            while (v == u) {
                v = rnd.nextInt(n);
            }

            int weight = 1 + rnd.nextInt(1000);
            graph.addEdge(u, v, weight);
        }

        return graph;
    }

    // Directed graph for SCC
    public static Graph directed(int n, int m) {
        Random rnd = new Random(SEED);
        Graph graph = new Graph(n, true);

        for (int i = 0; i < m; i++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);

            while (v == u) {
                v = rnd.nextInt(n);
            }

            int weight = 1 + rnd.nextInt(1000);
            graph.addEdge(u, v, weight);
        }

        return graph;
    }

    // Directed acyclic graph for Kahn and shortest paths
    public static Graph dag(int n, int m) {
        Random rnd = new Random(SEED);
        Graph graph = new Graph(n, true);

        int[] rank = permutation(n, rnd);

        for (int i = 0; i < m; i++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);

            while (v == u) {
                v = rnd.nextInt(n);
            }

            // Every edge must go from lower to higher rank
            if (rank[u] > rank[v]) {
                int temp = u;
                u = v;
                v = temp;
            }

            int weight = 1 + rnd.nextInt(1000);
            graph.addEdge(u, v, weight);
        }

        return graph;
    }
}
