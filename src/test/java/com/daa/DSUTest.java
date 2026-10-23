package com.daa;

import com.daa.algorithms.DSU;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DSUTest {

    @Test
    void randomUnions() {
        int n = 100;
        DSU dsu = new DSU(n);
        int[] group = new int[n];

        for (int i = 0; i < n; i++) {
            group[i] = i;
        }

        Random rnd = new Random(42);

        for (int step = 0; step < 500; step++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);

            int oldGroup = group[b];
            int newGroup = group[a];

            dsu.union(a, b);

            for (int i = 0; i < n; i++) {
                if (group[i] == oldGroup) {
                    group[i] = newGroup;
                }
            }

            int components = 0;

            for (int i = 0; i < n; i++) {
                boolean first = true;

                for (int j = 0; j < i; j++) {
                    if (group[i] == group[j]) {
                        first = false;
                        break;
                    }
                }

                if (first) {
                    components++;
                }
            }

            assertEquals(components, dsu.components());

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    assertEquals(group[i] == group[j],
                            dsu.find(i) == dsu.find(j));
                }
            }
        }
    }

    @Test
    void repeatedUnion() {
        DSU dsu = new DSU(3);

        assertTrue(dsu.union(0, 1));
        assertFalse(dsu.union(0, 1));
        assertEquals(2, dsu.components());
        assertEquals(1, dsu.getSuccessfulUnions());
    }
}
