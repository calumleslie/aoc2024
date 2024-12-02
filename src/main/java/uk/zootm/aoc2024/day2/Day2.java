package uk.zootm.aoc2024.day2;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Day2 {
    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day2.class.getResource("input"), StandardCharsets.UTF_8);

        var numSafe = input.stream()
                .map(Day2::parseReport)
                .filter(Day2::isSafe)
                .count();

        System.out.printf("Part 1: %d%n", numSafe);
    }

    static boolean isSafe(long[] report) {
        int signum = 0;
        for (int i = 0; i < report.length - 1; i++) {
            long difference = report[i + 1] - report[i];

            int diffSignum = Long.signum(difference);
            if (signum == 0) {
                signum = diffSignum;
            }

            if (difference == 0) {
                return false;
            }

            if (diffSignum != signum) {
                // Changed direction
                return false;
            }

            if (Math.abs(difference) > 3) {
                return false;
            }
        }
        return true;
    }

    public static long[] parseReport(String report) {
        return Arrays.stream(report.split("\s+", -1))
                .mapToLong(Long::parseLong)
                .toArray();
    }
}
