package uk.zootm.aoc2024.day18;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.zootm.aoc2024.graph.DijkstraSolver;
import uk.zootm.aoc2024.graph.DijkstraSolver.Path;
import uk.zootm.aoc2024.graph.DijkstraSolver.PathsToRetain;
import uk.zootm.aoc2024.graph.SimpleDirectedGraph;
import uk.zootm.aoc2024.graph.SimpleDirectedGraph.NoValue;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Vector;

public class Day18 {
    private static final Vector BOUNDS = new Vector(71, 71);

    public static void main(String[] args) throws IOException {
        var lines = Resources.readLines(Day18.class.getResource("input"), StandardCharsets.UTF_8);

        var firstKb = parseVectors(lines.stream()).limit(1024).collect(Collectors.toSet());

        System.out.printf("Part 1: %d%n", shortestPath(BOUNDS, firstKb).totalCost());
    }

    static Path<Vector> shortestPath(Vector size, Set<Vector> obstructed) {
        // Construct a graph linking every cell to its valid moves

        SimpleDirectedGraph<Vector> graph = new SimpleDirectedGraph<>();
        size.containedCoords()
                // Cannot move away from obstruction
                .filter(from -> !obstructed.contains(from))
                .forEach(from -> {
                    for (var direction : Direction.cardinal()) {
                        var to = from.plus(direction);
                        if (to.inBounds(size) && !obstructed.contains(to)) {
                            graph.addEdge(from, to);
                        }
                    }
                });

        DijkstraSolver<NoValue> solver = new DijkstraSolver<>(e -> 1L, PathsToRetain.SINGLE);
        List<Path<Vector>> paths = solver.pathsToNode(graph, new Vector(0, 0), size.plus(Direction.NW));
        // All paths same length
        return paths.getFirst();
    }

    static Stream<Vector> parseVectors(Stream<String> lines) {
        return lines.map(Day18::parseVector);
    }

    private static Vector parseVector(String line) {
        var bits = line.split(",", 2);

        return new Vector(Integer.parseInt(bits[0]), Integer.parseInt(bits[1]));
    }
}
