package uk.zootm.aoc2024.day12;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day12.Day12.Segment;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Vector;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class Day12Test {
    private static final String EXAMPLE = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """;

    @Test
    public void findSegment_fromExample() {
        var grid = CharacterGrid.fromString(EXAMPLE);

        // One of the smallest ones because I'm going to lose my mind otherwise
        assertThat(Day12.findSegment(grid, new Vector(4, 0))).isEqualTo(new Segment("I", Set.of(
                new Vector(4, 0),
                new Vector(5, 0),
                new Vector(4, 1),
                new Vector(5, 1))));
    }

    @Test
    public void findSegments_fromExample() {
        var grid = CharacterGrid.fromString(EXAMPLE);

        // One of the smallest ones because I'm going to lose my mind otherwise
        assertThat(Day12.findSegments(grid).stream().map(ResultParts::from)).containsExactlyInAnyOrder(
                new ResultParts("R", 12, 18),
                new ResultParts("I", 4, 8),
                new ResultParts("C", 14, 28),
                new ResultParts("F", 10, 18),
                new ResultParts("V", 13, 20),
                new ResultParts("J", 11, 20),
                new ResultParts("C", 1, 4),
                new ResultParts("E", 13, 18),
                new ResultParts("I", 14, 22),
                new ResultParts("M", 5, 12),
                new ResultParts("S", 3, 8));
    }

    record ResultParts(String element, long area, long perimeter) {
        static ResultParts from(Segment segment) {
            return new ResultParts(segment.element(), segment.area(), segment.perimeter());
        }
    }
}
