package com.daa;

import com.daa.algorithms.*;
import com.daa.benchmark.GraphGenerator;
import com.daa.graph.Graph;

public class Main {

    public static void main(String[] args) {

        int n = 1000;
        int m = 4000;

        Graph mstGraph = GraphGenerator.undirected(n, m);
        Graph directedGraph = GraphGenerator.directed(n, m);
        Graph dagGraph = GraphGenerator.dag(n, m);

        Kruskal.Result kruskal = Kruskal.run(mstGraph);
        Prim.Result prim = Prim.run(mstGraph);
        Kosaraju.Result scc = Kosaraju.run(directedGraph);
        Kahn.Result topo = Kahn.run(dagGraph);
        DagShortestPath.Result paths =
                DagShortestPath.run(dagGraph);

        System.out.println(" Generator Verification ");

        System.out.println("Kruskal MST: " + kruskal.totalWeight);
        System.out.println("Prim MST: " + prim.totalWeight);
        System.out.println("SCC count: " + scc.components);
        System.out.println("Topological order size: "
                + topo.order.size());
        System.out.println("DAG distance sum: "
                + paths.sumDistances);

        System.out.println("Kosaraju scans: " + scc.edgeScans);
        System.out.println("Kahn scans: " + topo.edgeScans);
        System.out.println("DAG scans: " + paths.edgeScans);

        if (kruskal.totalWeight != prim.totalWeight) {
            throw new IllegalStateException(
                    "MST algorithms disagree!");
        }

        if (!Kahn.isValidOrder(dagGraph, topo.order)) {
            throw new IllegalStateException(
                    "Invalid topological order!");
        }

        if (scc.edgeScans != 2L * m
                || topo.edgeScans != m
                || paths.edgeScans != m) {
            throw new IllegalStateException(
                    "Edge scan counters are incorrect!");
        }

        System.out.println("Basic verification passed!");
    }
}
