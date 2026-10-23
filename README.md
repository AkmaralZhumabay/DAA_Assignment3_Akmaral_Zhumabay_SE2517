# DAA Assignment 3 — Graph Algorithms

**Student:** Akmaral Zhumabay  
**Group:** SE-2517  
**Course:** Design and Analysis of Algorithms  

## Project Description

This project implements graph algorithms for a city network and job scheduler using Java.

The road network uses Minimum Spanning Tree algorithms to connect districts with minimum total cost. The job scheduler uses graph traversal algorithms to identify strongly connected components, detect cycles, determine execution order, and calculate shortest paths.

## Implemented Algorithms

| Algorithm | Purpose |
|---|---|
| DSU | Manages connected components using path compression and union by size |
| Kruskal | Finds a minimum spanning tree using sorted edges and DSU |
| Prim | Finds a minimum spanning tree using a priority queue |
| Kosaraju | Finds strongly connected components using iterative DFS |
| Kahn | Produces a topological order and detects cycles |
| DAG Shortest Paths | Finds shortest paths using topological order, including negative weights |

All algorithms use the same adjacency-list Graph class.

## Technologies

- Java 17+
- Maven
- JUnit 5
- Python (pandas, matplotlib, NumPy) for generating plots

## Project Structure

```text
src/main/java/com/daa/
    algorithms/     Graph algorithms
    graph/          Graph representation and file reader
    metrics/        Operation counters
    benchmark/      Graph generator and benchmark

src/test/java/com/daa/
    DSUTest.java
    MSTTest.java
    SCCTest.java
    KahnTest.java
    DagShortestPathTest.java

data/               Sample graph input files
results/
    results.csv     Benchmark results
    plots/          Three PNG charts

generate_plots.py   Plot generation script
REPORT.md           Complexity analysis and results
README.md           Project instructions
```

## Build and Run

Open the project in IntelliJ IDEA with Java 17 or newer.

To compile the project:

```bash
mvn clean compile
```

To run all JUnit 5 tests:

```bash
mvn test
```

To run the benchmark:

```bash
mvn exec:java -Dexec.mainClass="com.daa.benchmark.Benchmark"
```

Alternatively, open `Benchmark.java` in IntelliJ and run its `main()` method.

The benchmark creates `results/results.csv` with 50 rows, using five graph sizes and two densities.

To regenerate the plots:

```bash
py -m pip install pandas matplotlib numpy
py generate_plots.py
```

## Input Format

Graph files are stored in `data/`.

The first line contains:

```text
n m directed|undirected
```

Each following line contains:

```text
u v weight
```

Example:

```text
5 3 undirected
0 1 4
1 2 2
3 4 5
```

Vertices are numbered from 0 to n−1.

## Benchmark

The benchmark uses `Random(42)` to generate reproducible graphs.

Graph sizes: 1,000; 3,162; 10,000; 31,623; 100,000.

Densities: sparse (4n edges) and dense (20n edges).

For each configuration, the program runs Kruskal, Prim, Kosaraju, Kahn, and DAG shortest paths.

Execution time is measured as the median of five runs. Operation counters include DSU operations, edge scans, weight comparisons, and maximum DFS stack depth.

The benchmark automatically verifies MST agreement, valid topological ordering, exact edge-scan counts, and all reference results.

## Testing

The project includes JUnit 5 tests for the graph algorithms.

The latest completed Maven test run reported:

- 14 tests executed
- 0 failures
- 0 errors
- 0 skipped tests

The benchmark also passed all ten graph configurations and reproduced the required reference values.

## Results

The generated results are saved in `results/results.csv`.

Three plots are included:

- `results/plots/operations.png`
- `results/plots/scans_per_edge.png`
- `results/plots/kruskal_edge_share.png`

The detailed complexity analysis, correctness arguments, and benchmark conclusions are available in `REPORT.md`.

## AI Use Acknowledgement

ChatGPT was used to assist with planning the project structure, explaining graph algorithms, reviewing code, preparing tests and writing documentation.

The implementation was run and checked in IntelliJ IDEA, and the benchmark results were compared against the assignment's reference values.

## Git Repository

Main branch: `main`

Feature branches: `feature/dsu`, `feature/mst`, `feature/scc`, `feature/paths`, `feature/metrics`

Release tag: `v1.0`

GitHub repository: https://github.com/AkmaralZhumabay/DAA_Assignment3_Akmaral_Zhumabay_SE2517