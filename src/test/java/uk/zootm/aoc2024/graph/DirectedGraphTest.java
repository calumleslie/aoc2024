package uk.zootm.aoc2024.graph;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectedGraphTest {
    @Test
    public void hasCycles_noCycles() {
        SimpleDirectedGraph<Integer> graph = new SimpleDirectedGraph<>();
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(4, 5);

        graph.addEdge(10, 11);
        graph.addEdge(11, 12);

        assertThat(graph.hasCycles()).isFalse();
    }

    @Test
    public void hasCycles_withCycles() {
        SimpleDirectedGraph<Integer> graph = new SimpleDirectedGraph<>();
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(4, 5);
        graph.addEdge(3, 1); // Cycle

        graph.addEdge(10, 11);
        graph.addEdge(11, 12);

        assertThat(graph.hasCycles()).isTrue();
    }
}
