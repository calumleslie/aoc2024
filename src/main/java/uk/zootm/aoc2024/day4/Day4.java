package uk.zootm.aoc2024.day4;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day4 {

    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day4.class.getResource("input"), StandardCharsets.UTF_8);
        var grid = new Grid(input);

        System.out.printf("Part 1: %d%n", grid.find("XMAS").count());
        System.out.printf("Part 2: %d%n", countXMases(grid));
    }

    static long countXMases(Grid grid) {
        return grid.coords().filter(coord -> hasXMas(grid, coord)).count();
    }

    // I'm not generalizing this so this is not going on the grid class :)
    static boolean hasXMas(Grid grid, Vector centre) {
        if (grid.charAt(centre) != 'A') {
            return false;
        }

        List<Vector> corners = Stream.of(Direction.NE, Direction.SE, Direction.SW, Direction.NW)
                .map(dir -> centre.plus(dir.vector()))
                .collect(Collectors.toUnmodifiableList());

        if (!corners.stream().allMatch(grid::inBounds)) {
            return false;
        }

        long mases = corners.stream()
                .filter(corner -> grid.charAt(corner) == 'M')
                .filter(corner -> grid.charAt(opposite(centre, corner)) == 'S')
                .count();

        return mases == 2L;
    }

    private static Vector opposite(Vector centre, Vector source) {
        return centre.minus(source.minus(centre));
    }

    enum Direction {
        N(0, -1),
        NE(1, -1),
        E(1, 0),
        SE(1, 1),
        S(0, 1),
        SW(-1, 1),
        W(-1, 0),
        NW(-1, -1);

        private final Vector vector;

        private Direction(int x, int y) {
            Preconditions.checkArgument(x >= -1 && x <= 1);
            Preconditions.checkArgument(y >= -1 && y <= 1);
            this.vector = new Vector(x, y);
        }

        public Vector vector() {
            return vector;
        }
    }

    record Vector(int x, int y) {
        Vector plus(Vector other) {
            return new Vector(x + other.x, y + other.y);
        }

        Vector minus(Vector other) {
            return new Vector(x - other.x, y - other.y);
        }

        Vector times(int magnitude) {
            return new Vector(x * magnitude, y * magnitude);
        }
    }

    record FindResult(Vector location, Direction direction) {
    }

    record Grid(List<String> lines) {
        Grid {
            long distinctLengths = lines.stream().map(String::length).distinct().count();
            Preconditions.checkArgument(distinctLengths == 1L);
        }

        Stream<FindResult> find(String target) {
            var possibleResults = coords().flatMap(loc -> Stream.of(Direction.values()).map(dir -> new FindResult(loc, dir)));

            return possibleResults.filter(result -> matches(result.location(), result.direction(), target));
        }

        Stream<Vector> coords() {
            return IntStream.range(0, height())
                    .mapToObj(y -> IntStream.range(0, width()).mapToObj(x -> new Vector(x, y)))
                    .flatMap(x -> x);
        }

        boolean matches(Vector coord, Direction direction, String target) {
            checkBounds(coord);

            Vector end = coord.plus(direction.vector().times(target.length() - 1));
            if (!inBounds(end)) {
                return false;
            }

            IntStream actualCharacters = Stream.iterate(coord, c -> c.plus(direction.vector())).mapToInt(this::charAt);
            IntStream targetCharacters = target.chars();

            var targetIterator = targetCharacters.iterator();
            var actualIterator = actualCharacters.iterator();

            while (targetIterator.hasNext()) {
                // Don't need to check actual due to inBounds check above
                if (targetIterator.nextInt() != actualIterator.nextInt()) {
                    return false;
                }
            }

            return true;
        }

        char charAt(Vector coord) {
            checkBounds(coord);
            // Not very unicode compliant right now
            return lines.get(coord.y()).charAt(coord.x());
        }

        private void checkBounds(Vector coord) {
            Preconditions.checkArgument(inBounds(coord), "Out of bounds: %s", coord);
        }

        int width() {
            return lines.getFirst().length();
        }

        int height() {
            return lines.size();
        }

        boolean inBounds(Vector coord) {
            return coord.x() >= 0 && coord.x() < width() && coord.y() >= 0 && coord.y() < height();
        }
    }
}
