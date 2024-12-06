package uk.zootm.aoc2024.grid;

public record Vector(int x, int y) {
    public Vector plus(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector minus(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public Vector times(int magnitude) {
        return new Vector(x * magnitude, y * magnitude);
    }
}