package uk.zootm.aoc2024.day8;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import uk.zootm.aoc2024.grid.Grid;
import uk.zootm.aoc2024.grid.Vector;

public class Day8 {
    public static void main(String[] args) throws Exception {
        var input = Resources.readLines(Day8.class.getResource("input"), StandardCharsets.UTF_8);
        var map = AntennaMap.parse(Grid.fromCharacterGrid(input));

        System.out.printf(
                "Part 1: %d%n", map.findAllInterferingPoints().distinct().count());
    }

    record AntennaMap(ListMultimap<String, Vector> antennas, int height, int width) {
        static AntennaMap parse(Grid grid) {
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

        Stream<Vector> findAllInterferingPoints() {
            return antennas.asMap().values().stream()
                    .flatMap(antennae -> findInterferingPointsFor((List<Vector>) antennae));
        }

        Stream<Vector> findInterferingPointsFor(List<Vector> antennae) {
            Stream<Vector> result = Stream.empty();
            for (int i = 0; i < (antennae.size() - 1); i++) {
                for (int j = i + 1; j < antennae.size(); j++) {
                    result = Stream.concat(result, findInterferingPoints(antennae.get(i), antennae.get(j)));
                }
            }
            return result;
        }

        Stream<Vector> findInterferingPoints(Vector first, Vector second) {
            var line = first.minus(second).minimize();

            var up = Stream.iterate(first, value -> value.minus(line)).takeWhile(this::inBounds);
            var down = Stream.iterate(first, value -> value.plus(line)).takeWhile(this::inBounds);

            return Stream.concat(up, down).filter(point -> interferes(first, second, point));
        }

        boolean inBounds(Vector vector) {
            return vector.x() >= 0 && vector.x() < width && vector.y() >= 0 && vector.y() < height;
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
