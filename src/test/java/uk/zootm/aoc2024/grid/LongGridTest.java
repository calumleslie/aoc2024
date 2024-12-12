package uk.zootm.aoc2024.grid;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LongGridTest {
    @Test
    public void fromStringGrid_simple() {
        CharacterGrid stringGrid = CharacterGrid.fromString(
                """
                        123
                        456
                        789
                        """);

        assertThat(LongGrid.fromStringGrid(stringGrid))
                .isEqualTo(new LongGrid(new long[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, 3));
    }
}
