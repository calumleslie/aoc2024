package uk.zootm.aoc2024.day12;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Preconditions;
import java.util.Set;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day12.Day12.Corner;
import uk.zootm.aoc2024.day12.Day12.Segment;
import uk.zootm.aoc2024.grid.CharacterGrid;
import uk.zootm.aoc2024.grid.Vector;

public class Day12Test {
    private static final String EXAMPLE =
            """
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
        assertThat(Day12.findSegment(grid, new Vector(4, 0)))
                .isEqualTo(new Segment(
                        "I", Set.of(new Vector(4, 0), new Vector(5, 0), new Vector(4, 1), new Vector(5, 1))));
    }

    @Test
    public void findSegments_fromExample() {
        var grid = CharacterGrid.fromString(EXAMPLE);

        // One of the smallest ones because I'm going to lose my mind otherwise
        assertThat(Day12.findSegments(grid).stream().map(ResultParts::from))
                .containsExactlyInAnyOrder(
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

    @Test
    public void findCornersForCell_variousExamples() {
        assertCornersAre(
                """
                OOO
                OXO
                OOO
                """,
                Corner.NE,
                Corner.NW,
                Corner.SW,
                Corner.SE);

        assertCornersAre(
                """
                OXO
                XXX
                OXO
                """,
                Corner.NE,
                Corner.NW,
                Corner.SW,
                Corner.SE);

        assertCornersAre(
                """
                XOX
                OXO
                XOX
                """,
                Corner.NE,
                Corner.NW,
                Corner.SW,
                Corner.SE);

        assertCornersAre(
                """
                XOO
                OXO
                OOO
                """,
                Corner.NE,
                Corner.NW,
                Corner.SW,
                Corner.SE);

        assertCornersAre(
                """
                OXO
                OXO
                OOO
                """,
                Corner.SW,
                Corner.SE);

        assertCornersAre(
                """
                XXX
                XXO
                OOO
                """, Corner.SE);
    }

    @Test
    public void doubleCorner() {
        CharacterGrid grid = CharacterGrid.fromString(
                """
                        XXO
                        XOX
                        XXX
                        """);

        Segment x = Day12.findSegment(grid, new Vector(0, 0));

        assertThat(x.cornerLocations()).contains(new Vector(2, 1), new Vector(2, 1));
    }

    private void assertCornersAre(String gridString, Corner... corner) {
        CharacterGrid grid = CharacterGrid.fromString(gridString);

        Preconditions.checkArgument(grid.height() == 3 && grid.width() == 3);

        Segment segment = Day12.findSegment(grid, new Vector(1, 1));

        assertThat(segment.corners(new Vector(1, 1))).containsExactlyInAnyOrder(corner);
    }

    @Test
    public void findSegmentsWithDiscount_fromExample() {
        var grid = CharacterGrid.fromString(EXAMPLE);

        // One of the smallest ones because I'm going to lose my mind otherwise
        assertThat(Day12.findSegments(grid).stream().map(ResultParts::fromWithDiscount))
                .containsExactlyInAnyOrder(
                        new ResultParts("R", 12, 10),
                        new ResultParts("I", 4, 4),
                        new ResultParts("C", 14, 22),
                        new ResultParts("F", 10, 12),
                        new ResultParts("V", 13, 10),
                        new ResultParts("J", 11, 12),
                        new ResultParts("C", 1, 4),
                        new ResultParts("E", 13, 8),
                        new ResultParts("I", 14, 16),
                        new ResultParts("M", 5, 6),
                        new ResultParts("S", 3, 6));
    }

    record ResultParts(String element, long area, long perimeter) {
        static ResultParts from(Segment segment) {
            return new ResultParts(segment.element(), segment.area(), segment.perimeter());
        }

        static ResultParts fromWithDiscount(Segment segment) {
            return new ResultParts(
                    segment.element(), segment.area(), segment.cornerLocations().count());
        }
    }
}
