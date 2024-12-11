package uk.zootm.aoc2024.day11;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import uk.zootm.aoc2024.util.LongStrings;

public class Day11 {
    public static void main(String[] args) throws Exception {
        var line = Resources.toString(Day11.class.getResource("input"), StandardCharsets.UTF_8);
        var input =
                Splitter.on(' ').splitToStream(line).mapToLong(Long::parseLong).toArray();

        System.out.printf("Day 1: %d%n", blinkTimes(input, 25).length);
    }

    static long[] blinkTimes(long[] values, int repetitions) {
        return Streams.findLast(Stream.iterate(values, Day11::blink).limit(repetitions + 1))
                .get();
    }

    static long[] blink(long[] values) {
        return Arrays.stream(values).flatMap(Day11::blink).toArray();
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
