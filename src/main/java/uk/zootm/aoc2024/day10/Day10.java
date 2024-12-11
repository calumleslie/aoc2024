package uk.zootm.aoc2024.day10;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Stream;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Grid;
import uk.zootm.aoc2024.grid.LongGrid;
import uk.zootm.aoc2024.grid.ObjGrid;
import uk.zootm.aoc2024.grid.Vector;

public class Day10 {
    private static final Set<Direction> LEGAL_DIRECTIONS =
            ImmutableSet.of(Direction.N, Direction.E, Direction.S, Direction.W);

    public static void main(String[] args) throws Exception {
        var input = Resources.readLines(Day10.class.getResource("input"), StandardCharsets.UTF_8);
        var map = LongGrid.fromStringGrid(CharacterGrid.fromCharacterGrid(input));

        System.out.printf("Part 1: %d%n", peaksTrailheadScore(map));
        System.out.printf("Part 2: %d%n", routesTrailheadScore(map));
    }

    static long peaksTrailheadScore(LongGrid trailMap) {
        var reachablePeaks = reachablePeaks(trailMap);
        return trailheads(trailMap)
                .map(reachablePeaks::get)
                .mapToLong(peaks -> peaks.elementSet().size())
                .sum();
    }

    static long routesTrailheadScore(LongGrid trailMap) {
        var reachablePeaks = reachablePeaks(trailMap);
        return trailheads(trailMap)
                .map(reachablePeaks::get)
                .mapToLong(Multiset::size)
                .sum();
    }

    static Stream<Vector> trailheads(LongGrid trailMap) {
        return trailMap.coords().filter(c -> trailMap.getAsLong(c) == 0L);
    }

    /**
     * Returns a grid where each cell contains a multiset of which peaks are reachable by a trail from that cell, with
     * the count being the number of unique trails.
     */
    static Grid<Multiset<Vector>> reachablePeaks(LongGrid trailMap) {
        Multimap<Long, Vector> heightToLocations = HashMultimap.create();

        trailMap.coords().forEach(coord -> {
            heightToLocations.put(trailMap.get(coord), coord);
        });

        ObjGrid<Multiset<Vector>> reachablePeaks = ObjGrid.empty(trailMap.width(), trailMap.height());

        // Calling a "9-height" area a "peak". Each of these are reachable from themselves
        heightToLocations.get(9L).forEach(loc -> reachablePeaks.set(loc, ImmutableMultiset.of(loc)));

        // Now we walk downwards counting reachable from adjacent
        for (long height = 8; height >= 0; height--) {
            var higher = heightToLocations.get(height + 1);
            var locations = heightToLocations.get(height);

            for (var location : locations) {
                // Any "higher" height should be populated
                var reachable = adjacent(trailMap, location)
                        .filter(higher::contains)
                        .flatMap(adj -> reachablePeaks.get(adj).stream())
                        .collect(ImmutableMultiset.toImmutableMultiset());

                reachablePeaks.set(location, reachable);
            }
        }
        return reachablePeaks;
    }

    private static Stream<Vector> adjacent(LongGrid map, Vector vector) {
        return LEGAL_DIRECTIONS.stream().map(dir -> vector.plus(dir.vector())).filter(map::inBounds);
    }
}
