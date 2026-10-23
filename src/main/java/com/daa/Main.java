package com.daa;

import com.daa.algorithms.DSU;
import com.daa.graph.Graph;
import com.daa.graph.GraphReader;

public class Main {
    public static void main(String[] args) throws Exception {

        Graph graph = GraphReader.read("data/connected.txt");

        System.out.println("Vertices: " + graph.size());
        System.out.println("Edges: " + graph.edgeCount());

        DSU dsu = new DSU(5);

        dsu.union(0, 1);
        dsu.union(1, 2);

        System.out.println("Components: " + dsu.components());
        System.out.println("Same group: "
                + (dsu.find(0) == dsu.find(2)));
        System.out.println("DSU operations: " + dsu.getDsuOps());
    }
}
