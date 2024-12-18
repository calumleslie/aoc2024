package uk.zootm.aoc2024.graph;

import com.google.common.base.Preconditions;

/**
 * Same as directedGraph but no edge metadata
 *
 * @param <N>
 */
public class SimpleDirectedGraph<N> extends DirectedGraph<N, SimpleDirectedGraph.NoValue> {
    private static final NoValue VALUE = new NoValue();

    // Convenience method for when one is not using edge metadata
    public void addEdge(N from, N to) {
        addEdge(from, to, VALUE);
    }

    @Override
    public void addEdge(N from, N to, NoValue edge) {
        // With apologies to Liskov
        Preconditions.checkArgument(edge == VALUE, "This graph does not store edge metadata");
        super.addEdge(from, to, edge);
    }

    public static class NoValue {
        private NoValue() {
        }

        @Override
        public String toString() {
            return "(none)";
        }
    }
}
