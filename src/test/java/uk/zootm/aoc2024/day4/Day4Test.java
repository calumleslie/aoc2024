package uk.zootm.aoc2024.day4;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day4.Day4.Direction;
import uk.zootm.aoc2024.day4.Day4.FindResult;
import uk.zootm.aoc2024.day4.Day4.Grid;
import uk.zootm.aoc2024.day4.Day4.Vector;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day4Test {
    @Test
    public void matches_variousMatches() {
        Grid grid = gridOf("""
                SATOR
                AREPO
                TENET
                OPERA
                ROTAS
                """);

        assertThat(grid.matches(new Vector(0, 0), Direction.S, "SATOR")).isTrue();
        assertThat(grid.matches(new Vector(0, 0), Direction.E, "SATOR")).isTrue();
        assertThat(grid.matches(new Vector(4, 4), Direction.N, "SATOR")).isTrue();
        assertThat(grid.matches(new Vector(4, 4), Direction.W, "SATOR")).isTrue();
        assertThat(grid.matches(new Vector(4, 4), Direction.NW, "SRNRS")).isTrue();
    }

    @Test
    public void matches_outOfBounds() {
        Grid grid = gridOf("""
                SATOR
                AREPO
                TENET
                OPERA
                ROTAS
                """);

        assertThat(grid.matches(new Vector(0, 0), Direction.E, "SATORTENET")).isFalse();
        assertThat(grid.matches(new Vector(4, 4), Direction.E, "SATOR")).isFalse();
        assertThat(grid.matches(new Vector(0, 0), Direction.N, "SATOR")).isFalse();
    }

    @Test
    public void find_multipleMatches() {
        Grid grid = gridOf("""
                SATOR
                AREPO
                TENET
                OPERA
                ROTAS
                """);

        assertThat(grid.find("AREPO")).containsExactlyInAnyOrder(
                new FindResult(new Vector(1, 0), Direction.S),
                new FindResult(new Vector(0, 1), Direction.E),
                new FindResult(new Vector(4, 3), Direction.W),
                new FindResult(new Vector(3, 4), Direction.N));

        assertThat(grid.find("OO")).containsExactlyInAnyOrder(
                new FindResult(new Vector(3, 0), Direction.SE),
                new FindResult(new Vector(4, 1), Direction.NW),
                new FindResult(new Vector(0, 3), Direction.SE),
                new FindResult(new Vector(1, 4), Direction.NW));
    }

    @Test
    public void find_unicode() {
        Grid grid = gridOf("""
                🎄🎅🎁
                🎊🎉🎅
                🎈✨🎄
                """);

        assertThat(grid.find("🎄🎅🎁")).containsExactlyInAnyOrder(
                new FindResult(new Vector(0, 0), Direction.E),
                new FindResult(new Vector(2, 2), Direction.N));
    }

    @Test
    public void find_example() {
        Grid grid = gridOf("""
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

        assertThat(grid.find("XMAS")).hasSize(18);
    }

    @Test
    public void hasXMas_example() {
        Grid grid = gridOf("""
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
        Grid grid = gridOf("""
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

    private Grid gridOf(String string) {
        return new Grid(string.strip().lines().collect(Collectors.toUnmodifiableList()));
    }
}
