package uk.zootm.aoc2024.day8;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Vector;

public class Day8 {
    public static void main(String[] args) throws Exception {
        var input = Resources.readLines(Day8.class.getResource("input"), StandardCharsets.UTF_8);
        var map = AntennaMap.parse(CharacterGrid.fromCharacterGrid(input));

        System.out.printf("Part 1: %d%n", map.interferingPoints().count());
        System.out.printf("Part 2: %d%n", map.pathPoints().count());
    }

    record AntennaMap(ListMultimap<String, Vector> antennas, int height, int width) {
        static AntennaMap parse(CharacterGrid grid) {
            ImmutableListMultimap.Builder<String, Vector> result = ImmutableListMultimap.builder();

            grid.coords()
                    .flatMap(coord -> {
                        var cell = grid.get(coord);
                        var c = cell.charAt(0); // I will forever regret the Grapheme thing
                        if (Character.isDigit(c) || Character.isAlphabetic(c)) {
                            return Stream.of(Map.entry(cell, coord));
                        } else {
                            return Stream.empty();
                        }
                    })
                    .forEach(entry -> result.put(entry.getKey(), entry.getValue()));

            return new AntennaMap(result.build(), grid.width(), grid.height());
        }

        Stream<Vector> interferingPoints() {
            return findAllPaths()
                    .filter(PathElement::interferes)
                    .map(PathElement::point)
                    .distinct();
        }

        Stream<Vector> pathPoints() {
            return findAllPaths().map(PathElement::point).distinct();
        }

        Stream<PathElement> findAllPaths() {
            return antennas.asMap().values().stream()
                    .flatMap(antennae -> findAllPathsForFrequency((List<Vector>) antennae));
        }

        Stream<PathElement> findAllPathsForFrequency(List<Vector> antennae) {
            Stream<PathElement> result = Stream.empty();
            for (int i = 0; i < (antennae.size() - 1); i++) {
                for (int j = i + 1; j < antennae.size(); j++) {
                    result = Stream.concat(result, findPath(antennae.get(i), antennae.get(j)));
                }
            }
            return result;
        }

        Stream<PathElement> findPath(Vector first, Vector second) {
            var line = first.minus(second).minimize();

            var up = Stream.iterate(first, value -> value.minus(line)).takeWhile(this::inBounds);
            var down =
                    Stream.iterate(first.plus(line), value -> value.plus(line)).takeWhile(this::inBounds);

            return Stream.concat(up, down).map(point -> new PathElement(first, second, point));
        }

        boolean inBounds(Vector vector) {
            return vector.x() >= 0 && vector.x() < width && vector.y() >= 0 && vector.y() < height;
        }
    }

    record PathElement(Vector antenna1, Vector antenna2, Vector point) {

        boolean interferes() {
            return Day8.interferes(antenna1, antenna2, point);
        }
    }

    static boolean interferes(Vector first, Vector second, Vector potentiallyInterfering) {
        var fromFirst = first.minus(potentiallyInterfering).abs();
        var fromSecond = second.minus(potentiallyInterfering).abs();

        var bigger = fromFirst.magnitude() > fromSecond.magnitude() ? fromFirst : fromSecond;
        var smaller = fromFirst.magnitude() > fromSecond.magnitude() ? fromSecond : fromFirst;

        return bigger.equals(smaller.times(2));
    }
}
