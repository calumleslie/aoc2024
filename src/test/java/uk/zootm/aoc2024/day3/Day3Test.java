package uk.zootm.aoc2024.day3;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day3.Day3.Do;
import uk.zootm.aoc2024.day3.Day3.Dont;
import uk.zootm.aoc2024.day3.Day3.Mul;

public class Day3Test {

    public static final String EXAMPLE1 = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
    public static final String EXAMPLE2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";

    @Test
    public void parseExample1() {
        assertThat(Day3.parse(EXAMPLE1)).containsExactly(new Mul(2, 4), new Mul(5, 5), new Mul(11, 8), new Mul(8, 5));
    }

    @Test
    public void parseExample2() {
        assertThat(Day3.parse(EXAMPLE2))
                .containsExactly(new Mul(2, 4), new Dont(), new Mul(5, 5), new Mul(11, 8), new Do(), new Mul(8, 5));
    }

    @Test
    public void part1_solveExample() {
        var parsed = Day3.parse(EXAMPLE1);
        assertThat(Day3.solve(parsed)).isEqualTo(161);
    }

    @Test
    public void part2_solveExample() {
        var parsed = Day3.parse(EXAMPLE2);
        assertThat(Day3.solve(parsed)).isEqualTo(48);
    }
}
