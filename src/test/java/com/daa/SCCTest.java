package com.daa;

import com.daa.algorithms.Kosaraju;
import com.daa.graph.Edge;
import com.daa.graph.Graph;
import com.daa.graph.GraphReader;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    // Simple BFS used as an independent reference
    private boolean[] reachable(Graph graph, int start) {
        boolean[] visited = new boolean[graph.size()];
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int v = queue.poll();

            for (Edge edge : graph.neighbors(v)) {
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.add(edge.to);
                }
            }
        }

        return visited;
    }

    @Test
    void compareWithBruteForce() {
        Random rnd = new Random(42);

        for (int test = 0; test < 30; test++) {
            int n = 2 + rnd.nextInt(199);
            Graph graph = new Graph(n, true);

            int m = n * 3;

            for (int i = 0; i < m; i++) {
                graph.addEdge(
                        rnd.nextInt(n),
                        rnd.nextInt(n),
                        1);
            }

            Kosaraju.Result result = Kosaraju.run(graph);

            boolean[][] reach = new boolean[n][];

            for (int i = 0; i < n; i++) {
                reach[i] = reachable(graph, i);
            }

            int expectedComponents = 0;
            boolean[] assigned = new boolean[n];

            for (int i = 0; i < n; i++) {
                if (assigned[i]) {
                    continue;
                }

                expectedComponents++;

                for (int j = 0; j < n; j++) {
                    if (reach[i][j] && reach[j][i]) {
                        assigned[j] = true;
                    }
                }
            }

            assertEquals(expectedComponents,
                    result.components);

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    boolean sameScc =
                            reach[i][j] && reach[j][i];

                    assertEquals(sameScc,
                            result.componentId[i]
                                    == result.componentId[j]);
                }
            }

            assertEquals(2L * m, result.edgeScans);
        }
    }

    @Test
    void cyclicGraph() throws Exception {
        Graph graph = GraphReader.read("data/cyclic.txt");

        Kosaraju.Result result = Kosaraju.run(graph);

        assertEquals(3, result.components);
        assertEquals(result.componentId[0],
                result.componentId[1]);
        assertEquals(result.componentId[1],
                result.componentId[2]);
        assertNotEquals(result.componentId[2],
                result.componentId[3]);
    }

    @Test
    void stackSafety() {
        int n = 100000;
        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        Kosaraju.Result result =
                assertDoesNotThrow(() -> Kosaraju.run(graph));

        assertEquals(n, result.components);
        assertEquals(2L * (n - 1), result.edgeScans);
        assertTrue(result.maxDepth > 0);
    }
}
