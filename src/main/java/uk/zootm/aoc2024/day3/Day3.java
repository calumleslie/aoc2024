package uk.zootm.aoc2024.day3;

import com.google.common.io.Resources;
import uk.zootm.aoc2024.day2.Day2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 {
    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day3.class.getResource("input"), StandardCharsets.UTF_8)
                .stream().collect(Collectors.joining());

        var parsed = parse(input);

        System.out.printf("Part 1: %d%n", part1(parsed));
    }

    // If you see me writing horrible regexes, no you didn't
    private static final Pattern MUL_PATTERN = Pattern.compile("""
                    mul\\(
                        ([1-9][0-9]{0,2})
                        ,
                        ([1-9][0-9]{0,2})
                    \\)
                    """, Pattern.COMMENTS);

    static int part1(List<Mul> muls) {
        return muls.stream().mapToInt(Mul::result).sum();
    }

    static List<Mul> parse(String line) {
        var result = new ArrayList<Mul>();
        var matcher = MUL_PATTERN.matcher(line);
        while(matcher.find()) {
            int left = Integer.parseInt(matcher.group(1));
            int right = Integer.parseInt(matcher.group(2));
            result.add(new Mul(left, right));
        }
        return result;
    }

    record Mul(int left, int right) {
        int result() {
            return left * right;
        }
    }
}
