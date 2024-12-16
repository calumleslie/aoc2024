package uk.zootm.aoc2024.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DirectedGraph<N, E> {
    private final Set<N> nodes;
    private final Table<N, N, E> edges;

    public DirectedGraph() {
        this(new HashSet<>(), HashBasedTable.create());
    }

    private DirectedGraph(Set<N> nodes, Table<N, N, E> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public void addNode(N name) {
        nodes.add(name);
    }

    public void addEdge(N from, N to, E edge) {
        nodes.add(from);
        nodes.add(to);
        edges.put(from, to, edge);
    }

    public Map<N, E> outgoing(N name) {
        return edges.row(name);
    }

    public Set<N> outgoingNodes(N name) {
        return outgoing(name).keySet();
    }

    public Map<N, E> incoming(N name) {
        return edges.column(name);
    }

    public Set<N> incomingNodes(N name) {
        return incoming(name).keySet();
    }

    public boolean hasCycles() {
        // It's possible to avoid duplicating work here but maybe that's not worthwhile :D
        return nodes.stream().anyMatch(this::containsCycle);
    }

    private boolean containsCycle(N node) {
        Set<N> seen = new LinkedHashSet<>();
        Deque<N> toCheck = new LinkedList<>();
        toCheck.addAll(outgoingNodes(node));

        N current;
        while ((current = toCheck.poll()) != null) {
            if (current.equals(node)) {
                return true;
            }
            seen.add(current);
            toCheck.addAll(outgoingNodes(current));
        }
        return false;
    }

    @Override
    public DirectedGraph<N, E> clone() {
        return new DirectedGraph<>(new HashSet<>(nodes), HashBasedTable.create(edges));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DirectedGraph other) {
            return Objects.equals(nodes, other.nodes) && Objects.equals(edges, other.edges);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }

    @Override
    public String toString() {
        return String.format("DirectedGraph(nodes=%s,edges=%s)", nodes, edges);
    }
}
