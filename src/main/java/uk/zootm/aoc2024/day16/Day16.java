package uk.zootm.aoc2024.day16;

import com.google.common.io.Resources;
import uk.zootm.aoc2024.graph.DijkstraSolver;
import uk.zootm.aoc2024.graph.DijkstraSolver.Path;
import uk.zootm.aoc2024.graph.DirectedGraph;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Grid;
import uk.zootm.aoc2024.grid.ObjGrid;
import uk.zootm.aoc2024.grid.Vector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Day16 {

    public static void main(String[] args) throws IOException {
        var lines = Resources.readLines(Day16.class.getResource("input"), StandardCharsets.UTF_8);
        var grid = CharacterGrid.fromCharacterGrid(lines);
        var maze = readMaze(grid);

        var paths = minCostPaths(maze);
        System.out.printf("Part 1: %d%n", paths.getFirst().totalCost());

        var uniqueLocations = paths.stream().flatMap(Day16::trace).map(Location::vector).distinct().count();
        System.out.printf("Part 2: %d%n", uniqueLocations);
    }

    static long uniqueLocations(List<Path<Location>> paths) {
        return paths.stream().flatMap(Day16::trace).map(Location::vector).distinct().count();
    }

    static Grid<String> draw(Grid<String> base, Path<Location> path) {
        var result = ObjGrid.from(base, x -> x);
        trace(path).forEach(loc -> result.set(loc.vector(), symbol(loc.direction())));
        return result;
    }

    private static String symbol(Direction d) {
        return switch (d) {
            case N -> "^";
            case E -> ">";
            case S -> "v";
            case W -> "<";
            default -> throw new IllegalArgumentException("Can't handle " + d);
        };
    }

    // Approach: Generate graph of all transitions, solve using Dijkstra's algorithm
    // Transitions include turns so each "node" counts as a single transition with cost

    static List<Path<Location>> minCostPaths(Maze maze) {
        var startLoc = new Location(maze.start(), Direction.E);
        var solver = new DijkstraSolver<Long>(x -> x);

        return solver.pathsToMatchingNode(maze.map(), startLoc, loc -> loc.vector().equals(maze.end()));
    }

    private static Stream<Location> trace(Path<Location> path) {
        return path.toPathList().stream().flatMap(Day16::traceSingle);
    }

    private static Stream<Location> traceSingle(Path<Location> segment) {
        if(segment.prefix() == null) {
            return Stream.of(segment.end());
        } else {
            var startVector = segment.prefix().end().vector;
            var start = new Location(startVector, segment.end().direction());
            var tooFar = segment.end().step();
            return Stream.iterate(start, Location::step).takeWhile(loc -> !loc.equals(tooFar));
        }
    }

    static Maze readMaze(CharacterGrid grid) {
        return new MazeReader(grid).read();
    }

    private record MazeReader(CharacterGrid grid) {
        private static final long TURN_COST = 1000L;
        private static final long MOVE_COST = 1L;

        public Maze read() {
            DirectedGraph<Location, Long> map = new DirectedGraph<>();

            var corners = findCorners(grid).toList();
            corners.stream().forEach(c -> addCorner(map, c));
            corners.stream().flatMap(this::findConnected).forEach(conn -> addConnection(map, conn));

            var start = grid.find("S").findFirst().orElseThrow().location();
            var end = grid.find("E").findFirst().orElseThrow().location();

            return new Maze(map, start, end);
        }

        private Stream<Connected> findConnected(Vector corner) {
            return Direction.cardinal().stream()
                    .map(dir -> new Location(corner, dir))
                    .flatMap(loc -> findConnected(loc).stream());
        }

        // We could just generate loads of single transitions but this is more ~efficient~
        private Optional<Connected> findConnected(Location location) {
            var start = location.vector();
            var dir = location.direction();
            // Could also pass list of corners here but this seems fast enough for now
            var nextCorner = Stream.iterate(start.plus(dir), curr -> curr.plus(dir))
                    .takeWhile(this::isPassable)
                    .filter(this::isCorner)
                    .findFirst();

            // Is the floating point here going to cause a problem? Probably not at these sizes
            return nextCorner.map(end -> new Connected(
                    location, new Location(end, dir), (long) end.minus(start).magnitude()));
        }

        private void addConnection(DirectedGraph<Location, Long> graph, Connected connected) {
            graph.addEdge(connected.from(), connected.to(), connected.length() * MOVE_COST);
            graph.addEdge(connected.to().flip(), connected.from().flip(), connected.length() * MOVE_COST);
        }

        private void addCorner(DirectedGraph<Location, Long> graph, Vector corner) {
            // We add a vertex for each direction and connect them to where you can turn to
            Location.allAtPoint(corner).forEach(loc -> {
                graph.addEdge(loc, new Location(corner, loc.direction().clockwise90()), TURN_COST);
                graph.addEdge(loc, new Location(corner, loc.direction().anticlockwise90()), TURN_COST);
            });
        }

        private Stream<Vector> findCorners(Grid<String> grid) {
            return grid.coords().filter(this::isCorner);
        }

        private boolean isCorner(Vector location) {
            // We _should_ add S and E as extra corners in case they appear in a non-corner position, but they do not
            // so I'm not doing it
            if (!isPassable(location)) {
                return false;
            }

            List<Direction> joinedDirections = Direction.cardinal().stream()
                    .filter(dir -> isPassable(location.plus(dir)))
                    .toList();

            if (joinedDirections.size() == 2) {
                // We're a corner iff this doesn't continue a line
                return !joinedDirections.get(0).opposite().equals(joinedDirections.get(1));
            } else {
                // The odd case here is 0 but the bot may as well spin in a circle for a bit
                return true;
            }
        }

        private boolean isPassable(Vector location) {
            if (!grid.inBounds(location)) {
                return false;
            }
            var code = grid.get(location);
            return code.equals(".") || code.equals("S") || code.equals("E");
        }
    }

    record Maze(DirectedGraph<Location, Long> map, Vector start, Vector end) {}

    record Location(Vector vector, Direction direction) {
        Location(int x, int y, Direction direction) {
            this(new Vector(x, y), direction);
        }

        static Stream<Location> allAtPoint(Vector vector) {
            return Direction.cardinal().stream().map(dir -> new Location(vector, dir));
        }

        Location step() {
            return new Location(vector.plus(direction), direction);
        }

        Location flip() {
            return new Location(vector, direction().opposite());
        }
    }

    private record Connected(Location from, Location to, long length) {}
}
