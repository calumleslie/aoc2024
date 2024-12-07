package uk.zootm.aoc2024.day7;

import com.google.common.primitives.ImmutableLongArray;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day7.Day7.Equation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day7Test {
    @Test
    public void equation_parse() {
        Equation parsed = Equation.parse("161011: 16 10 13");

        assertThat(parsed).isEqualTo(new Equation(161011, ImmutableLongArray.of(16, 10, 13)));
    }

    @Test
    public void findOperators_fromExamples() {
        assertThat(Day7.findOperators(Equation.parse("190: 10 19"))).containsExactlyInAnyOrder(
                List.of("*"));

        assertThat(Day7.findOperators(Equation.parse("3267: 81 40 27"))).containsExactlyInAnyOrder(
                List.of("+", "*"),
                List.of("*", "+"));

        assertThat(Day7.findOperators(Equation.parse("292: 11 6 16 20"))).containsExactlyInAnyOrder(
                List.of("+", "*", "+"));
    }

    @Test
    public void findOperators_fullExample() {
        assertThat(Day7.part1("""
                190: 10 19
                3267: 81 40 27
                83: 17 5
                156: 15 6
                7290: 6 8 6 15
                161011: 16 10 13
                192: 17 8 14
                21037: 9 7 18 13
                292: 11 6 16 20
                """.strip().lines())).isEqualTo(3749);
    }
}
