package uk.zootm.aoc2024.day10;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.LongGrid;
import uk.zootm.aoc2024.grid.ObjGrid;
import uk.zootm.aoc2024.grid.Vector;

public class Day10 {
    private static final Set<Direction> LEGAL_DIRECTIONS =
            ImmutableSet.of(Direction.N, Direction.E, Direction.S, Direction.W);

    public static void main(String[] args) throws Exception {
        var input = Resources.readLines(Day10.class.getResource("input"), StandardCharsets.UTF_8);
        var map = LongGrid.fromStringGrid(CharacterGrid.fromCharacterGrid(input));

        System.out.printf("Part 1: %d%n", trailheadScore(map));
    }

    static long trailheadScore(LongGrid trailMap) {
        var reachablePeaks = reachablePeaks(trailMap);
        return trailheads(trailMap)
                .map(reachablePeaks::get)
                .mapToLong(Set::size)
                .sum();
    }

    static Stream<Vector> trailheads(LongGrid trailMap) {
        return trailMap.coords().filter(c -> trailMap.getAsLong(c) == 0L);
    }

    static ObjGrid<Set<Vector>> reachablePeaks(LongGrid trailMap) {
        Multimap<Long, Vector> heightToLocations = HashMultimap.create();

        trailMap.coords().forEach(coord -> {
            heightToLocations.put(trailMap.get(coord), coord);
        });

        ObjGrid<Set<Vector>> reachablePeaks = ObjGrid.empty(trailMap.width(), trailMap.height());

        // Calling a "9-height" area a "peak". Each of these are reachable from themselves
        heightToLocations.get(9L).forEach(loc -> reachablePeaks.set(loc, Set.of(loc)));

        // Now we walk downwards counting reachable from adjacent
        for (long height = 8; height >= 0; height--) {
            var higher = heightToLocations.get(height + 1);
            var locations = heightToLocations.get(height);

            for (var location : locations) {
                // Any "higher" height should be populated
                var reachable = adjacent(trailMap, location)
                        .filter(higher::contains)
                        .flatMap(adj -> reachablePeaks.get(adj).stream())
                        .collect(Collectors.toSet());

                reachablePeaks.set(location, reachable);
            }
        }
        return reachablePeaks;
    }

    private static Stream<Vector> adjacent(LongGrid map, Vector vector) {
        return LEGAL_DIRECTIONS.stream().map(dir -> vector.plus(dir.vector())).filter(map::inBounds);
    }
}
