package uk.zootm.aoc2024.day18;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.grid.Vector;

public class Day18Test {
    private static final String EXAMPLE =
            """
                    5,4
                    4,2
                    4,5
                    3,0
                    2,1
                    6,3
                    2,4
                    1,5
                    0,6
                    3,3
                    2,6
                    5,1
                    1,2
                    5,5
                    2,5
                    6,5
                    1,4
                    0,4
                    6,4
                    1,1
                    6,1
                    1,0
                    0,5
                    1,6
                    2,0
                    """;

    @Test
    public void parseVectors_example() {
        assertThat(Day18.parseVectors(EXAMPLE.strip().lines().limit(5L)))
                .containsExactly(
                        new Vector(5, 4), new Vector(4, 2), new Vector(4, 5), new Vector(3, 0), new Vector(2, 1));
    }

    @Test
    public void shortestPath_example() {
        var firstTwelve = Day18.parseVectors(EXAMPLE.strip().lines()).limit(12).collect(Collectors.toSet());
        // 23 nodes == 22 transitions
        assertThat(Day18.shortestPath(new Vector(7, 7), firstTwelve).get().toPathList())
                .hasSize(23);
    }

    @Test
    public void firstPathBlocker_example() {
        List<Vector> allObstructions =
                Day18.parseVectors(EXAMPLE.strip().lines()).toList();

        assertThat(Day18.firstPathBlocker(new Vector(7, 7), allObstructions)).contains(new Vector(6, 1));
    }
}
