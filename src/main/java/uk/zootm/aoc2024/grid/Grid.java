package uk.zootm.aoc2024.grid;

import com.google.common.base.Preconditions;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Grid<T> {
    T get(Vector coord);

    int width();

    int height();

    default Vector bounds() {
        return new Vector(width(), height());
    }

    /**
     * Guaranteed to be in row-then-column order, ascending
     */
    default Stream<Vector> coords() {
        return bounds().containedCoords();
    }

    default boolean inBounds(Vector coord) {
        return coord.inBounds(bounds());
    }

    default void checkBounds(Vector coord) {
        Preconditions.checkArgument(inBounds(coord), "Out of bounds: %s", coord);
    }
}
