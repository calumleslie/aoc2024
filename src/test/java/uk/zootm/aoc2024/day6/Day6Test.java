package uk.zootm.aoc2024.day6;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.grid.Grid;

import static org.assertj.core.api.Assertions.assertThat;

public class Day6Test {
    @Test
    public void walk_example() {
        var grid = Grid.fromString("""
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

        var path = Day6.walk(grid);

        assertThat(path.stream().distinct()).hasSize(41);
    }
}
