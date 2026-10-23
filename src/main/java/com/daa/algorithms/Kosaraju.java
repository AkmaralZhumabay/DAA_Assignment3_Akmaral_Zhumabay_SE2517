package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kosaraju {

    public static class Result {
        public int components;
        public int[] componentId;
        public long edgeScans;
        public int maxDepth;
    }

    private static class Frame {
        int vertex;
        int nextEdge;

        Frame(int vertex) {
            this.vertex = vertex;
            this.nextEdge = 0;
        }
    }

    public static Result run(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kosaraju requires a directed graph");
        }

        int n = graph.size();
        Result result = new Result();

        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();

        // First DFS pass: finishing order
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            ArrayDeque<Frame> stack = new ArrayDeque<>();
            stack.push(new Frame(start));
            visited[start] = true;

            while (!stack.isEmpty()) {
                result.maxDepth = Math.max(
                        result.maxDepth, stack.size());

                Frame current = stack.peek();
                List<Edge> neighbors =
                        graph.neighbors(current.vertex);

                if (current.nextEdge < neighbors.size()) {
                    Edge edge = neighbors.get(current.nextEdge++);
                    result.edgeScans++;

                    if (!visited[edge.to]) {
                        visited[edge.to] = true;
                        stack.push(new Frame(edge.to));
                    }
                } else {
                    order.add(current.vertex);
                    stack.pop();
                }
            }
        }

        // Reverse the graph
        Graph reversed = new Graph(n, true);

        for (Edge edge : graph.getEdges()) {
            reversed.addEdge(edge.to, edge.from, edge.weight);
        }

        // Second DFS pass
        Arrays.fill(visited, false);
        result.componentId = new int[n];

        for (int i = order.size() - 1; i >= 0; i--) {
            int start = order.get(i);

            if (visited[start]) {
                continue;
            }

            ArrayDeque<Integer> stack = new ArrayDeque<>();
            stack.push(start);
            visited[start] = true;

            while (!stack.isEmpty()) {
                result.maxDepth = Math.max(
                        result.maxDepth, stack.size());

                int v = stack.pop();
                result.componentId[v] = result.components;

                for (Edge edge : reversed.neighbors(v)) {
                    result.edgeScans++;

                    if (!visited[edge.to]) {
                        visited[edge.to] = true;
                        stack.push(edge.to);
                    }
                }
            }

            result.components++;
        }

        return result;
    }
}
