package uk.zootm.aoc2024.day7;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.common.primitives.ImmutableLongArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Stream;

public class Day7 {
    private static final Splitter PREFIX_SEPARATOR = Splitter.on(": ");
    private static final Splitter SPACE = Splitter.on(' ');

    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day7.class.getResource("input"), StandardCharsets.UTF_8);

        System.out.printf("Part 1: %d%n", part1(input.stream()));
        System.out.printf("Part 2: %d%n", part2(input.stream()));
    }

    static long part1(Stream<String> lines) {
        Finder finder = new Finder(Operator.ADD, Operator.MUL);

        return lines.map(Equation::parse)
                .filter(eq -> finder.findOperators(eq).findAny().isPresent())
                .mapToLong(Equation::target)
                .sum();
    }

    static long part2(Stream<String> lines) {
        Finder finder = new Finder(Operator.ADD, Operator.MUL, Operator.CAT);

        return lines.map(Equation::parse)
                .filter(eq -> finder.findOperators(eq).findAny().isPresent())
                .mapToLong(Equation::target)
                .sum();
    }

    enum Operator {
        ADD {
            @Override
            OptionalLong nextTarget(long target, long currentValue) {
                return OptionalLong.of(target - currentValue);
            }
        },
        MUL {
            @Override
            OptionalLong nextTarget(long target, long currentValue) {
                if (target % currentValue == 0) {
                    return OptionalLong.of(target / currentValue);
                } else {
                    return OptionalLong.empty();
                }
            }
        },
        CAT {
            OptionalLong nextTarget(long target, long currentValue) {
                // We can't form a suffix of a negative number as it would have a negative symbol in the middle
                if (currentValue >= 0 && hasSuffix(target, currentValue)) {
                    long currentValueLength = lengthBase10(currentValue);

                    return OptionalLong.of((target - currentValue) / (long) Math.pow(10, currentValueLength));
                } else {
                    return OptionalLong.empty();
                }
            }
        };

        abstract OptionalLong nextTarget(long target, long currentValue);
    }

    // Strict suffix, i.e. the values cannot be equal
    static boolean hasSuffix(long target, long suffix) {
        // In hindsight doing this with strings would have been much easier
        long targetLength = lengthBase10(target);
        long suffixLength = lengthBase10(suffix);
        long remainder = target % (long) Math.pow(10, suffixLength);

        return targetLength > suffixLength && remainder == suffix;
    }

    private static long lengthBase10(long value) {
        return value == 0 ? 1L : 1L + (long) Math.floor(Math.log10(value));
    }

    static class Finder {
        private final EnumSet<Operator> operators;

        Finder(Operator first, Operator... rest) {
            this.operators = EnumSet.of(first, rest);
        }

        Finder(EnumSet<Operator> operators) {
            this.operators = operators;
        }

        Stream<List<Operator>> findOperators(Equation equation) {
            return findOperators(equation.target(), equation.values());
        }

        Stream<List<Operator>> findOperators(long target, ImmutableLongArray array) {
            Preconditions.checkArgument(!array.isEmpty());

            // Base case; we have found a trivial solution if this is a single element array containing just the target
            if (array.length() == 1) {
                if (array.get(0) == target) {
                    return Stream.of(List.of());
                } else {
                    return Stream.empty();
                }
            }

            // Other cases we iterate
            long currentValue = array.get(array.length() - 1);
            ImmutableLongArray prefix = array.subArray(0, array.length() - 1);

            Stream<List<Operator>> results = Stream.empty();
            for (var op : operators) {
                OptionalLong nextTarget = op.nextTarget(target, currentValue);
                if (nextTarget.isPresent()) {
                    var solutions = findOperators(nextTarget.getAsLong(), prefix)
                            .map(list -> ImmutableList.<Operator>builder().addAll(list).add(op).build());

                    results = Stream.concat(results, solutions);
                }
            }

            return results;
        }
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
