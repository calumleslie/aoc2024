package uk.zootm.aoc2024.day3;

import com.google.common.io.Resources;

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

        // Part 1 only supports mul
        var part1Input = parsed.stream().filter(op -> op instanceof Mul).collect(Collectors.toList());
        System.out.printf("Part 1: %d%n", solve(part1Input));
        System.out.printf("Part 2: %d%n", solve(parsed));
    }

    // If you see me writing horrible regexes, no you didn't
    private static final Pattern MUL_PATTERN = Pattern.compile("""
            (
                (?<do>do\\(\\))
                |
                (?<dont>don't\\(\\))
                |
                (?<mul>mul\\(
                        (?<left>[1-9][0-9]{0,2})
                        ,
                        (?<right>[1-9][0-9]{0,2})
                    \\)
                )
            )
            """, Pattern.COMMENTS);

    static int solve(List<Op> ops) {
        int sum = 0;
        boolean doing = true;

        for (var op : ops) {
            switch (op) {
                case Mul mul -> sum += doing ? mul.result() : 0;
                case Do d -> doing = true;
                case Dont d -> doing = false;
                default -> throw new IllegalArgumentException("unknown op " + op);
            }
        }

        return sum;
    }

    static List<Op> parse(String line) {
        var result = new ArrayList<Op>();
        var matcher = MUL_PATTERN.matcher(line);
        while (matcher.find()) {
            if (matcher.group("do") != null) {
                result.add(new Do());
            }
            if (matcher.group("dont") != null) {
                result.add(new Dont());
            }
            if (matcher.group("mul") != null) {
                int left = Integer.parseInt(matcher.group("left"));
                int right = Integer.parseInt(matcher.group("right"));
                result.add(new Mul(left, right));
            }
        }
        return result;
    }

    // Just a marker. Tempting to use sealed classes but those don't seem to work with records and I am not willing to
    // give up records.
    interface Op {
    }

    record Dont() implements Op {
    }

    record Do() implements Op {
    }

    record Mul(int left, int right) implements Op {
        int result() {
            return left * right;
        }
    }
}
