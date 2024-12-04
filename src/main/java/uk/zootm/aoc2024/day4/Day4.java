package uk.zootm.aoc2024.day4;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import com.google.common.primitives.ImmutableIntArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * What's going on with the complicated "Graphemes" stuff in this one? I wanted to see if I could make this work
 * "correctly" with respect to Unicode without losing the O(1) "cell" lookup in the grid. This is easy to do with
 * Unicode codepoints but these are not what people generally consider to be a "character", so instead the system
 * indexes grapheme clusters within strings ahead of time (this is what the Graphemes class does).
 */
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
        if (!grid.graphemeAt(centre).equals("A")) {
            return false;
        }

        List<Vector> corners = Stream.of(Direction.NE, Direction.SE, Direction.SW, Direction.NW)
                .map(dir -> centre.plus(dir.vector()))
                .collect(Collectors.toUnmodifiableList());

        if (!corners.stream().allMatch(grid::inBounds)) {
            return false;
        }

        long mases = corners.stream()
                .filter(corner -> grid.graphemeAt(corner).equals("M"))
                .filter(corner -> grid.graphemeAt(opposite(centre, corner)).equals("S"))
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

    static class Grid {
        int width;
        List<Graphemes> lines;

        Grid(List<String> lineStrings) {
            List<Graphemes> lines = lineStrings.stream()
                    .map(l -> Graphemes.forString(l, Locale.getDefault()))
                    .collect(Collectors.toUnmodifiableList());

            long distinctLengths = lines.stream().map(Graphemes::length).distinct().count();

            Preconditions.checkArgument(distinctLengths == 1L);

            this.width = lines.getFirst().length();
            this.lines = lines;
        }


        Stream<FindResult> find(String targetString) {
            var target = Graphemes.forString(targetString, Locale.getDefault());
            var possibleResults = coords().flatMap(loc -> Stream.of(Direction.values()).map(dir -> new FindResult(loc, dir)));

            return possibleResults.filter(result -> matches(result.location(), result.direction(), target));
        }

        Stream<Vector> coords() {
            return IntStream.range(0, height())
                    .mapToObj(y -> IntStream.range(0, width()).mapToObj(x -> new Vector(x, y)))
                    .flatMap(x -> x);
        }

        boolean matches(Vector coord, Direction direction, String target) {
            return matches(coord, direction, Graphemes.forString(target, Locale.getDefault()));
        }

        boolean matches(Vector coord, Direction direction, Graphemes target) {
            checkBounds(coord);

            Vector end = coord.plus(direction.vector().times(target.length() - 1));
            if (!inBounds(end)) {
                return false;
            }

            Stream<String> actualGraphemes = Stream.iterate(coord, c -> c.plus(direction.vector())).map(this::graphemeAt);
            Stream<String> targetGraphemes = target.graphemes();

            var targetIterator = targetGraphemes.iterator();
            var actualIterator = actualGraphemes.iterator();

            while (targetIterator.hasNext()) {
                // Don't need to check actual due to inBounds check above
                String targetGrapheme = targetIterator.next();
                String actualGrapheme = actualIterator.next();
                if (!targetGrapheme.equals(actualGrapheme)) {
                    return false;
                }
            }

            return true;
        }

        String graphemeAt(Vector coord) {
            checkBounds(coord);
            return lines.get(coord.y()).grapheme(coord.x());
        }

        private void checkBounds(Vector coord) {
            Preconditions.checkArgument(inBounds(coord), "Out of bounds: %s", coord);
        }

        int width() {
            return width;
        }

        int height() {
            return lines.size();
        }

        boolean inBounds(Vector coord) {
            return coord.x() >= 0 && coord.x() < width() && coord.y() >= 0 && coord.y() < height();
        }
    }

    static class Graphemes {
        private final String string;
        private final int[] boundaries;

        private Graphemes(String string, int[] graphemeStarts) {
            this.string = string;
            this.boundaries = graphemeStarts;
        }

        int length() {
            return boundaries.length - 1;
        }

        Stream<String> graphemes() {
            return IntStream.range(0, length()).mapToObj(this::grapheme);
        }

        String grapheme(int index) {
            int start = boundaries[index];
            int end = index == (boundaries.length - 1) ? string.length() : boundaries[index + 1];
            return string.substring(start, end);
        }

        static Graphemes forString(String string, Locale locale) {
            ImmutableIntArray.Builder boundaries = ImmutableIntArray.builder();
            BreakIterator iterator = BreakIterator.getCharacterInstance(locale);
            iterator.setText(string);
            boundaries.add(iterator.first());
            int next = iterator.next();
            while (next != BreakIterator.DONE) {
                boundaries.add(next);
                next = iterator.next();
            }
            return new Graphemes(string, boundaries.build().toArray());
        }

        @Override
        public String toString() {
            return String.format("IndexedGraphemes(%s, %s)", string, Arrays.toString(boundaries));
        }
    }
}
