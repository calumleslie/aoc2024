package uk.zootm.aoc2024.day16;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day16.Day16.Location;
import uk.zootm.aoc2024.day16.Day16.Maze;
import uk.zootm.aoc2024.day16.Day16.Path;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Vector;

public class Day16Test {

    public static final String EXAMPLE1 =
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
            """;

    public static final String EXAMPLE2 =
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
            """;

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
        var maze = maze(EXAMPLE1);

        assertThat(Day16.minCostPaths(maze))
                .contains(new Path(new Location(1, 13, Direction.E))
                        .then(1, 13, Direction.N, 1000)
                        .then(1, 11, Direction.N, 2)
                        .then(1, 11, Direction.E, 1000)
                        .then(3, 11, Direction.E, 2)
                        .then(3, 11, Direction.N, 1000)
                        .then(3, 9, Direction.N, 2)
                        .then(3, 7, Direction.N, 2)
                        .then(3, 7, Direction.E, 1000)
                        .then(5, 7, Direction.E, 2)
                        .then(9, 7, Direction.E, 4)
                        .then(11, 7, Direction.E, 2)
                        .then(11, 7, Direction.S, 1000)
                        .then(11, 13, Direction.S, 6)
                        .then(11, 13, Direction.E, 1000)
                        .then(13, 13, Direction.E, 2)
                        .then(13, 13, Direction.N, 1000)
                        .then(13, 1, Direction.N, 12));
    }

    @Test
    public void uniqueLocations_example1() {
        var maze = maze(EXAMPLE1);

        var paths = Day16.minCostPaths(maze);
        assertThat(Day16.uniqueLocations(paths)).isEqualTo(45);
    }

    @Test
    public void uniqueLocations_example2() {
        var maze = maze(EXAMPLE2);

        var paths = Day16.minCostPaths(maze);
        assertThat(Day16.uniqueLocations(paths)).isEqualTo(64);
    }

    private Maze maze(String in) {
        return Day16.readMaze(CharacterGrid.fromString(in));
    }
}
