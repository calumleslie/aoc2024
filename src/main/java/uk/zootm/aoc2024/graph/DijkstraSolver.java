package uk.zootm.aoc2024.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

public class DijkstraSolver<E> {
    private final ToLongFunction<E> weighEdge;

    public DijkstraSolver(ToLongFunction<E> weighEdge) {
        this.weighEdge = weighEdge;
    }

    public <N> List<Path<N>> pathsToNode(DirectedGraph<N, E> graph, N start, N end) {
        return pathsToMatchingNode(graph, start, (Predicate<N>) end::equals);
    }

    public <N> List<Path<N>> pathsToMatchingNode(DirectedGraph<N, E> graph, N start, Predicate<N> isEnd) {
        // linked as we'll be iterating this a lot
        var unvisited = new LinkedHashSet<>(graph.nodes());
        var paths = ArrayListMultimap.<N, Path<N>>create();
        paths.put(start, new Path<>(start));

        while (true) {
            var currentPaths = unvisited.stream()
                    .filter(paths::containsKey)
                    .map(paths::get)
                    .min(Comparator.comparing(ps -> ps.getFirst().totalCost()))
                    .orElseThrow(() -> new IllegalStateException("unreachable, visited: " + paths.keySet()));

            var currentNode = currentPaths.getFirst().end();
            if (isEnd.test(currentNode)) {
                return currentPaths;
            }

            for (var outgoing : graph.outgoing(currentNode).entrySet()) {
                var incrementalCost = weighEdge.applyAsLong(outgoing.getValue());
                var outPaths = currentPaths.stream().map(p -> p.then(outgoing.getKey(), incrementalCost)).toList();

                // Cost of all of these is required to be the same
                var cost = outPaths.getFirst().totalCost();
                var existingCost = paths.get(outgoing.getKey()).stream().mapToLong(Path::totalCost).findFirst().orElse(Long.MAX_VALUE);

                if(existingCost == cost) {
                    // We found more equivalent paths
                    paths.putAll(outgoing.getKey(), outPaths);
                } else if(cost < existingCost) {
                    // We found better paths, replace existing
                    paths.removeAll(outgoing.getKey());
                    paths.putAll(outgoing.getKey(), outPaths);
                }
            }

            unvisited.remove(currentNode);
        }
    }

    public record Path<N>(N end, Path<N> prefix, long totalCost) {
        public Path {
            Objects.requireNonNull(end);
            Preconditions.checkArgument((prefix == null) == (totalCost == 0L));
        }

        public Path(N node) {
           this(node, null, 0L);
        }

        public Path<N> then(N node, long incrementalCost) {
            return new Path<>(node, this, totalCost + incrementalCost);
        }

        public List<Path<N>> toPathList() {
            var reverse = Stream.iterate(this, p -> p.prefix()).takeWhile(p -> p != null).toList();
            return reverse.reversed();
        }
    }
}
