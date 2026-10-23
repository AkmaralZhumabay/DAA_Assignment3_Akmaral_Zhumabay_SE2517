import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# Read benchmark results
df = pd.read_csv("results/results.csv")

# Create output folder
os.makedirs("results/plots", exist_ok=True)

algorithms = [
    "Kruskal", "Prim", "Kosaraju",
    "Kahn", "DagShortestPath"
]

densities = ["sparse", "dense"]

# Plot 1: Operations vs n
plt.figure(figsize=(11, 7))

for algorithm in algorithms:
    for density in densities:
        data = df[
            (df["algorithm"] == algorithm)
            & (df["graph"] == density)
            ].sort_values("n")

        # Select a meaningful operation counter
        if algorithm == "Kruskal":
            operations = data["comparisons"] + data["dsu_ops"]
        elif algorithm == "Prim":
            operations = data["comparisons"] + data["edge_scans"]
        else:
            operations = data["edge_scans"]

        plt.plot(
            data["n"], operations,
            marker="o",
            linestyle="-" if density == "sparse" else "--",
            label=f"{algorithm} ({density})"
        )

plt.xscale("log")
plt.yscale("log")
plt.xlabel("Number of vertices (n)")
plt.ylabel("Counted operations")
plt.title("Operations vs Graph Size")
plt.grid(True, which="both", alpha=0.3)
plt.legend(fontsize=8)
plt.tight_layout()
plt.savefig("results/plots/operations.png", dpi=200)
plt.close()

# Plot 2: Scans per edge
plt.figure(figsize=(10, 6))

for algorithm in ["Kosaraju", "Kahn", "DagShortestPath"]:
    for density in densities:
        data = df[
            (df["algorithm"] == algorithm)
            & (df["graph"] == density)
            ].sort_values("n")

        ratio = data["edge_scans"] / data["m"]

        plt.plot(
            data["n"], ratio,
            marker="o",
            linestyle="-" if density == "sparse" else "--",
            label=f"{algorithm} ({density})"
        )

plt.xscale("log")
plt.xlabel("Number of vertices (n)")
plt.ylabel("Edge scans / m")
plt.title("Scans per Edge")
plt.grid(True, alpha=0.3)
plt.legend()
plt.tight_layout()
plt.savefig("results/plots/scans_per_edge.png", dpi=200)
plt.close()

# Plot 3: Kruskal edge share
plt.figure(figsize=(10, 6))

for density in densities:
    data = df[
        (df["algorithm"] == "Kruskal")
        & (df["graph"] == density)
        ].sort_values("n")

    ratio = data["edge_scans"] / data["m"]

    plt.plot(
        data["n"], ratio,
        marker="o",
        label=density.capitalize()
    )

plt.xscale("log")
plt.xlabel("Number of vertices (n)")
plt.ylabel("Fraction of edges examined")
plt.title("Share of Edges Examined by Kruskal")
plt.grid(True, alpha=0.3)
plt.legend()
plt.tight_layout()
plt.savefig("results/plots/kruskal_edge_share.png", dpi=200)
plt.close()

# Calculate slopes for the report
print("\nLog-log slopes:")

for algorithm in algorithms:
    for density in densities:
        data = df[
            (df["algorithm"] == algorithm)
            & (df["graph"] == density)
            ].sort_values("n")

        if algorithm == "Kruskal":
            operations = data["comparisons"] + data["dsu_ops"]
        elif algorithm == "Prim":
            operations = data["comparisons"] + data["edge_scans"]
        else:
            operations = data["edge_scans"]

        slope = np.polyfit(
            np.log(data["n"]),
            np.log(operations),
            1
        )[0]

        print(f"{algorithm} ({density}): {slope:.3f}")

print("\nThree plots saved in results/plots/")
