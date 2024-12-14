package uk.zootm.aoc2024.day13;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day13.Day13.Problem;
import uk.zootm.aoc2024.day13.Day13.Solution;

public class Day13Test {
    @Test
    public void problem_parseExample() {
        var problems = Problem.parse(
                """
                Button A: X+94, Y+34
                Button B: X+22, Y+67
                Prize: X=8400, Y=5400

                Button A: X+26, Y+66
                Button B: X+67, Y+21
                Prize: X=12748, Y=12176

                Button A: X+17, Y+86
                Button B: X+84, Y+37
                Prize: X=7870, Y=6450

                Button A: X+69, Y+23
                Button B: X+27, Y+71
                Prize: X=18641, Y=10279
                """);

        assertThat(problems)
                .containsExactly(
                        new Problem(94, 34, 22, 67, 8400, 5400),
                        new Problem(26, 66, 67, 21, 12748, 12176),
                        new Problem(17, 86, 84, 37, 7870, 6450),
                        new Problem(69, 23, 27, 71, 18641, 10279));
    }

    @Test
    public void pressesToWin_fromExamples() {
        assertThat(new Problem(94, 34, 22, 67, 8400, 5400).pressesToWin()).contains(new Solution(80, 40));

        assertThat(new Problem(26, 66, 67, 21, 12748, 12176).pressesToWin()).isEmpty();

        assertThat(new Problem(17, 86, 84, 37, 7870, 6450).pressesToWin()).contains(new Solution(38, 86));

        assertThat(new Problem(69, 23, 27, 71, 18641, 10279).pressesToWin()).isEmpty();
    }

    @Test
    public void part2_examples() {
        assertThat(new Problem(94, 34, 22, 67, 8400, 5400).withPart2Correction().pressesToWin())
                .isEmpty();

        assertThat(new Problem(26, 66, 67, 21, 12748, 12176)
                        .withPart2Correction()
                        .pressesToWin())
                .isPresent();

        assertThat(new Problem(17, 86, 84, 37, 7870, 6450).withPart2Correction().pressesToWin())
                .isEmpty();

        assertThat(new Problem(69, 23, 27, 71, 18641, 10279)
                        .withPart2Correction()
                        .pressesToWin())
                .isPresent();
    }

    @Test
    public void exactA_inexactB_shouldNotHaveSolution() {
        assertThat(new Problem(74, 18, 54, 78, 10000000005322L, 10000000003954L).pressesToWin())
                .isEmpty();
    }

    @Test
    public void pressesToWinUpTo100_example() {
        assertThat(Day13.costToWin(Stream.of(
                        new Problem(94, 34, 22, 67, 8400, 5400),
                        new Problem(26, 66, 67, 21, 12748, 12176),
                        new Problem(17, 86, 84, 37, 7870, 6450),
                        new Problem(69, 23, 27, 71, 18641, 10279))))
                .isEqualTo(480);
    }
}
