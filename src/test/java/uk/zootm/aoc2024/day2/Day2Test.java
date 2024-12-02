package uk.zootm.aoc2024.day2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class Day2Test {

    @Test
    public void exampleReports() {
        assertSafe("7 6 4 2 1");
        assertUnsafe("1 2 7 8 9");
        assertUnsafe("9 7 6 2 1");
        assertUnsafe("1 3 2 4 5");
        assertUnsafe("8 6 4 4 1");
        assertSafe("1 3 6 7 9");
    }

    private void assertSafe(String reportString) {
        var report = Day2.parseReport(reportString);
        then(Day2.isSafe(report)).isTrue().describedAs("Should be safe: [%s]", reportString);
    }

    private void assertUnsafe(String reportString) {
        var report = Day2.parseReport(reportString);
        then(Day2.isSafe(report)).isFalse().describedAs("Should be unsafe: [%s]", reportString);
    }
}
