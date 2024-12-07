package uk.zootm.aoc2024.day7;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.common.primitives.ImmutableLongArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Day7 {
    private static final Splitter PREFIX_SEPARATOR = Splitter.on(": ");
    private static final Splitter SPACE = Splitter.on(' ');

    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day7.class.getResource("input"), StandardCharsets.UTF_8);

        System.out.printf("Part 1: %d%n", part1(input.stream()));
    }

    static long part1(Stream<String> lines) {
        return lines.map(Equation::parse)
                .filter(eq -> findOperators(eq).findAny().isPresent())
                .mapToLong(Equation::target)
                .sum();
    }

    static Stream<List<String>> findOperators(Equation equation) {
        return findOperators(equation.target(), equation.values());
    }

    static Stream<List<String>> findOperators(long target, ImmutableLongArray array) {
        Preconditions.checkArgument(!array.isEmpty());

        // Base case; we have found a trivial solution if this is a single element array containing just the target
        if(array.length() == 1) {
            if(array.get(0) == target) {
                return Stream.of(List.of());
            } else {
                return Stream.empty();
            }
        }

        // Other cases we iterate
        long currentValue = array.get(array.length() - 1);
        ImmutableLongArray prefix = array.subArray(0, array.length() - 1);

        long targetFromAddition = target - currentValue;
        Stream<List<String>> solutionsFromAddition = findOperators(targetFromAddition, prefix)
                .map(list -> ImmutableList.<String>builder().addAll(list).add("+").build());

        Stream<List<String>> solutionsFromMultiplication = Stream.empty();
        if(target % currentValue == 0) {
            solutionsFromMultiplication = findOperators(target / currentValue, prefix)
                    .map(list -> ImmutableList.<String>builder().addAll(list).add("*").build());
        }

        return Stream.concat(solutionsFromAddition, solutionsFromMultiplication);
    }

    // Partly using the immutable array here just so equals works
    record Equation(long target, ImmutableLongArray values) {
        static Equation parse(String input) {
            Iterator<String> prefixSuffix = PREFIX_SEPARATOR.split(input).iterator();
            long target = Long.parseLong(prefixSuffix.next());
            long[] values = SPACE.splitToStream(prefixSuffix.next()).mapToLong(Long::parseLong).toArray();

            return new Equation(target, ImmutableLongArray.copyOf(values));
        }
    }
}
