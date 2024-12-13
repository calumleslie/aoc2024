package uk.zootm.aoc2024.grid;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class VectorTest {

    @Test
    public void minimize_positive() {
        assertThat(new Vector(100, 50).minimize()).isEqualTo(new Vector(2, 1));
    }

    @Test
    public void minimize_negative() {
        assertThat(new Vector(-99, -33).minimize()).isEqualTo(new Vector(-3, -1));
    }

    @Test
    public void minimize_withZero() {
        assertThat(new Vector(1_000_000, 0).minimize()).isEqualTo(new Vector(1, 0));
    }
}
