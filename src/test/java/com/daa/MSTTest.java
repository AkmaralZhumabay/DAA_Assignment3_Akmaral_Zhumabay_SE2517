package com.daa;

import com.daa.algorithms.Kruskal;
import com.daa.algorithms.Prim;
import com.daa.graph.Graph;
import com.daa.graph.GraphReader;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    @Test
    void randomConnectedGraphs() {
        Random rnd = new Random(42);

        for (int test = 0; test < 100; test++) {
            int n = 2 + rnd.nextInt(49);
            Graph graph = new Graph(n, false);

            // First make the graph connected
            for (int i = 1; i < n; i++) {
                int parent = rnd.nextInt(i);
                int weight = 1 + rnd.nextInt(100);

                graph.addEdge(parent, i, weight);
            }

            // Add random edges
            for (int i = 0; i < n * 3; i++) {
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                int weight = 1 + rnd.nextInt(100);

                graph.addEdge(u, v, weight);
            }

            Kruskal.Result k = Kruskal.run(graph);
            Prim.Result p = Prim.run(graph);

            assertEquals(k.totalWeight, p.totalWeight);
            assertEquals(n - 1, k.edgeCount);
            assertEquals(n - 1, p.edgeCount);
            assertEquals(1, k.components);
            assertEquals(1, p.components);
        }
    }

    @Test
    void disconnectedGraph() throws Exception {
        Graph graph =
                GraphReader.read("data/disconnected.txt");

        Kruskal.Result k = Kruskal.run(graph);
        Prim.Result p = Prim.run(graph);

        assertEquals(11, k.totalWeight);
        assertEquals(k.totalWeight, p.totalWeight);
        assertEquals(3, k.components);
        assertEquals(3, p.components);
        assertEquals(3, k.edgeCount);
    }

    @Test
    void selfLoopsAndParallelEdges() {
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 0, -100);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);

        Kruskal.Result k = Kruskal.run(graph);
        Prim.Result p = Prim.run(graph);

        assertEquals(5, k.totalWeight);
        assertEquals(5, p.totalWeight);
        assertEquals(2, k.edgeCount);
        assertEquals(2, p.edgeCount);
    }
}
