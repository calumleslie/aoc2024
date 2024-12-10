package uk.zootm.aoc2024.grid;

public record Vector(int x, int y) implements Comparable<Vector> {
    public Vector plus(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector minus(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public Vector times(int magnitude) {
        return new Vector(x * magnitude, y * magnitude);
    }

    public Vector abs() {
        return new Vector(Math.abs(x), Math.abs(y));
    }

    public double magnitude() {
        return Math.sqrt((x * x) + (y * y));
    }

    @Override
    public int compareTo(Vector o) {
        return Double.compare(magnitude(), o.magnitude());
    }

    /**
     * Reduces a vector to the smallest size that preserves direction. Not super-efficient, will struggle if one of the
     * numbers is large and prime.
     */
    public Vector minimize() {
        // Yes using a prime sieve or something would be better
        int range = Math.max(Math.abs(x), Math.abs(y));
        int minimizedX = x;
        int minimizedY = y;

        for (int i = 2; i <= range; i++) {
            if (minimizedX % i == 0 && minimizedY % i == 0) {
                minimizedX /= i;
                minimizedY /= i;
                range /= i; // Should always be one of x or y
                i--; // We want to check this factor again
            }
        }

        return new Vector(minimizedX, minimizedY);
    }
}
