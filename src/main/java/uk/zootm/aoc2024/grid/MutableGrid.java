package uk.zootm.aoc2024.grid;

public interface MutableGrid<T> extends Grid<T> {
    void set(Vector coord, T value);

    default void swap(Vector one, Vector two) {
        var oneVal = get(one);
        var twoVal = get(two);
        set(one, twoVal);
        set(two, oneVal);
    }
}
