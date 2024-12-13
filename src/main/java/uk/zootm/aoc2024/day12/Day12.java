package uk.zootm.aoc2024.day12;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Vector;

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Resources.readLines(Day12.class.getResource("input"), StandardCharsets.UTF_8);
        CharacterGrid grid = CharacterGrid.fromCharacterGrid(lines);

        var segments = findSegments(grid);
        System.out.printf(
                "Part 1: %d%n", segments.stream().mapToLong(Segment::score).sum());
        System.out.printf(
                "Part 2: %d%n",
                segments.stream().mapToLong(Segment::discountScore).sum());
    }

    private static final Set<Direction> ALL = ImmutableSet.copyOf(Direction.values());

    static List<Segment> findSegments(CharacterGrid grid) {
        Set<Vector> seen = new HashSet<>();
        List<Segment> result = new ArrayList<>();
        var coordIterator = grid.coords().iterator();

        while (coordIterator.hasNext()) {
            var loc = coordIterator.next();
            if (seen.contains(loc)) {
                continue;
            }
            var segment = findSegment(grid, loc);
            result.add(segment);
            seen.addAll(segment.cells());
        }

        return result;
    }

    static Segment findSegment(CharacterGrid grid, Vector loc) {
        String element = grid.get(loc);
        Set<Vector> cells = new HashSet<>();
        Deque<Vector> toCheck = new ArrayDeque<>();
        cells.add(loc);
        toCheck.add(loc);

        Vector current;
        while ((current = toCheck.pollFirst()) != null) {
            neighbours(current, Direction.cardinal())
                    .map(Neighbour::location)
                    .filter(grid::inBounds)
                    .filter(neighbour -> grid.get(neighbour).equals(element))
                    // Adds to the cells list as a "side effect" of filtering out seen cells
                    .filter(cells::add)
                    .forEach(toCheck::add);
        }

        return new Segment(element, cells);
    }

    private static Stream<Neighbour> neighbours(Vector vector, Set<Direction> directions) {
        return directions.stream().map(dir -> new Neighbour(vector, dir));
    }

    record Neighbour(Vector origin, Direction direction) {
        Vector location() {
            return origin.plus(direction);
        }
    }

    record Segment(String element, Set<Vector> cells) {
        int area() {
            return cells.size();
        }

        long perimeter() {
            return cells.stream().mapToLong(this::edges).sum();
        }

        long score() {
            return area() * perimeter();
        }

        long discountScore() {
            return area() * cornerLocations().count();
        }

        Stream<Vector> cornerLocations() {
            return cells.stream().flatMap(this::cornerLocations);
        }

        private long edges(Vector vector) {
            return externalNeighbours(vector, Direction.cardinal()).count();
        }

        private Stream<Vector> cornerLocations(Vector vector) {
            return corners(vector).map(corner -> corner.location(vector));
        }

        @VisibleForTesting
        Stream<Corner> corners(Vector vector) {
            Set<Direction> edgeDirections =
                    externalNeighbours(vector, ALL).map(Neighbour::direction).collect(Collectors.toSet());

            return Arrays.stream(Corner.values()).filter(corner -> corner.matches(edgeDirections));
        }

        private Stream<Neighbour> externalNeighbours(Vector vector, Set<Direction> directions) {
            return neighbours(vector, directions).filter(neighbour -> !cells.contains(neighbour.location()));
        }
    }

    enum Corner {
        NW(Direction.NW, 0, 0),
        NE(Direction.NE, 1, 0),
        SW(Direction.SW, 0, 1),
        SE(Direction.SE, 1, 1);

        private final Direction direction;
        private final Direction anticlockwise;
        private final Direction clockwise;
        private final Vector offset;

        Corner(Direction direction, int offsetX, int offsetY) {
            this.direction = direction;
            this.anticlockwise = direction.rotateSteps(-1);
            this.clockwise = direction.rotateSteps(1);
            this.offset = new Vector(offsetX, offsetY);
        }

        Vector location(Vector cell) {
            return cell.plus(offset);
        }

        boolean matches(Set<Direction> externalNeighbours) {
            boolean hasDirection = externalNeighbours.contains(direction);
            boolean hasAnticlockwise = externalNeighbours.contains(anticlockwise);
            boolean hasClockwise = externalNeighbours.contains(clockwise);

            boolean outerCorner = hasAnticlockwise && hasClockwise;
            boolean innerCorner = hasDirection && !hasAnticlockwise && !hasClockwise;

            return outerCorner || innerCorner;
        }
    }
}
