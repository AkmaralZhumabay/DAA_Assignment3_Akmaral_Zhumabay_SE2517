package com.daa.algorithms;

public class DSU {
    private int[] parent;
    private int[] size;
    private int count;

    private long findCalls;
    private long successfulUnions;

    public DSU(int n) {
        makeSet(n);
    }

    public void makeSet(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid size");
        }

        parent = new int[n];
        size = new int[n];
        count = n;

        findCalls = 0;
        successfulUnions = 0;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        findCalls++;

        int root = x;

        while (parent[root] != root) {
            root = parent[root];
        }

        while (parent[x] != x) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }

        return root;
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        if (size[rootX] < size[rootY]) {
            int temp = rootX;
            rootX = rootY;
            rootY = temp;
        }

        parent[rootY] = rootX;
        size[rootX] += size[rootY];

        count--;
        successfulUnions++;

        return true;
    }

    public int components() {
        return count;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getSuccessfulUnions() {
        return successfulUnions;
    }

    public long getDsuOps() {
        return findCalls + successfulUnions;
    }
}
