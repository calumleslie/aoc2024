package uk.zootm.aoc2024.day8;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableListMultimap;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day8.Day8.AntennaMap;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Vector;

public class Day8Test {
    @Test
    public void parse_fromExample() {
        var map = AntennaMap.parse(
                CharacterGrid.fromString(
                        """
                                ............
                                ........0...
                                .....0......
                                .......0....
                                ....0.......
                                ......A.....
                                ............
                                ............
                                ........A...
                                .........A..
                                ............
                                ............
                                """));

        assertThat(map)
                .isEqualTo(new AntennaMap(
                        ImmutableListMultimap.<String, Vector>builder()
                                .put("0", new Vector(8, 1))
                                .put("0", new Vector(5, 2))
                                .put("0", new Vector(7, 3))
                                .put("0", new Vector(4, 4))
                                .put("A", new Vector(6, 5))
                                .put("A", new Vector(8, 8))
                                .put("A", new Vector(9, 9))
                                .build(),
                        12,
                        12));
    }

    @Test
    public void interferes_singleExamples() {
        assertThat(Day8.interferes(new Vector(5, 5), new Vector(4, 3), new Vector(3, 1)))
                .isTrue();

        assertThat(Day8.interferes(new Vector(5, 5), new Vector(4, 3), new Vector(3, 2)))
                .isFalse();
    }

    @Test
    public void interferingPoints_fromExample1() {
        CharacterGrid grid = CharacterGrid.fromString(
                """
                        ..........
                        ...#......
                        #.........
                        ....a.....
                        ........a.
                        .....a....
                        ..#.......
                        ......A...
                        ..........
                        ..........
                        """);

        var map = AntennaMap.parse(grid);

        var expectedInterfering = grid.coords()
                .filter(c -> "#".equals(grid.get(c)) || "A".equals(grid.get(c)))
                .toList();

        assertThat(map.interferingPoints()).containsExactlyInAnyOrderElementsOf(expectedInterfering);
    }

    @Test
    public void interferingPoints_fromExample2() {
        CharacterGrid grid = CharacterGrid.fromString(
                """
                        ......#....#
                        ...#....0...
                        ....#0....#.
                        ..#....0....
                        ....0....#..
                        .#....A.....
                        ...#........
                        #......#....
                        ........A...
                        .........A..
                        ..........#.
                        ..........#.
                        """);

        var map = AntennaMap.parse(grid);

        var expectedInterfering =
                grid.coords().filter(c -> "#".equals(grid.get(c))).collect(Collectors.toList());
        // "antinode overlapping the topmost A-frequency antenna"
        expectedInterfering.add(new Vector(6, 5));

        assertThat(map.interferingPoints()).containsExactlyInAnyOrderElementsOf(expectedInterfering);
    }

    @Test
    public void findAllPaths_fromExample1() {
        CharacterGrid grid = CharacterGrid.fromString(
                """
                        T....#....
                        ...T......
                        .T....#...
                        .........#
                        ..#.......
                        ..........
                        ...#......
                        ..........
                        ....#.....
                        ..........
                        """);

        var map = AntennaMap.parse(grid);

        var expected = grid.coords()
                .filter(c -> "#".equals(grid.get(c)) || "T".equals(grid.get(c)))
                .collect(Collectors.toList());

        assertThat(map.pathPoints()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void findAllPaths_fromExample2() {
        CharacterGrid grid = CharacterGrid.fromString(
                """
                        ##....#....#
                        .#.#....0...
                        ..#.#0....#.
                        ..##...0....
                        ....0....#..
                        .#...#A....#
                        ...#..#.....
                        #....#.#....
                        ..#.....A...
                        ....#....A..
                        .#........#.
                        ...#......##
                        """);

        var map = AntennaMap.parse(grid);

        var expected = grid.coords().filter(c -> !".".equals(grid.get(c))).collect(Collectors.toList());

        assertThat(map.pathPoints()).containsExactlyInAnyOrderElementsOf(expected);
    }
}
