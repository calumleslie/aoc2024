package uk.zootm.aoc2024.day15;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day15.Day15.MapElement;
import uk.zootm.aoc2024.day15.Day15.Problem;
import uk.zootm.aoc2024.day15.Day15.Warehouse;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Vector;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.zootm.aoc2024.grid.Direction.E;
import static uk.zootm.aoc2024.grid.Direction.N;
import static uk.zootm.aoc2024.grid.Direction.S;
import static uk.zootm.aoc2024.grid.Direction.W;

public class Day15Test {

    @Test
    public void parse_smallExample() {
        var problem = parseProblem("""
                ########
                #..O.O.#
                ##@.O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                
                <^^>>>vv<v>>v<<
                """);

        assertThat(problem.warehouse().robot()).isEqualTo(new Vector(2, 2));
        assertThat(problem.warehouse().map().get(new Vector(0, 0))).isEqualTo(MapElement.WALL);
        assertThat(problem.warehouse().map().get(new Vector(1, 1))).isEqualTo(MapElement.BLANK);
        assertThat(problem.warehouse().map().get(new Vector(2, 2))).isEqualTo(MapElement.BLANK);
        assertThat(problem.warehouse().map().get(new Vector(3, 1))).isEqualTo(MapElement.BOX);
        assertThat(problem.instructions()).containsExactly(
                W, N, N, E, E, E, S, S, W, S, E, E, S, W, W);
    }

    @Test
    public void steps_example() {
        var problem = parseProblem("""
                ########
                #..O.O.#
                ##@.O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                
                <^^>>>vv<v>>v<<
                """);

        var wh = problem.warehouse();

        wh.moveRobot(W);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #..O.O.#
                ##@.O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                """));

        wh.moveRobot(N);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #.@O.O.#
                ##..O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                """));

        wh.moveRobot(N);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #.@O.O.#
                ##..O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                """));


        wh.moveRobot(E);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #..@OO.#
                ##..O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                """));

        wh.moveRobot(E);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #...@OO#
                ##..O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                """));


        wh.moveRobot(E);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #...@OO#
                ##..O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                """));


        wh.moveRobot(E);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #...@OO#
                ##..O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                """));

        wh.moveRobot(S);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##..@..#
                #...O..#
                #.#.O..#
                #...O..#
                #...O..#
                ########
                """));

        wh.moveRobot(S);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##..@..#
                #...O..#
                #.#.O..#
                #...O..#
                #...O..#
                ########
                """));


        wh.moveRobot(W);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##.@...#
                #...O..#
                #.#.O..#
                #...O..#
                #...O..#
                ########
                """));

        wh.moveRobot(S);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##.....#
                #..@O..#
                #.#.O..#
                #...O..#
                #...O..#
                ########
                """));

        wh.moveRobot(E);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##.....#
                #...@O.#
                #.#.O..#
                #...O..#
                #...O..#
                ########
                """));

        wh.moveRobot(E);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##.....#
                #....@O#
                #.#.O..#
                #...O..#
                #...O..#
                ########
                """));

        wh.moveRobot(S);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##.....#
                #.....O#
                #.#.O@.#
                #...O..#
                #...O..#
                ########
                """));

        wh.moveRobot(W);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##.....#
                #.....O#
                #.#O@..#
                #...O..#
                #...O..#
                ########
                """));

        wh.moveRobot(W);
        assertThat(wh).isEqualTo(warehouse("""
                ########
                #....OO#
                ##.....#
                #.....O#
                #.#O@..#
                #...O..#
                #...O..#
                ########
                """));

        assertThat(wh.boxesGpsSum()).isEqualTo(2028);
    }

    @Test
    public void largeExample() {
        var problem = parseProblem("""
                ##########
                #..O..O.O#
                #......O.#
                #.OO..O.O#
                #..O@..O.#
                #O#..O...#
                #O..O..O.#
                #.OO.O.OO#
                #....O...#
                ##########
                
                <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
                vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
                ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
                <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
                ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
                ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
                >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
                <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
                ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
                v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
                """);

        problem.warehouse().moveRobot(problem.instructions());

        assertThat(problem.warehouse().boxesGpsSum()).isEqualTo(10092);
    }

    private Warehouse warehouse(String string) {
        return Warehouse.read(CharacterGrid.fromString(string));
    }

    private Problem parseProblem(String string) {
        return Problem.parse(string.strip().lines().toList());
    }
}
