package uk.zootm.aoc2024.grid;

import com.google.common.base.Preconditions;

public class LongGrid extends AbstractGrid<Long> {
    private final long[] contents;
    private final int width;

    public LongGrid(long[] contents, int width) {
        Preconditions.checkArgument(contents.length % width == 0);
        this.contents = contents;
        this.width = width;
    }

    public static LongGrid empty(int width, int height) {
        long[] contents = new long[width * height];
        return new LongGrid(contents, width);
    }

    public static LongGrid fromStringGrid(Grid<String> input) {
        long[] contents =
                input.coords().map(input::get).mapToLong(Long::parseLong).toArray();
        return new LongGrid(contents, input.width());
    }

    @Override
    public Long get(Vector coord) {
        return getAsLong(coord);
    }

    public void set(Vector coord, long value) {
        contents[coordToIndex(coord)] = value;
    }

    public long getAsLong(Vector coord) {
        return contents[coordToIndex(coord)];
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
