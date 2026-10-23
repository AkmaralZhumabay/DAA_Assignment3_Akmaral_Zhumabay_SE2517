package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.PriorityQueue;

public class Prim {

    public static class Result {
        public long totalWeight;
        public int edgeCount;
        public int components;
        public long edgeScans;
        public long comparisons;
    }

    private static class Candidate {
        int vertex;
        int weight;

        Candidate(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    public static Result run(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Prim requires an undirected graph");
        }

        Result result = new Result();
        int n = graph.size();

        boolean[] visited = new boolean[n];

        PriorityQueue<Candidate> pq =
                new PriorityQueue<>((a, b) -> {
                    result.comparisons++;
                    return Integer.compare(a.weight, b.weight);
                });

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            result.components++;
            pq.add(new Candidate(start, 0));

            while (!pq.isEmpty()) {
                Candidate current = pq.poll();

                if (visited[current.vertex]) {
                    continue;
                }

                visited[current.vertex] = true;
                result.totalWeight += current.weight;

                if (current.vertex != start) {
                    result.edgeCount++;
                }

                for (Edge edge : graph.neighbors(current.vertex)) {
                    result.edgeScans++;

                    if (!visited[edge.to]) {
                        pq.add(new Candidate(edge.to, edge.weight));
                    }
                }
            }
        }

        return result;
    }
}
