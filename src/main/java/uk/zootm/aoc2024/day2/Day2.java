package uk.zootm.aoc2024.day2;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day2.class.getResource("input"), StandardCharsets.UTF_8);

        var numSafe = input.stream().map(Day2::parseReport).filter(Day2::isSafe).count();

        System.out.printf("Part 1: %d%n", numSafe);

        var numSafeDampened = input.stream()
                .map(Day2::parseReport)
                .filter(Day2::isSafeDampened)
                .count();

        System.out.printf("Part 2: %d%n", numSafeDampened);
    }

    static boolean isSafeDampened(long[] report) {
        int problematicLevel = firstProblematicLevel(report);
        if (problematicLevel == -1) {
            return true;
        }

        // We know it's this transition which is bad so we can try removing the level before or after

        List<long[]> toCheck = new ArrayList<>();
        toCheck.add(removeIndex(report, problematicLevel));
        toCheck.add(removeIndex(report, problematicLevel + 1));
        if (problematicLevel == 1) {
            // Special case: We may have just decided the direction was wrong due to the transition before so cram 0
            // in as well
            toCheck.add(removeIndex(report, 0));
        }
        return toCheck.stream().anyMatch(Day2::isSafe);
    }

    static long[] removeIndex(long[] report, int index) {
        long[] newReport = new long[report.length - 1];
        System.arraycopy(report, 0, newReport, 0, index);
        System.arraycopy(report, index + 1, newReport, index, report.length - index - 1);
        return newReport;
    }

    static boolean isSafe(long[] report) {
        return firstProblematicLevel(report) == -1;
    }

    private static int firstProblematicLevel(long[] report) {
        int signum = 0;
        for (int i = 0; i < report.length - 1; i++) {
            long difference = report[i + 1] - report[i];

            int diffSignum = Long.signum(difference);
            if (signum == 0) {
                signum = diffSignum;
            }

            if (difference == 0) {
                return i;
            }

            if (diffSignum != signum) {
                // Changed direction
                return i;
            }

            if (Math.abs(difference) > 3) {
                return i;
            }
        }
        return -1;
    }

    public static long[] parseReport(String report) {
        return Arrays.stream(report.split("\s+", -1)).mapToLong(Long::parseLong).toArray();
    }
}
