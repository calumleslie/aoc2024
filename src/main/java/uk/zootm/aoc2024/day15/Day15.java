package uk.zootm.aoc2024.day15;

import com.google.common.io.Resources;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.CharacterGrid.FindResult;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Grid;
import uk.zootm.aoc2024.grid.MutableGrid;
import uk.zootm.aoc2024.grid.ObjGrid;
import uk.zootm.aoc2024.grid.Vector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15 {

    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day15.class.getResource("input"), StandardCharsets.UTF_8);

        var problem = Problem.parse(input);
        problem.warehouse().moveRobot(problem.instructions());
        System.out.printf("Part 1: %d%n", problem.warehouse().boxesGpsSum());
    }

    enum MapElement {
        WALL("#"), BOX("O"), BLANK(".");

        final String str;

        MapElement(String str) {
            this.str = str;
        }

        static Optional<MapElement> fromStr(String string) {
            return Arrays.stream(values()).filter(e -> e.str.equals(string)).findFirst();
        }

        @Override
        public String toString() {
            return str;
        }
    }

    static class Warehouse {
        private final MutableGrid<MapElement> map;
        private Vector robot;

        public Warehouse(MutableGrid<MapElement> map, Vector robot) {
            this.map = map;
            this.robot = robot;
        }

        Grid<MapElement> map() {
            return map;
        }

        Vector robot() {
            return robot;
        }

        void moveRobot(List<Direction> instructions) {
            instructions.forEach(this::moveRobot);
        }

        void moveRobot(Direction direction) {
            findBlank(direction).ifPresent(blank -> {
                Vector cell = blank;
                // "Bubble" the blank cell back to the cell where the robot will move to
                while(!cell.equals(robot)) {
                    var next = cell.minus(direction);
                    map.swap(cell, next);
                    cell = next;
                }
                robot = robot.plus(direction);
            });
        }

        long boxesGpsSum() {
            return map.coords()
                    .filter(v -> map.get(v) == MapElement.BOX)
                    .mapToLong(v -> v.y() * 100 + v.x())
                    .sum();
        }

        static Warehouse read(CharacterGrid charGrid) {
            var robot = charGrid.find("@").findFirst().map(FindResult::location).get();
            // Blank out the robot cell, we store this separately
            var grid = readMap(charGrid);
            Warehouse warehouse = new Warehouse(grid, robot);
            return warehouse;
        }

        private Optional<Vector> findBlank(Direction direction) {
            return Stream.iterate(robot.plus(direction), r -> r.plus(direction))
                    .takeWhile(v -> map.inBounds(v) && map.get(v) != MapElement.WALL)
                    .filter(v -> map.get(v).equals(MapElement.BLANK))
                    .findFirst();
        }

        @Override
        public int hashCode() {
            return Objects.hash(map, robot);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Warehouse other) {
                return Objects.equals(robot, other.robot) && Objects.equals(map, other.map);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return String.format("Robot: %s%nMap:%n%s", robot, map);
        }
    }

    static MutableGrid<MapElement> readMap(Grid<String> stringMap) {
        return ObjGrid.from(stringMap, str -> MapElement.fromStr(str).orElse(MapElement.BLANK));
    }

    record Problem(Warehouse warehouse, List<Direction> instructions) {
        public static Problem parse(List<String> lines) {
            var gridLines = lines.stream().takeWhile(line -> !line.isBlank()).toList();
            var instructionsLine = lines.subList(gridLines.size() + 1, lines.size()).stream().collect(Collectors.joining());

            var charGrid = CharacterGrid.fromCharacterGrid(gridLines);
            var warehouse = Warehouse.read(charGrid);
            var instructions = instructionsLine.chars().mapToObj(Problem::charToInstruction).toList();

            return new Problem(warehouse, instructions);
        }

        private static Direction charToInstruction(int character) {
            return switch(character) {
                case '^' -> Direction.N;
                case '>' -> Direction.E;
                case 'v' -> Direction.S;
                case '<' -> Direction.W;
                default -> throw new IllegalArgumentException("Unknown instruction: " + character);
            };
        }
    }
}
