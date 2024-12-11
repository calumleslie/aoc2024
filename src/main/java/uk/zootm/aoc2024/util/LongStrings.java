package uk.zootm.aoc2024.util;

public class LongStrings {

    public static long lengthBase10(long value) {
        return value == 0 ? 1L : 1L + (long) Math.floor(Math.log10(value));
    }

    private LongStrings() {
        // Do not instantiate
    }
}
