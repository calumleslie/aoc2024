package uk.zootm.aoc2024.grid;

import com.google.common.base.Preconditions;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Grid<T> {
    T get(Vector coord);

    int width();

    int height();

    /**
     * Guaranteed to be in row-then-column order, ascending
     */
    default Stream<Vector> coords() {
        return IntStream.range(0, height())
                .mapToObj(y -> IntStream.range(0, width()).mapToObj(x -> new Vector(x, y)))
                .flatMap(x -> x);
    }

    default boolean inBounds(Vector coord) {
        return coord.x() >= 0 && coord.x() < width() && coord.y() >= 0 && coord.y() < height();
    }

    default void checkBounds(Vector coord) {
        Preconditions.checkArgument(inBounds(coord), "Out of bounds: %s", coord);
    }
}
