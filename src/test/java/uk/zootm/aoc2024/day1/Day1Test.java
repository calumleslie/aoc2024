package uk.zootm.aoc2024.day1;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class Day1Test {

    private long part1Result;
    private long part2Result;

    @Test
    public void part1() {
        whenDoingPart1("""
                3   4
                4   3
                2   5
                1   3
                3   9
                3   3
                """);

        then(part1Result).isEqualTo(11);
    }

    @Test
    public void part2() {
        whenDoingPart2("""
                3   4
                4   3
                2   5
                1   3
                3   9
                3   3
                """);

        then(part2Result).isEqualTo(31);
    }

    private void whenDoingPart1(String string) {
        Day1.ListPair listPair = Day1.ListPair.parse(string.lines());
        part1Result = Day1.part1(listPair);
    }

    private void whenDoingPart2(String string) {
        Day1.ListPair listPair = Day1.ListPair.parse(string.lines());
        part2Result = Day1.part2(listPair);
    }
}
