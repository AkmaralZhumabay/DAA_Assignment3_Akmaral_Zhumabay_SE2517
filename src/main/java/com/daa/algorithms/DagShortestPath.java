package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DagShortestPath {

    private static final long INF = Long.MAX_VALUE / 4;

    public static class Result {
        public long[] distance;
        public int[] parent;
        public long edgeScans;
        public long comparisons;
        public long sumDistances;

        public List<Integer> getPath(int target) {
            if (target < 0 || target >= distance.length) {
                throw new IllegalArgumentException("Invalid vertex");
            }

            List<Integer> path = new ArrayList<>();

            if (distance[target] == INF) {
                return path;
            }

            int current = target;

            while (current != -1) {
                path.add(current);
                current = parent[current];
            }

            Collections.reverse(path);
            return path;
        }
    }

    public static Result run(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "DAG shortest paths require a directed graph");
        }

        Kahn.Result topo = Kahn.run(graph);

        if (topo.hasCycle) {
            throw new IllegalArgumentException(
                    "Shortest paths require a DAG");
        }

        int n = graph.size();

        Result result = new Result();
        result.distance = new long[n];
        result.parent = new int[n];

        Arrays.fill(result.distance, INF);
        Arrays.fill(result.parent, -1);

        int[] indegree = new int[n];

        for (Edge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        // Every vertex with in-degree 0 is a source
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                result.distance[i] = 0;
            }
        }

        // Relax edges in topological order
        for (int v : topo.order) {
            for (Edge edge : graph.neighbors(v)) {
                result.edgeScans++;

                if (result.distance[v] == INF) {
                    continue;
                }

                long newDistance =
                        result.distance[v] + edge.weight;

                result.comparisons++;

                if (newDistance < result.distance[edge.to]) {
                    result.distance[edge.to] = newDistance;
                    result.parent[edge.to] = v;
                }
            }
        }

        for (long distance : result.distance) {
            if (distance != INF) {
                result.sumDistances += distance;
            }
        }

        return result;
    }
}
