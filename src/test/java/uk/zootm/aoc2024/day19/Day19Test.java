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

    @Test
    public void waysToForm_example() {
        var towels = TowelRack.of("r", "wr", "b", "g", "bwu", "rb", "gb", "br");

        assertThat(towels.waysToForm("brwrr")).isEqualTo(2L);
        assertThat(towels.waysToForm("bggr")).isEqualTo(1L);
        assertThat(towels.waysToForm("gbbr")).isEqualTo(4L);
        assertThat(towels.waysToForm("rrbgbr")).isEqualTo(6L);
        assertThat(towels.waysToForm("ubwu")).isEqualTo(0L);
        assertThat(towels.waysToForm("bwurrg")).isEqualTo(1L);
        assertThat(towels.waysToForm("brgr")).isEqualTo(2L);
        assertThat(towels.waysToForm("bbrgwb")).isEqualTo(0L);
    }


    @Test
    public void waysToFormAllPatterns_example() {
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
                .waysToFormAllPatterns())
                .isEqualTo(16L);
    }

    private Problem problemOf(String input) {
        return Problem.parse(input.strip().lines());
    }
}
