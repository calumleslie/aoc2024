package uk.zootm.aoc2024.grid;

import com.google.common.base.Preconditions;
import com.google.common.primitives.ImmutableIntArray;
import java.text.BreakIterator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A grid where every cell contains a grapheme cluster (using this rather than a character because I wondered what it
 * would take).
 */
public class CharacterGrid extends AbstractGrid<String> {
    private final int width;
    private final List<Graphemes> lines;

    CharacterGrid(List<Graphemes> lines) {
        long distinctLengths = lines.stream().map(Graphemes::length).distinct().count();

        Preconditions.checkArgument(distinctLengths == 1L);

        this.width = lines.getFirst().length();
        this.lines = lines;
    }

    public static CharacterGrid fromCharacterGrid(List<String> lineStrings) {
        List<Graphemes> lines = lineStrings.stream()
                .map(l -> Graphemes.forString(l, Locale.getDefault()))
                .toList();

        return new CharacterGrid(lines);
    }

    public static CharacterGrid fromString(String string) {
        return fromCharacterGrid(string.strip().lines().toList());
    }

    public Stream<FindResult> find(String targetString) {
        var target = Graphemes.forString(targetString, Locale.getDefault());
        var possibleResults =
                coords().flatMap(loc -> Stream.of(Direction.values()).map(dir -> new FindResult(loc, dir)));

        return possibleResults.filter(result -> matches(result.location(), result.direction(), target));
    }

    boolean matches(Vector coord, Direction direction, String target) {
        return matches(coord, direction, Graphemes.forString(target, Locale.getDefault()));
    }

    boolean matches(Vector coord, Direction direction, Graphemes target) {
        checkBounds(coord);

        Vector end = coord.plus(direction.vector().times(target.length() - 1));
        if (!inBounds(end)) {
            return false;
        }

        Stream<String> actualGraphemes =
                Stream.iterate(coord, c -> c.plus(direction.vector())).map(this::get);
        Stream<String> targetGraphemes = target.graphemes();

        var targetIterator = targetGraphemes.iterator();
        var actualIterator = actualGraphemes.iterator();

        while (targetIterator.hasNext()) {
            // Don't need to check actual due to inBounds check above
            String targetGrapheme = targetIterator.next();
            String actualGrapheme = actualIterator.next();
            if (!targetGrapheme.equals(actualGrapheme)) {
                return false;
            }
        }

        return true;
    }

    public String get(Vector coord) {
        checkBounds(coord);
        return lines.get(coord.y()).grapheme(coord.x());
    }

    public int width() {
        return width;
    }

    public int height() {
        return lines.size();
    }

    public record FindResult(Vector location, Direction direction) {}

    private static class Graphemes {
        private final String string;
        private final int[] boundaries;

        private Graphemes(String string, int[] graphemeStarts) {
            this.string = string;
            this.boundaries = graphemeStarts;
        }

        int length() {
            return boundaries.length - 1;
        }

        Stream<String> graphemes() {
            return IntStream.range(0, length()).mapToObj(this::grapheme);
        }

        String grapheme(int index) {
            int start = boundaries[index];
            int end = index == (boundaries.length - 1) ? string.length() : boundaries[index + 1];
            return string.substring(start, end);
        }

        static Graphemes forString(String string, Locale locale) {
            ImmutableIntArray.Builder boundaries = ImmutableIntArray.builder();
            BreakIterator iterator = BreakIterator.getCharacterInstance(locale);
            iterator.setText(string);
            boundaries.add(iterator.first());
            int next = iterator.next();
            while (next != BreakIterator.DONE) {
                boundaries.add(next);
                next = iterator.next();
            }
            return new Graphemes(string, boundaries.build().toArray());
        }

        @Override
        public String toString() {
            return String.format("Graphemes(%s, %s)", string, Arrays.toString(boundaries));
        }
    }
}
