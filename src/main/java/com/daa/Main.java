package com.daa;

import com.daa.algorithms.DagShortestPath;
import com.daa.graph.Graph;
import com.daa.graph.GraphReader;

public class Main {

    public static void main(String[] args) throws Exception {

        Graph graph = GraphReader.read("data/cyclic.txt");

        DagShortestPath.Result result =
                DagShortestPath.run(graph);

        System.out.println(" DAG Shortest Paths ");

        for (int i = 0; i < graph.size(); i++) {
            System.out.println(
                    "Vertex " + i
                            + ": distance = " + result.distance[i]
                            + ", path = " + result.getPath(i)
            );
        }

        System.out.println("Sum of distances: "
                + result.sumDistances);

        System.out.println("Edge scans: "
                + result.edgeScans);
    }
}
