package uk.zootm.aoc2024.day15;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.CharacterGrid.FindResult;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Grid;
import uk.zootm.aoc2024.grid.MutableGrid;
import uk.zootm.aoc2024.grid.ObjGrid;
import uk.zootm.aoc2024.grid.Vector;

public class Day15 {

    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day15.class.getResource("input"), StandardCharsets.UTF_8);

        var problem = Problem.parse(input);
        var warehouse = problem.warehouse();
        var wideWarehouse = warehouse.scaleUp();

        warehouse.moveRobot(problem.instructions());
        System.out.printf("Part 1: %d%n", warehouse.boxesGpsSum());

        wideWarehouse.moveRobot(problem.instructions());
        System.out.printf("Part 2: %d%n", wideWarehouse.boxesGpsSum());
    }

    enum MapElement {
        WALL("#"),
        BOX("O"),
        BLANK("."),
        BOX_LEFT("["),
        BOX_RIGHT("]");

        final String str;

        MapElement(String str) {
            this.str = str;
        }

        static Optional<MapElement> fromStr(String string) {
            return Arrays.stream(values()).filter(e -> e.str.equals(string)).findFirst();
        }

        Optional<Direction> adjacentElementDirection() {
            return switch (this) {
                case BOX_LEFT -> Optional.of(Direction.E);
                case BOX_RIGHT -> Optional.of(Direction.W);
                default -> Optional.empty();
            };
        }

        @Override
        public String toString() {
            return str;
        }
    }

    record ScaledElement(MapElement left, MapElement right) {
        public static final ScaledElement SCALED_BOX = new ScaledElement(MapElement.BOX_LEFT, MapElement.BOX_RIGHT);
        public static final ScaledElement SCALED_BLANK = new ScaledElement(MapElement.BLANK, MapElement.BLANK);
        public static final ScaledElement SCALED_WALL = new ScaledElement(MapElement.WALL, MapElement.WALL);

        void scaleInto(MutableGrid<MapElement> scaledUpMap, Vector orig) {
            scaledUpMap.set(new Vector(orig.x() * 2, orig.y()), left);
            scaledUpMap.set(new Vector(orig.x() * 2 + 1, orig.y()), right);
        }

        static ScaledElement from(MapElement unscaled) {
            return switch (unscaled) {
                case BOX -> SCALED_BOX;
                case BLANK -> SCALED_BLANK;
                case WALL -> SCALED_WALL;
                default -> throw new IllegalArgumentException("Cannot scale " + unscaled);
            };
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
            findShovableComponent(direction).ifPresent(component -> {
                component.reversed().forEach(cell -> map.swap(cell, cell.plus(direction)));
                robot = robot.plus(direction);
            });
        }

        long boxesGpsSum() {
            return map.coords()
                    .filter(v -> map.get(v) == MapElement.BOX || map.get(v) == MapElement.BOX_LEFT)
                    .mapToLong(v -> v.y() * 100L + v.x())
                    .sum();
        }

        // Creates a copy
        Warehouse scaleUp() {
            var newMap = ObjGrid.<MapElement>empty(map.width() * 2, map.height());
            map.coords().forEach(c -> ScaledElement.from(map.get(c)).scaleInto(newMap, c));
            return new Warehouse(newMap, new Vector(robot.x() * 2, robot.y()));
        }

        static Warehouse read(CharacterGrid charGrid) {
            var robot = charGrid.find("@").findFirst().map(FindResult::location).orElseThrow();
            // Blank out the robot cell, we store this separately
            var grid = readMap(charGrid);
            return new Warehouse(grid, robot);
        }

        // Component will be returned in order of proximity to the shove
        private Optional<List<Vector>> findShovableComponent(Direction direction) {
            List<Vector> component = new ArrayList<>();

            // We don't add this to the component as we move the robot separately, for better or worse
            List<Vector> lastRow = new ArrayList<>();
            lastRow.add(robot);

            do {
                var row = findNextShovedRow(lastRow, direction);
                if (row.isEmpty()) {
                    return Optional.empty();
                }
                component.addAll(row.get());
                lastRow = row.get();
            } while (!lastRow.isEmpty()); // Empty row indicates no obstacles and we can get to shoving

            return Optional.of(component);
        }

        /**
         * Finds the list of elements the given row will shoved if moved in given direction. A present result means they
         * can be moved and the elements in the list indicate the non-blank cells they will in-turn shove. An empty list
         * implies there's no obstacle. An absent overall result indicates there's an obstacle and the row cannot be
         * shoved.
         */
        private Optional<List<Vector>> findNextShovedRow(List<Vector> current, Direction direction) {
            // We use a set here so we don't need to think about adding stuff twice
            LinkedHashSet<Vector> nextRow = new LinkedHashSet<>();
            for (Vector currentCell : current) {
                var nextCell = currentCell.plus(direction);
                if (!map().inBounds(nextCell)) {
                    throw new IllegalStateException("Pushed off the end of the world");
                }

                var value = map.get(nextCell);

                switch (value) {
                        // Nothing to do
                    case BLANK -> {}
                        // Obstacle, give up
                    case WALL -> {
                        return Optional.empty();
                    }
                    default -> {
                        nextRow.add(nextCell);
                        // If there's an attached element to this element, and it's attached in the direction
                        // perpendicular to the way we're shoving it (i.e. it's in this row), we shove it too
                        value.adjacentElementDirection()
                                .filter(direction::perpendicularTo)
                                .ifPresent(adjDir -> nextRow.add(nextCell.plus(adjDir)));
                    }
                }
            }
            return Optional.of(List.copyOf(nextRow));
        }

        @Override
        public int hashCode() {
            return Objects.hash(map, robot);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Warehouse other) {
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
            var instructionsLine = String.join("", lines.subList(gridLines.size() + 1, lines.size()));

            var charGrid = CharacterGrid.fromCharacterGrid(gridLines);
            var warehouse = Warehouse.read(charGrid);
            var instructions = instructionsLine
                    .chars()
                    .mapToObj(Problem::charToInstruction)
                    .toList();

            return new Problem(warehouse, instructions);
        }

        private static Direction charToInstruction(int character) {
            return switch (character) {
                case '^' -> Direction.N;
                case '>' -> Direction.E;
                case 'v' -> Direction.S;
                case '<' -> Direction.W;
                default -> throw new IllegalArgumentException("Unknown instruction: " + character);
            };
        }
    }
}
