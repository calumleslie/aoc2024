package uk.zootm.aoc2024.day4;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.grid.Grid;
import uk.zootm.aoc2024.grid.Vector;

public class Day4Test {
    @Test
    public void hasXMas_example() {
        Grid grid = Grid.fromString(
                """
                MMMSXXMASM
                MSAMXMSMSA
                AMXSXMAAMM
                MSAMASMSMX
                XMASAMXAMM
                XXAMMXXAMA
                SMSMSASXSS
                SAXAMASAAA
                MAMMMXMMMM
                MXMXAXMASX
                """);

        assertThat(Day4.hasXMas(grid, new Vector(2, 1))).isTrue();
        assertThat(Day4.hasXMas(grid, new Vector(2, 2))).isFalse();
    }

    @Test
    public void countXMases_example() {
        Grid grid = Grid.fromString(
                """
                MMMSXXMASM
                MSAMXMSMSA
                AMXSXMAAMM
                MSAMASMSMX
                XMASAMXAMM
                XXAMMXXAMA
                SMSMSASXSS
                SAXAMASAAA
                MAMMMXMMMM
                MXMXAXMASX
                """);

        assertThat(Day4.countXMases(grid)).isEqualTo(9);
    }
}
