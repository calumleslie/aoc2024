package uk.zootm.aoc2024.day12;

import com.google.common.io.Resources;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Vector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Resources.readLines(Day12.class.getResource("input"), StandardCharsets.UTF_8);
        CharacterGrid grid = CharacterGrid.fromCharacterGrid(lines);

        var segments = findSegments(grid);
        System.out.printf("Part 1: %d%n", segments.stream().mapToLong(Segment::score).sum());
    }

    static List<Segment> findSegments(CharacterGrid grid) {
        Set<Vector> seen = new HashSet<>();
        List<Segment> result = new ArrayList<>();
        var coordIterator = grid.coords().iterator();

        while(coordIterator.hasNext()) {
            var loc = coordIterator.next();
            if(seen.contains(loc)) {
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
        while((current = toCheck.pollFirst()) != null) {
            neighbours(current)
                    .filter(grid::inBounds)
                    .filter(neighbour -> grid.get(neighbour).equals(element))
                    // Adds to the cells list as a "side effect" of filtering out seen cells
                    .filter(cells::add)
                    .forEach(toCheck::add);
        }

        return new Segment(element, cells);
    }

    private static Stream<Vector> neighbours(Vector vector) {
        return Direction.cardinal().stream().map(vector::plus);
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

        private long edges(Vector vector) {
            return neighbours(vector).filter(neighbour -> !cells.contains(neighbour)).count();
        }
    }
}
