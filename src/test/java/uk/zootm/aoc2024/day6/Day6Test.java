package uk.zootm.aoc2024.day6;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.grid.CharacterGrid;

import static org.assertj.core.api.Assertions.assertThat;

public class Day6Test {
    @Test
    public void walk_example() {
        var grid = CharacterGrid.fromString(
                """
                ....#.....
                .........#
                ..........
                ..#.......
                .......#..
                ..........
                .#..^.....
                ........#.
                #.........
                ......#...
                """);

        var path = new Day6.Solver(grid).walk();

        assertThat(path.visitedLocations()).hasSize(41);
    }

    @Test
    public void part2_examples() {
        var grid = CharacterGrid.fromString(
                """
                ....#.....
                .........#
                ..........
                ..#.......
                .......#..
                ..........
                .#..^.....
                ........#.
                #.........
                ......#...
                """);

        assertThat(Day6.part2(grid)).hasSize(6);
    }
}
