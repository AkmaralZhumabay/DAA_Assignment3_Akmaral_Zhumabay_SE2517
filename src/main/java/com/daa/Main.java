package com.daa;

import com.daa.algorithms.Kruskal;
import com.daa.algorithms.Prim;
import com.daa.graph.Graph;
import com.daa.graph.GraphReader;

public class Main {

    public static void main(String[] args) throws Exception {

        Graph graph = GraphReader.read("data/disconnected.txt");

        Kruskal.Result kruskal = Kruskal.run(graph);
        Prim.Result prim = Prim.run(graph);

        System.out.println(" Minimum Spanning Tree ");

        System.out.println("Kruskal weight: "
                + kruskal.totalWeight);

        System.out.println("Prim weight: "
                + prim.totalWeight);

        System.out.println("Kruskal edges: "
                + kruskal.edgeCount);

        System.out.println("Prim edges: "
                + prim.edgeCount);

        System.out.println("Components: "
                + kruskal.components);

        if (kruskal.totalWeight != prim.totalWeight) {
            throw new IllegalStateException(
                    "MST weights do not match!");
        }

        System.out.println("MST verification passed!");
    }
}
