package com.daa.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> edges;

    public Graph(int n, boolean directed) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid vertex count");
        }

        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>();
        this.edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        if (u < 0 || u >= n || v < 0 || v >= n) {
            throw new IllegalArgumentException("Invalid vertex");
        }

        Edge edge = new Edge(u, v, weight);
        adj.get(u).add(edge);
        edges.add(edge);

        if (!directed) {
            adj.get(v).add(new Edge(v, u, weight));
        }
    }

    public int size() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> neighbors(int v) {
        return adj.get(v);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int edgeCount() {
        return edges.size();
    }
}
