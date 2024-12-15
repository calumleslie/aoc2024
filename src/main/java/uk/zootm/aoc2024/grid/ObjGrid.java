package uk.zootm.aoc2024.grid;

import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ObjGrid<T> extends AbstractGrid<T> implements MutableGrid<T> {
    private final Object[] contents;
    private final int width;

    public ObjGrid(Object[] contents, int width) {
        Preconditions.checkArgument(contents.length % width == 0);
        this.contents = contents;
        this.width = width;
    }

    public static <T> ObjGrid<T> empty(int width, int height) {
        Object[] contents = new Object[width * height];
        return new ObjGrid<>(contents, width);
    }

    public static <X, Y> ObjGrid<Y> from(Grid<X> fromGrid, Function<X, Y> transform) {
        ObjGrid<Y> result = empty(fromGrid.width(), fromGrid.height());

        Object[] contents = IntStream.range(0, fromGrid.height() * fromGrid.width())
                .mapToObj(i -> indexToCoord(i, fromGrid.width(), fromGrid.height()))
                .map(fromGrid::get)
                .map(transform)
                .toArray();

        return new ObjGrid<>(contents, fromGrid.width());
    }

    @Override
    public T get(Vector coord) {
        return (T) contents[coordToIndex(coord)];
    }

    public void set(Vector coord, T value) {
        contents[coordToIndex(coord)] = value;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return contents.length / width;
    }

    private static Vector indexToCoord(int index, int width, int height) {
        Objects.checkIndex(index, height * width);
        return new Vector(index % width, index / width);
    }

    private int coordToIndex(Vector vector) {
        checkBounds(vector);
        return vector.x() + (width * vector.y());
    }
}
