package com.daa;

import com.daa.algorithms.DagShortestPath;
import com.daa.graph.Edge;
import com.daa.graph.Graph;
import com.daa.graph.GraphReader;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DagShortestPathTest {

    private long[] bellmanFord(Graph graph) {
        int n = graph.size();
        long[] dist = new long[n];
        int[] indegree = new int[n];

        long inf = Long.MAX_VALUE / 4;
        Arrays.fill(dist, inf);

        for (Edge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        // Virtual super-source
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                dist[i] = 0;
            }
        }

        for (int i = 0; i < n - 1; i++) {
            for (Edge edge : graph.getEdges()) {
                if (dist[edge.from] != inf) {
                    long candidate =
                            dist[edge.from] + edge.weight;

                    if (candidate < dist[edge.to]) {
                        dist[edge.to] = candidate;
                    }
                }
            }
        }

        return dist;
    }

    @Test
    void randomDagsWithNegativeWeights() {
        Random rnd = new Random(42);

        for (int test = 0; test < 100; test++) {
            int n = 2 + rnd.nextInt(29);
            Graph graph = new Graph(n, true);

            // Edges go forward, so cycles are impossible
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (rnd.nextDouble() < 0.2) {
                        int weight = rnd.nextInt(21) - 10;
                        graph.addEdge(i, j, weight);
                    }
                }
            }

            DagShortestPath.Result result =
                    DagShortestPath.run(graph);

            long[] expected = bellmanFord(graph);

            assertArrayEquals(expected, result.distance);
            assertEquals(graph.edgeCount(),
                    result.edgeScans);
        }
    }

    @Test
    void restoreNegativePath() throws Exception {
        Graph graph =
                GraphReader.read("data/negative_dag.txt");

        DagShortestPath.Result result =
                DagShortestPath.run(graph);

        assertEquals(-6, result.sumDistances);
        assertEquals(
                java.util.List.of(1, 2, 3, 4),
                result.getPath(4));
    }

    @Test
    void rejectCycle() throws Exception {
        Graph graph = GraphReader.read("data/cyclic.txt");

        assertThrows(IllegalArgumentException.class,
                () -> DagShortestPath.run(graph));
    }
}
