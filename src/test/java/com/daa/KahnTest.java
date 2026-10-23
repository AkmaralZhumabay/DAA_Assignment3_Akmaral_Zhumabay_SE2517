package com.daa;

import com.daa.algorithms.Kahn;
import com.daa.graph.Graph;
import com.daa.graph.GraphReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KahnTest {

    @Test
    void validDagOrder() throws Exception {
        Graph graph = GraphReader.read("data/dag.txt");

        Kahn.Result result = Kahn.run(graph);

        assertFalse(result.hasCycle);
        assertEquals(graph.size(), result.order.size());
        assertTrue(Kahn.isValidOrder(graph, result.order));
        assertEquals(graph.edgeCount(), result.edgeScans);
    }

    @Test
    void detectCycle() throws Exception {
        Graph graph = GraphReader.read("data/cyclic.txt");

        Kahn.Result result = Kahn.run(graph);

        assertTrue(result.hasCycle);
        assertTrue(result.order.isEmpty());
    }

    @Test
    void isolatedVertices() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 1);

        Kahn.Result result = Kahn.run(graph);

        assertFalse(result.hasCycle);
        assertEquals(5, result.order.size());
        assertTrue(Kahn.isValidOrder(graph, result.order));
    }
}
