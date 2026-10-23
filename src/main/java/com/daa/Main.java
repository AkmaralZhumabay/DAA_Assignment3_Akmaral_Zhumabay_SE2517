package com.daa;

import com.daa.algorithms.Kahn;
import com.daa.algorithms.Kosaraju;
import com.daa.graph.Graph;
import com.daa.graph.GraphReader;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println(" Kosaraju ");

        Graph cyclic = GraphReader.read("data/cyclic.txt");

        Kosaraju.Result scc = Kosaraju.run(cyclic);

        System.out.println("SCC count: " + scc.components);
        System.out.println("Edge scans: " + scc.edgeScans);
        System.out.println("Max depth: " + scc.maxDepth);

        for (int i = 0; i < cyclic.size(); i++) {
            System.out.println("Vertex " + i
                    + " -> Component " + scc.componentId[i]);
        }

        System.out.println();
        System.out.println(" Kahn ");

        Graph dag = GraphReader.read("data/dag.txt");

        Kahn.Result topo = Kahn.run(dag);

        System.out.println("Topological order: " + topo.order);
        System.out.println("Has cycle: " + topo.hasCycle);
        System.out.println("Valid order: "
                + Kahn.isValidOrder(dag, topo.order));
        System.out.println("Edge scans: " + topo.edgeScans);

        System.out.println();
        System.out.println(" Cycle Detection ");

        Kahn.Result cyclicResult = Kahn.run(cyclic);

        System.out.println("Has cycle: " + cyclicResult.hasCycle);
    }
}
