package uk.zootm.aoc2024.day10;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.LongGrid;

public class Day10Test {
    @Test
    public void trailheadScore_example() {
        var map = gridOf(
                """
                        89010123
                        78121874
                        87430965
                        96549874
                        45678903
                        32019012
                        01329801
                        10456732
                        """);

        var reachableMap = Day10.reachablePeaks(map);
        assertThat(Day10.trailheads(map).map(reachableMap::get).mapToInt(Set::size))
                .containsExactly(5, 6, 5, 3, 1, 3, 5, 3, 5);

        assertThat(Day10.trailheadScore(map)).isEqualTo(36L);
    }

    private static LongGrid gridOf(String s) {
        return LongGrid.fromStringGrid(CharacterGrid.fromString(s));
    }
}
