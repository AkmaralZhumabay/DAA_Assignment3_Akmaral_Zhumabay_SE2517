package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {

    public static class Result {
        public long totalWeight;
        public int edgeCount;
        public int components;
        public long edgeScans;
        public long comparisons;
        public long dsuOps;
    }

    public static Result run(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kruskal requires an undirected graph");
        }

        Result result = new Result();
        DSU dsu = new DSU(graph.size());

        List<Edge> edges = new ArrayList<>(graph.getEdges());

        edges.sort((a, b) -> {
            result.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        for (Edge edge : edges) {
            if (dsu.components() == 1) {
                break;
            }

            result.edgeScans++;

            if (dsu.find(edge.from) != dsu.find(edge.to)) {
                dsu.union(edge.from, edge.to);
                result.totalWeight += edge.weight;
                result.edgeCount++;
            }
        }

        result.components = dsu.components();
        result.dsuOps = dsu.getDsuOps();

        return result;
    }
}
