package uk.zootm.aoc2024.day10;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.LongGrid;

public class Day10Test {
    @Test
    public void peaksTrailheadScore_example() {
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

        assertThat(Day10.peaksTrailheadScore(map)).isEqualTo(36L);
    }

    @Test
    public void routesTrailheadScore_example() {
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

        assertThat(Day10.routesTrailheadScore(map)).isEqualTo(81L);
    }

    private static LongGrid gridOf(String s) {
        return LongGrid.fromStringGrid(CharacterGrid.fromString(s));
    }
}
