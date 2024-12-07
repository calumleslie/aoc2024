package uk.zootm.aoc2024.day4;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.zootm.aoc2024.grid.Direction;
import uk.zootm.aoc2024.grid.Grid;
import uk.zootm.aoc2024.grid.Vector;

/**
 * What's going on with the complicated "Graphemes" stuff in this one? I wanted to see if I could make this work
 * "correctly" with respect to Unicode without losing the O(1) "cell" lookup in the grid. This is easy to do with
 * Unicode codepoints but these are not what people generally consider to be a "character", so instead the system
 * indexes grapheme clusters within strings ahead of time (this is what the Graphemes class does).
 */
public class Day4 {

    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day4.class.getResource("input"), StandardCharsets.UTF_8);
        var grid = Grid.fromCharacterGrid(input);

        System.out.printf("Part 1: %d%n", grid.find("XMAS").count());
        System.out.printf("Part 2: %d%n", countXMases(grid));
    }

    static long countXMases(Grid grid) {
        return grid.coords().filter(coord -> hasXMas(grid, coord)).count();
    }

    // I'm not generalizing this so this is not going on the grid class :)
    static boolean hasXMas(Grid grid, Vector centre) {
        if (!grid.get(centre).equals("A")) {
            return false;
        }

        List<Vector> corners = Stream.of(Direction.NE, Direction.SE, Direction.SW, Direction.NW)
                .map(dir -> centre.plus(dir.vector()))
                .collect(Collectors.toUnmodifiableList());

        if (!corners.stream().allMatch(grid::inBounds)) {
            return false;
        }

        long mases = corners.stream()
                .filter(corner -> grid.get(corner).equals("M"))
                .filter(corner -> grid.get(opposite(centre, corner)).equals("S"))
                .count();

        return mases == 2L;
    }

    private static Vector opposite(Vector centre, Vector source) {
        return centre.minus(source.minus(centre));
    }
}
