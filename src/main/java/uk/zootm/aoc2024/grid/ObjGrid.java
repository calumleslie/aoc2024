package uk.zootm.aoc2024.grid;

import com.google.common.base.Preconditions;

public class ObjGrid<T> extends AbstractGrid<T> {
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

    private int coordToIndex(Vector vector) {
        checkBounds(vector);
        return vector.x() + (width * vector.y());
    }
}
