package uk.zootm.aoc2024.day11;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class Day11Test {

    @Test
    public void blink_single_zero() {
        assertThat(Day11.blink(0L)).containsExactly(1L);
    }

    @Test
    public void blink_single_evenDigits() {
        assertThat(Day11.blink(2000)).containsExactly(20L, 0L);
        assertThat(Day11.blink(2024)).containsExactly(20L, 24L);
    }

    @Test
    public void blink_single_otherValues() {
        assertThat(Day11.blink(1L)).containsExactly(2024L);
        assertThat(Day11.blink(123L)).containsExactly(2024L * 123L);
    }

    @Test
    public void blink_array() {
        assertThat(Day11.blink(new long[] {253, 0, 2024, 14168})).containsExactly(512072, 1, 20, 24, 28676032);
    }

    @Test
    public void blinkTimes_examples() {
        long[] initial = new long[] {125, 17};

        assertThat(Day11.blinkTimes(initial, 6))
                .containsExactly(
                        2097446912, 14168, 4048, 2, 0, 2, 4, 40, 48, 2024, 40, 48, 80, 96, 2, 8, 6, 7, 6, 0, 3, 2);
        assertThat(Day11.blinkTimes(initial, 25)).hasSize(55312);
    }
}
