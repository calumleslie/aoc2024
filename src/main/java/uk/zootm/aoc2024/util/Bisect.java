package uk.zootm.aoc2024.util;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public class Bisect {

    public static <T> int bisectList(List<T> list, Predicate<T> condition) {
        return (int) bisect(list.size() - 1, i -> condition.test(list.get((int) i)));
    }

    public static int bisectInt(int maxInclusive, IntPredicate condition) {
        // Casts all safe as no value here exceeds maxInclusive
        return (int) bisect(maxInclusive, l -> condition.test((int) l));
    }

    /**
     * Finds first value in range [0..maxInclusive] for which condition is true.
     */
    public static long bisect(long maxInclusive, LongPredicate condition) {
        long start = 0L;
        long end = maxInclusive;

        Preconditions.checkArgument(condition.test(end), "condition must match by final index but does not");

        while (start != end) {
            long diff = end - start;
            long mid = start + diff / 2;

            if(condition.test(mid)) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }

        return start;
    }

    private Bisect() {
    }
}
