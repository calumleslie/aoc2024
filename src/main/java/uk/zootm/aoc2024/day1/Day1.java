package uk.zootm.aoc2024.day1;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Streams;
import com.google.common.io.Resources;
import com.google.common.primitives.ImmutableLongArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class Day1 {
    public static void main(String[] args) throws IOException {
        var input = Resources.readLines(Day1.class.getResource("input"), StandardCharsets.UTF_8);
        var listPair = ListPair.parse(input.stream());
        System.out.printf("Part 1: %d%n", part1(listPair));
        System.out.printf("Part 2: %d%n", part2(listPair));
    }

    static long part1(ListPair listPair) {
        long[] left = listPair.left.toArray();
        long[] right = listPair.right.toArray();

        Arrays.sort(left);
        Arrays.sort(right);

        return Streams.zip(Arrays.stream(left).boxed(), Arrays.stream(right).boxed(), (l, r) -> Math.abs(r - l))
                .mapToLong(x -> x)
                .sum();
    }

    static long part2(ListPair listPair) {
        var rightFrequencies = HashMultiset.create(listPair.right().asList());

        return listPair.left().stream().map(l -> l * rightFrequencies.count(l)).sum();
    }

    record ListPair(ImmutableLongArray left, ImmutableLongArray right) {
        ListPair {
            Preconditions.checkArgument(left.length() == right.length());
        }

        static ListPair parse(Stream<String> lines) {
            var left = ImmutableLongArray.builder();
            var right = ImmutableLongArray.builder();
            var iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] bits = line.split("\s+", -1);
                Preconditions.checkState(bits.length == 2, "Cannot parse line: [%s]", line);
                left.add(Long.parseLong(bits[0]));
                right.add(Long.parseLong(bits[1]));
            }
            return new ListPair(left.build(), right.build());
        }
    }
}
