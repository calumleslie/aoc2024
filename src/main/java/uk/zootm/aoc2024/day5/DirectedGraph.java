package uk.zootm.aoc2024.day5;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class DirectedGraph<N> {
    private final Set<N> nodes;
    private final SetMultimap<N, N> outgoing;
    private final SetMultimap<N, N> incoming;

    DirectedGraph() {
        this(new HashSet<>(), HashMultimap.create(), HashMultimap.create());
    }

    private DirectedGraph(Set<N> nodes, SetMultimap<N, N> outgoing, SetMultimap<N, N> incoming) {
        this.nodes = nodes;
        this.outgoing = outgoing;
        this.incoming = incoming;
    }

    void addNode(N name) {
        nodes.add(name);
    }

    void addEdge(N from, N to) {
        nodes.add(from);
        nodes.add(to);
        outgoing.put(from, to);
        incoming.put(to, from);
    }

    Set<N> outgoing(N name) {
        return outgoing.get(name);
    }

    Set<N> incoming(N name) {
        return incoming.get(name);
    }

    @Override
    protected DirectedGraph<N> clone() {
        return new DirectedGraph<>(
                new HashSet<>(nodes),
                HashMultimap.create(outgoing),
                HashMultimap.create(incoming));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DirectedGraph other) {
            return Objects.equals(nodes, other.nodes) && Objects.equals(outgoing, other.outgoing);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, outgoing);
    }

    @Override
    public String toString() {
        return String.format("DirectedGraph(nodes=%s,edges=%s)", nodes, outgoing);
    }
}