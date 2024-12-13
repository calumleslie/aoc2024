package uk.zootm.aoc2024.day6;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Vector;

public class Day6 {
    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day6.class.getResource("input"), StandardCharsets.UTF_8);
        var grid = CharacterGrid.fromCharacterGrid(input);

        System.out.printf(
                "Part 1: %d%n", new Solver(grid).walk().visitedLocations().size());
        System.out.printf("Part 2: %d%n", part2(grid).size());
    }

    static List<Vector> part2(CharacterGrid grid) {
        Path mainPath = new Solver(grid).walk();

        // Only need to check things we'd bump into. Feels like there's probably a "nice" algorithm for this but I do
        // not know it; probably we can at least memoize the partial paths. Whatever. This works fine.
        return mainPath.visitedLocations().stream()
                .filter(coord -> {
                    var solver = new Solver(grid, Set.of(coord));
                    return solver.walk().isLoop();
                })
                .toList();
    }

    static class Solver {
        private final CharacterGrid grid;
        private final Set<Vector> extraObstacles;

        Solver(CharacterGrid grid) {
            this(grid, Collections.emptySet());
        }

        public Solver(CharacterGrid grid, Set<Vector> extraObstacles) {
            this.grid = grid;
            this.extraObstacles = extraObstacles;
        }

        Path walk() {
            Vector start = grid.find("^").findFirst().orElseThrow().location();

            var current = new PathElement(start, Direction.N);
            var path = new LinkedHashSet<PathElement>();

            while (grid.inBounds(current.location()) && !path.contains(current)) {
                path.add(current);
                var direction = findDirection(current);
                var location = current.location().plus(direction.vector());
                current = new PathElement(location, direction);
            }

            // We found a loop if we ended up on the grid and didn't walk off it
            return new Path(List.copyOf(path), grid.inBounds(current.location()));
        }

        private Direction findDirection(PathElement current) {
            Direction result = current.direction();
            while (!canMove(current.location(), result)) {
                result = result.clockwise90();
                if (result.equals(current.direction())) {
                    throw new IllegalArgumentException("Cannot move from: " + current.location());
                }
            }
            return result;
        }

        private boolean canMove(Vector location, Direction direction) {
            Vector destination = location.plus(direction.vector());
            // We can move off-grid, or into any space which is not an obstacle
            return !grid.inBounds(destination) || !hasObstacle(destination);
        }

        private boolean hasObstacle(Vector destination) {
            return grid.get(destination).equals("#") || extraObstacles.contains(destination);
        }
    }

    record Path(List<PathElement> path, boolean isLoop) {
        Set<Vector> visitedLocations() {
            return path.stream().map(PathElement::location).collect(Collectors.toSet());
        }
    }

    record PathElement(Vector location, Direction direction) {}
}
