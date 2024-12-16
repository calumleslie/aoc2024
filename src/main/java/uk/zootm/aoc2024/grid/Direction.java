package uk.zootm.aoc2024.grid;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public enum Direction {
    N(0, -1),
    NE(1, -1),
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0),
    NW(-1, -1);

    private static final ImmutableSet<Direction> CARDINAL = ImmutableSet.of(N, E, S, W);

    private final Vector vector;

    Direction(int x, int y) {
        Preconditions.checkArgument(x >= -1 && x <= 1);
        Preconditions.checkArgument(y >= -1 && y <= 1);
        this.vector = new Vector(x, y);
    }

    public static Set<Direction> cardinal() {
        return CARDINAL;
    }

    public Direction anticlockwise90() {
        return rotateSteps(-2);
    }

    public Direction clockwise90() {
        return rotateSteps(2);
    }

    public Direction opposite() {
        return rotateSteps(4);
    }

    public boolean perpendicularTo(Direction other) {
        return other.equals(this.rotateSteps(-2)) || other.equals(this.rotateSteps(2));
    }

    /**
     * Each step is 45 degrees clockwise
     */
    public Direction rotateSteps(int steps) {
        var values = values();
        // Move down the list of values 2 steps (each step is 45 degrees)
        return values[Math.floorMod(ordinal() + steps, values.length)];
    }

    public Vector vector() {
        return vector;
    }
}
