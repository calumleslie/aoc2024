package uk.zootm.aoc2024.grid;

import java.util.Objects;

abstract class AbstractGrid<T> implements Grid<T> {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Grid g) {
            return width() == g.width()
                    && height() == g.width()
                    && coords().allMatch(c -> Objects.equals(get(c), g.get(c)));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(coords().map(this::get).toArray());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append("(").append(width()).append("x").append(height()).append(", contents=\n");
        for(int y = 0; y < height(); y++) {
            builder.append("  ");
            for(int x = 0; x < width(); x++) {
                builder.append('|');
                builder.append(get(new Vector(x, y)));
            }
            builder.append("|\n");
        }
        builder.append(")");
        return builder.toString();
    }
}
