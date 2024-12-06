package uk.zootm.aoc2024.grid;

import com.google.common.base.Preconditions;

public enum Direction {
    N(0, -1),
    NE(1, -1),
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0),
    NW(-1, -1);

    private final Vector vector;

    Direction(int x, int y) {
        Preconditions.checkArgument(x >= -1 && x <= 1);
        Preconditions.checkArgument(y >= -1 && y <= 1);
        this.vector = new Vector(x, y);
    }

    public Direction clockwise90() {
        var values = values();
        // Move down the list of values 2 steps (each step is 45 degrees)
        return values[(ordinal() + 2) % values.length];
    }

    public Vector vector() {
        return vector;
    }
}