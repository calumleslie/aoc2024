package uk.zootm.aoc2024.day3;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day3.Day3.Mul;

import static org.assertj.core.api.Assertions.assertThat;

public class Day3Test {

    public static final String EXAMPLE = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";

    @Test
    public void parseExample() {
        assertThat(Day3.parse(EXAMPLE))
                .containsExactly(
                        new Mul(2, 4),
                        new Mul(5, 5),
                        new Mul(11, 8),
                        new Mul(8, 5));
    }

    @Test
    public void part1_solveExample() {
        var parsed = Day3.parse(EXAMPLE);
        assertThat(Day3.part1(parsed)).isEqualTo(161);
    }
}
