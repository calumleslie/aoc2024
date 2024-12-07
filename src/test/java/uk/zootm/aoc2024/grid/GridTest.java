package uk.zootm.aoc2024.grid;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.grid.Grid.FindResult;

public class GridTest {
    @Test
    public void matches_variousMatches() {
        Grid grid = Grid.fromString(
                """
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
        Grid grid = Grid.fromString(
                """
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
        Grid grid = Grid.fromString(
                """
                SATOR
                AREPO
                TENET
                OPERA
                ROTAS
                """);

        assertThat(grid.find("AREPO"))
                .containsExactlyInAnyOrder(
                        new FindResult(new Vector(1, 0), Direction.S),
                        new FindResult(new Vector(0, 1), Direction.E),
                        new FindResult(new Vector(4, 3), Direction.W),
                        new FindResult(new Vector(3, 4), Direction.N));

        assertThat(grid.find("OO"))
                .containsExactlyInAnyOrder(
                        new FindResult(new Vector(3, 0), Direction.SE),
                        new FindResult(new Vector(4, 1), Direction.NW),
                        new FindResult(new Vector(0, 3), Direction.SE),
                        new FindResult(new Vector(1, 4), Direction.NW));
    }

    @Test
    public void find_unicode() {
        Grid grid = Grid.fromString(
                """
                🎄🎅🏼🎁
                🎊🎉🎅🏼
                🎈✨🎄
                """);

        assertThat(grid.find("🎄🎅🏼🎁"))
                .containsExactlyInAnyOrder(
                        new FindResult(new Vector(0, 0), Direction.E), new FindResult(new Vector(2, 2), Direction.N));
    }

    @Test
    public void find_example() {
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

        assertThat(grid.find("XMAS")).hasSize(18);
    }
}
