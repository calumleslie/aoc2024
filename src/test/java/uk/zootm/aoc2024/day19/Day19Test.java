package uk.zootm.aoc2024.day19;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day19.Day19.Problem;
import uk.zootm.aoc2024.day19.Day19.TowelRack;

public class Day19Test {

    @Test
    public void parse_example() {
        var problem = problemOf(
                """
                        r, wr, b, g, bwu, rb, gb, br
                        
                        brwrr
                        bggr
                        gbbr
                        rrbgbr
                        ubwu
                        bwurrg
                        brgr
                        bbrgwb
                        """);

        assertThat(problem.towelRack().towels()).containsExactly("r", "wr", "b", "g", "bwu", "rb", "gb", "br");
        assertThat(problem.patterns()).containsExactly("brwrr", "bggr", "gbbr", "rrbgbr", "ubwu", "bwurrg", "brgr", "bbrgwb");
    }

    @Test
    public void isPossible_examples() {
        var towels = TowelRack.of("r", "wr", "b", "g", "bwu", "rb", "gb", "br");

        assertThat(towels.isPossible("brwrr")).isTrue();
        assertThat(towels.isPossible("bggr")).isTrue();
        assertThat(towels.isPossible("gbbr")).isTrue();
        assertThat(towels.isPossible("rrbgbr")).isTrue();
        assertThat(towels.isPossible("ubwu")).isFalse();
        assertThat(towels.isPossible("bwurrg")).isTrue();
        assertThat(towels.isPossible("brgr")).isTrue();
        assertThat(towels.isPossible("bbrgwb")).isFalse();
    }

    @Test
    public void possiblePatterns_example() {
        assertThat(problemOf(
                """
                        r, wr, b, g, bwu, rb, gb, br
                        
                        brwrr
                        bggr
                        gbbr
                        rrbgbr
                        ubwu
                        bwurrg
                        brgr
                        bbrgwb
                        """)
                .possiblePatterns())
                .containsExactly("brwrr", "bggr", "gbbr", "rrbgbr", "bwurrg", "brgr");
    }

    private Problem problemOf(String input) {
        return Problem.parse(input.strip().lines());
    }
}
