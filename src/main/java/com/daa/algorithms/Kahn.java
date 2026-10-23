package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Kahn {

    public static class Result {
        public List<Integer> order = new ArrayList<>();
        public boolean hasCycle;
        public long edgeScans;
    }

    public static Result run(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kahn requires a directed graph");
        }

        int n = graph.size();
        int[] indegree = new int[n];

        // Calculate in-degrees
        for (Edge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();

        // Start with vertices that have no incoming edges
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        Result result = new Result();

        while (!queue.isEmpty()) {
            int v = queue.poll();
            result.order.add(v);

            for (Edge edge : graph.neighbors(v)) {
                result.edgeScans++;
                indegree[edge.to]--;

                if (indegree[edge.to] == 0) {
                    queue.add(edge.to);
                }
            }
        }

        result.hasCycle = result.order.size() != n;

        if (result.hasCycle) {
            result.order.clear();
        }

        return result;
    }

    public static boolean isValidOrder(
            Graph graph, List<Integer> order) {

        int n = graph.size();

        if (order.size() != n) {
            return false;
        }

        int[] position = new int[n];

        java.util.Arrays.fill(position, -1);

        for (int i = 0; i < n; i++) {
            int v = order.get(i);

            if (v < 0 || v >= n || position[v] != -1) {
                return false;
            }

            position[v] = i;
        }

        for (Edge edge : graph.getEdges()) {
            if (position[edge.from] >= position[edge.to]) {
                return false;
            }
        }

        return true;
    }
}
