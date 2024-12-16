package uk.zootm.aoc2024.day16;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day16.Day16.Location;
import uk.zootm.aoc2024.day16.Day16.Maze;
import uk.zootm.aoc2024.day16.Day16.Path;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Vector;

public class Day16Test {
    @Test
    public void readSimpleMaze() {
        // I am not using the example for this

        Maze maze = maze(
                """
                #####
                #..E#
                ##.##
                ##.##
                #.S.#
                #####
                """);

        var map = maze.map();

        // Calling this one assertion enough
        assertThat(map.outgoing(new Location(new Vector(2, 1), Direction.S)))
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        new Location(2, 1, Direction.E), 1000L,
                        new Location(2, 1, Direction.W), 1000L,
                        new Location(2, 4, Direction.S), 3L));
    }

    @Test
    public void minCostPath_example1() {
        var maze = maze(
                """
                ###############
                #.......#....E#
                #.#.###.#.###.#
                #.....#.#...#.#
                #.###.#####.#.#
                #.#.#.......#.#
                #.#.#####.###.#
                #...........#.#
                ###.#.#####.#.#
                #...#.....#.#.#
                #.#.#.###.#.#.#
                #.....#...#.#.#
                #.###.#.#.#.#.#
                #S..#.....#...#
                ###############
                """);

        assertThat(Day16.minCostPath(maze))
                .isEqualTo(new Path(new Location(1, 13, Direction.E))
                        .then(1, 13, Direction.N, 1000)
                        .then(1, 11, Direction.N, 2)
                        .then(1, 11, Direction.E, 1000)
                        .then(3, 11, Direction.E, 2)
                        .then(3, 11, Direction.N, 1000)
                        .then(3, 7, Direction.N, 4)
                        .then(3, 7, Direction.E, 1000)
                        .then(11, 7, Direction.E, 8)
                        .then(11, 7, Direction.S, 1000)
                        .then(11, 13, Direction.S, 6)
                        .then(11, 13, Direction.E, 1000)
                        .then(13, 13, Direction.E, 2)
                        .then(13, 13, Direction.N, 1000)
                        .then(13, 1, Direction.N, 12));
    }

    @Test
    public void minCostPath_example2() {
        var maze = maze(
                """
                #################
                #...#...#...#..E#
                #.#.#.#.#.#.#.#.#
                #.#.#.#...#...#.#
                #.#.#.#.###.#.#.#
                #...#.#.#.....#.#
                #.#.#.#.#.#####.#
                #.#...#.#.#.....#
                #.#.#####.#.###.#
                #.#.#.......#...#
                #.#.###.#####.###
                #.#.#...#.....#.#
                #.#.#.#####.###.#
                #.#.#.........#.#
                #.#.#.#########.#
                #S#.............#
                #################
                """);

        assertThat(Day16.minCostPath(maze).totalCost()).isEqualTo(11048);
    }

    private Maze maze(String in) {
        return Day16.readMaze(CharacterGrid.fromString(in));
    }
}
