package uk.zootm.aoc2024.day19;

import com.google.common.base.Splitter;
import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day19 {
    private static final Splitter TOWEL_SPLITTER = Splitter.on(", ");

    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day19.class.getResource("input"), StandardCharsets.UTF_8);

        var problem = Problem.parse(input.stream());
        System.out.println(problem);

        System.out.printf("Part 1: %d%n", problem.possiblePatterns().size());
        System.out.printf("Part 2: %d%n", problem.waysToFormAllPatterns());
    }

    static class TowelRack {
        private final List<String> towels;
        private final Map<String, Long> waysToFormMemo = new HashMap<>();

        TowelRack(List<String> towels) {
            this.towels = towels;
        }

        static TowelRack of(String... towels) {
            return new TowelRack(List.of(towels));
        }

        List<String> towels() {
            return towels;
        }

        long waysToForm(String pattern) {
            if(waysToFormMemo.containsKey(pattern)) {
                return waysToFormMemo.get(pattern);
            }

            if (pattern.isBlank()) {
                return 1;
            }

            long waysToForm = towels.stream()
                    .filter(pattern::startsWith)
                    .mapToLong(towel -> waysToForm(pattern.substring(towel.length())))
                    .sum();


            waysToFormMemo.put(pattern, waysToForm);

            return waysToForm;
        }

        boolean isPossible(String pattern) {
            return waysToForm(pattern) > 0;
        }
    }

    record Problem(TowelRack towelRack, List<String> patterns) {
        List<String> possiblePatterns() {
            return patterns.stream().filter(towelRack::isPossible).toList();
        }

        long waysToFormAllPatterns() {
            return patterns.stream().mapToLong(towelRack::waysToForm).sum();
        }

        static Problem parse(Stream<String> lines) {
            // Just going to do this in a stupid way

            var towels = new ArrayList<String>();
            var patterns = new ArrayList<String>();

            lines.filter(line -> !line.isBlank())
                    .forEach(line -> {
                        if (line.contains(", ")) {
                            TOWEL_SPLITTER.splitToStream(line).forEach(towels::add);
                        } else {
                            patterns.add(line);
                        }
                    });

            return new Problem(new TowelRack(towels), patterns);
        }
    }
}
