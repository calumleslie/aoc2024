package uk.zootm.aoc2024.day7;

import com.google.common.primitives.ImmutableLongArray;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day7.Day7.Equation;
import uk.zootm.aoc2024.day7.Day7.Finder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.longs;
import static uk.zootm.aoc2024.day7.Day7.Operator.ADD;
import static uk.zootm.aoc2024.day7.Day7.Operator.CAT;
import static uk.zootm.aoc2024.day7.Day7.Operator.MUL;

public class Day7Test {
    @Test
    public void equation_parse() {
        Equation parsed = Equation.parse("161011: 16 10 13");

        assertThat(parsed).isEqualTo(new Equation(161011, ImmutableLongArray.of(16, 10, 13)));
    }

    @Test
    public void findOperators_addMul_fromExamples() {
        Finder finder = new Finder(ADD, MUL);

        assertThat(finder.findOperators(Equation.parse("190: 10 19"))).containsExactlyInAnyOrder(
                List.of(MUL));

        assertThat(finder.findOperators(Equation.parse("3267: 81 40 27"))).containsExactlyInAnyOrder(
                List.of(ADD, MUL),
                List.of(MUL, ADD));

        assertThat(finder.findOperators(Equation.parse("292: 11 6 16 20"))).containsExactlyInAnyOrder(
                List.of(ADD, MUL, ADD));

        assertThat(finder.findOperators(Equation.parse("7290: 6 8 6 15"))).isEmpty();
    }

    @Test
    public void findOperators_addMulCat_fromExamples() {
        Finder finder = new Finder(ADD, MUL, CAT);

        assertThat(finder.findOperators(Equation.parse("7290: 6 8 6 15"))).containsExactlyInAnyOrder(
                List.of(MUL, CAT, MUL));

        assertThat(finder.findOperators(Equation.parse("156: 15 6"))).containsExactlyInAnyOrder(
                List.of(CAT));

        assertThat(finder.findOperators(Equation.parse("190: 10 19"))).containsExactlyInAnyOrder(
                List.of(MUL));

        assertThat(finder.findOperators(Equation.parse("3267: 81 40 27"))).containsExactlyInAnyOrder(
                List.of(ADD, MUL),
                List.of(MUL, ADD));

        assertThat(finder.findOperators(Equation.parse("292: 11 6 16 20"))).containsExactlyInAnyOrder(
                List.of(ADD, MUL, ADD));
    }

    @Test
    public void part1_example() {
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

    @Test
    public void part2_example() {
        assertThat(Day7.part2("""
                190: 10 19
                3267: 81 40 27
                83: 17 5
                156: 15 6
                7290: 6 8 6 15
                161011: 16 10 13
                192: 17 8 14
                21037: 9 7 18 13
                292: 11 6 16 20
                """.strip().lines())).isEqualTo(11387);
    }

    @Test
    public void hasSuffix_positive() {
        // Range where concatenating two will not exceed a long :)
        var prefixes = longs().between(1, 10_000_000);
        var suffixes = longs().between(0, 10_000_000);

        qt().forAll(prefixes, suffixes).checkAssert((prefix, suffix) -> {
            long concatenated = Long.parseLong(Long.toString(prefix) + Long.toString(suffix));

            assertThat(Day7.hasSuffix(concatenated, suffix)).isTrue();
        });
    }

    @Test
    public void hasSuffix_negative() {
        // Range where concating two will not exceed a long :)
        var completeValues = longs().between(1, 10_000_000);
        var suffixes = longs().between(0, 10_000_000);

        qt().forAll(completeValues, suffixes)
                .assuming((complete, potentialSuffix) -> !Long.toString(complete).endsWith(Long.toString(potentialSuffix)))
                .checkAssert((complete, potentialSuffix) -> {
                    assertThat(Day7.hasSuffix(complete, potentialSuffix)).isFalse();
                });
    }

    @Test
    public void hasSuffix_sameValueDoesNotCount() {
        // Range where concating two will not exceed a long :)
        var longs = longs().between(0, 1_000_000_000);

        qt().forAll(longs)
                .checkAssert(value -> {
                    assertThat(Day7.hasSuffix(value, value)).isFalse();
                });
    }
}
