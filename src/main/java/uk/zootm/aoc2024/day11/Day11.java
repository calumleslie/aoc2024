package uk.zootm.aoc2024.day11;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import uk.zootm.aoc2024.util.LongStrings;

public class Day11 {
    public static void main(String[] args) throws Exception {
        var line = Resources.toString(Day11.class.getResource("input"), StandardCharsets.UTF_8);
        var input =
                Splitter.on(' ').splitToStream(line).mapToLong(Long::parseLong).toArray();
        var frequencyMap = FrequencyMap.of(input);

        System.out.printf("Day 1: %d%n", blinkTimes(frequencyMap, 25).count());
        System.out.printf("Day 2: %d%n", blinkTimes(frequencyMap, 75).count());
    }

    static FrequencyMap blinkTimes(FrequencyMap initial, int repetitions) {
        return Streams.findLast(Stream.iterate(initial, Day11::blink).limit(repetitions + 1))
                .get();
    }

    static FrequencyMap blink(FrequencyMap frequencyMap) {
        var result = new FrequencyMap();

        frequencyMap.frequencies().entrySet().forEach(entry -> blink(entry.getKey())
                .forEach(newStone -> result.increment(newStone, entry.getValue())));

        return result;
    }

    record FrequencyMap(Map<Long, Long> frequencies) {
        FrequencyMap() {
            this(new HashMap<>());
        }

        long count() {
            return frequencies.values().stream().mapToLong(l -> l).sum();
        }

        void increment(long key, long increment) {
            frequencies.compute(key, (k, count) -> (count == null ? 0 : count) + increment);
        }

        static FrequencyMap of(long... values) {
            var frequencies = LongStream.of(values)
                    .boxed()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            return new FrequencyMap(frequencies);
        }
    }

    static LongStream blink(long value) {
        if (value == 0) {
            return LongStream.of(1L);
        }

        long digits = LongStrings.lengthBase10(value);
        if (digits % 2 == 0) {
            long middlePower = (long) Math.pow(10, digits / 2);
            long head = value / middlePower;
            long tail = value % middlePower;

            return LongStream.of(head, tail);
        }

        return LongStream.of(value * 2024);
    }
}
