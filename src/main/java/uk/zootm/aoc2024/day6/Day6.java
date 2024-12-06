package uk.zootm.aoc2024.day6;

import com.google.common.io.Resources;
import uk.zootm.aoc2024.day4.Day4;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Grid;
import uk.zootm.aoc2024.grid.Vector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Day6 {
    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day6.class.getResource("input"), StandardCharsets.UTF_8);
        var grid = Grid.fromCharacterGrid(input);

        System.out.printf("Part 1: %d%n", walk(grid).stream().distinct().count());
    }

    static List<Vector> walk(Grid grid) {
        Vector start = grid.find("^").findFirst().orElseThrow().location();

        Direction direction = Direction.N;
        Vector current = start;
        var path = new ArrayList<Vector>();

        while (grid.inBounds(current)) {
            path.add(current);
            direction = findDirection(grid, current, direction);
            current = current.plus(direction.vector());
        }

        return path;
    }

    private static Direction findDirection(Grid grid, Vector location, Direction currentDirection) {
        Direction result = currentDirection;
        while (!canMove(grid, location, result)) {
            result = result.clockwise90();
            if (result.equals(currentDirection)) {
                throw new IllegalArgumentException("Cannot move from: " + location);
            }
        }
        return result;
    }

    private static boolean canMove(Grid grid, Vector location, Direction direction) {
        Vector destination = location.plus(direction.vector());
        // We can move off-grid, or into any space which is not an obstacle
        return !grid.inBounds(destination) || !grid.get(destination).equals("#");
    }
}
