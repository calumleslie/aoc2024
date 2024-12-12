package uk.zootm.aoc2024.day9;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day9.Day9.FileLocation;
import uk.zootm.aoc2024.day9.Day9.Filesystem;

import static org.assertj.core.api.Assertions.assertThat;

public class Day9Test {
    @Test
    public void parse() {
        assertThat(Day9.parse("12345"))
                .isEqualTo(Filesystem.fromSectors(0, -1, -1, 1, 1, 1, -1, -1, -1, -1, 2, 2, 2, 2, 2));
    }

    @Test
    public void compact_example() {
        Filesystem fs = Day9.parse("2333133121414131402");

        Day9.compact(fs);

        assertThat(fs.checksum()).isEqualTo(1928);
    }

    @Test
    public void files_simple() {
        assertThat(Day9.parse("12345").files())
                .containsExactly(new FileLocation(0, 0, 1), new FileLocation(1, 3, 3), new FileLocation(2, 10, 5));
    }

    @Test
    public void compactNonFragmented_example() {
        Filesystem fs = Day9.parse("2333133121414131402");

        Day9.compactNonFragmented(fs);

        assertThat(fs.checksum()).isEqualTo(2858);
    }
}
