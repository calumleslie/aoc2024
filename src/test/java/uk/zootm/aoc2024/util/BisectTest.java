package uk.zootm.aoc2024.util;

import org.junit.jupiter.api.Test;
import org.quicktheories.core.Gen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.longs;

public class BisectTest {
    @Test
    public void allTrue() {
        assertThat(Bisect.bisect(Long.MAX_VALUE, i -> true)).isEqualTo(0L);
    }

    @Test
    public void falseExceptAtEnd() {
        assertThat(Bisect.bisect(Long.MAX_VALUE, i -> i == Long.MAX_VALUE)).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    public void propertyBased() {
        qt().forAll(nonNegativeLongs(), nonNegativeLongs())
                .checkAssert((l1, l2) -> {
                    long max = Math.max(l1, l2);
                    long idx = Math.min(l1, l2);
                    assertThat(Bisect.bisect(max, i -> i >= idx)).isEqualTo(idx);
                });
    }

    private Gen<Long> nonNegativeLongs() {
        return longs().between(0L, Long.MAX_VALUE);
    }


}
