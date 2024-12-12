package uk.zootm.aoc2024.day2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class Day2Test {

    @Test
    public void part1_exampleReports() {
        assertSafe("7 6 4 2 1");
        assertUnsafe("1 2 7 8 9");
        assertUnsafe("9 7 6 2 1");
        assertUnsafe("1 3 2 4 5");
        assertUnsafe("8 6 4 4 1");
        assertSafe("1 3 6 7 9");
    }

    @Test
    public void part2_exampleReports() {
        assertSafeDampened("7 6 4 2 1");
        assertUnsafeDampened("1 2 7 8 9");
        assertUnsafeDampened("9 7 6 2 1");
        assertSafeDampened("1 3 2 4 5");
        assertSafeDampened("8 6 4 4 1");
        assertSafeDampened("1 3 6 7 9");
    }

    @Test
    public void part2_myOwnExamples() {
        assertSafeDampened("48 46 47 49 51 54 56");
    }

    @Test
    public void removeIndex() {
        long[] removed = Day2.removeIndex(new long[] {0, 1, 2, 3, 4, 5}, 2);
        then(removed).containsExactly(0, 1, 3, 4, 5);
    }

    private void assertSafe(String reportString) {
        var report = Day2.parseReport(reportString);
        then(Day2.isSafe(report)).isTrue().describedAs("Should be safe: [%s]", reportString);
    }

    private void assertUnsafe(String reportString) {
        var report = Day2.parseReport(reportString);
        then(Day2.isSafe(report)).isFalse().describedAs("Should be unsafe: [%s]", reportString);
    }

    private void assertSafeDampened(String reportString) {
        var report = Day2.parseReport(reportString);
        then(Day2.isSafeDampened(report)).isTrue().describedAs("Should be safe: [%s]", reportString);
    }

    private void assertUnsafeDampened(String reportString) {
        var report = Day2.parseReport(reportString);
        then(Day2.isSafeDampened(report)).isFalse().describedAs("Should be unsafe: [%s]", reportString);
    }
}
