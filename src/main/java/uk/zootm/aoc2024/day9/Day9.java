package uk.zootm.aoc2024.day9;

import com.google.common.io.Resources;
import com.google.common.primitives.ImmutableIntArray;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class Day9 {
    public static void main(String[] args) throws Exception {
        var input = Resources.toString(Day9.class.getResource("input"), StandardCharsets.UTF_8);
        var fs = parse(input);

        var part1Fs = fs.clone();
        compact(part1Fs);
        System.out.printf("Part 1: %d%n", part1Fs.checksum());

        var part2Fs = fs.clone();
        compactNonFragmented(part2Fs);
        System.out.printf("Part 2: %d%n", part2Fs.checksum());
    }

    static Filesystem parse(String input) {
        var builder = ImmutableIntArray.builder();
        var inFile = true;
        int currentFile = 0;
        int cursor = 0;
        var characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            var digit = Character.digit(characters[i], 10);
            int end = cursor + digit;
            if (inFile) {
                builder.addAll(fileBlock(currentFile, digit));
                currentFile++;
            } else {
                builder.addAll(fileBlock(-1, digit));
            }

            cursor = end;
            inFile = !inFile;
        }
        return Filesystem.fromSectors(builder.build().toArray());
    }

    private static IntStream fileBlock(int fileNumber, int length) {
        return IntStream.generate(() -> fileNumber).limit(length);
    }

    static void compact(Filesystem fs) {
        while (!fs.contiguous()) {
            fs.swap(fs.firstUnoccupiedSector(), fs.lastOccupiedSector());
        }
    }

    static void compactNonFragmented(Filesystem fs) {
        var filesInReverseIdOrder = fs.files().stream()
                .sorted(Comparator.comparing(FileLocation::id).reversed());

        // File lengths are up to 9 due to the way the problem is written. This obviously doesnt' generalize
        // so we'll add it here :)
        var cursors = new int[10];

        filesInReverseIdOrder.forEach(file -> {
            fs.findFirstBlankSpace(cursors[file.length], file.length()).stream()
                    .filter(newStart -> newStart < file.start())
                    .forEach(newStart -> {
                        fs.swapRange(newStart, file.start(), file.length());
                        cursors[file.length] = newStart;
                    });
        });
    }

    static class Filesystem {
        private final int[] sectors;
        private final BitSet occupied;

        private Filesystem(int[] sectors, BitSet occupied) {
            this.sectors = sectors;
            this.occupied = occupied;
        }

        static Filesystem fromSectors(int... sectors) {
            var occupied = new BitSet(sectors.length);
            for (int i = 0; i < sectors.length; i++) {
                occupied.set(i, sectors[i] >= 0);
            }
            return new Filesystem(sectors, occupied);
        }

        void swapRange(int i1, int i2, int length) {
            for (int o = 0; o < length; o++) {
                swap(i1 + o, i2 + o);
            }
        }

        void swap(int i1, int i2) {
            int tmp = get(i1);
            set(i1, get(i2));
            set(i2, tmp);
        }

        int get(int i) {
            return sectors[i];
        }

        void set(int i, int value) {
            sectors[i] = value;
            occupied.set(i, value >= 0);
        }

        boolean contiguous() {
            return firstUnoccupiedSector() > lastOccupiedSector();
        }

        OptionalInt findFirstBlankSpace(int start, int length) {
            return IntStream.range(start, sectors.length - length)
                    .filter(from -> isFree(from, length))
                    .findFirst();
        }

        List<FileLocation> files() {
            int[] occupiedIndexes = occupied.stream().toArray();
            List<FileLocation> found = new ArrayList<>();
            int prevId = -1;
            int prevIdx = -1;
            int start = 0;
            for (int i : occupiedIndexes) {
                int id = sectors[i];
                if (prevId != id) {
                    if (prevId != -1) {
                        found.add(new FileLocation(prevId, start, prevIdx - start + 1));
                    }
                    start = i;
                }
                prevId = id;
                prevIdx = i;
            }
            if (prevId != -1) {
                found.add(new FileLocation(prevId, start, prevIdx - start + 1));
            }
            return found;
        }

        private boolean isFree(int from, int length) {
            BitSet range = new BitSet();
            range.set(from, from + length);
            return !occupied.intersects(range);
        }

        int firstUnoccupiedSector() {
            return occupied.nextClearBit(0);
        }

        int lastOccupiedSector() {
            return occupied.previousSetBit(sectors.length - 1);
        }

        long checksum() {
            return occupied.stream().mapToLong(i -> i * sectors[i]).sum();
        }

        @Override
        protected Filesystem clone() {
            return new Filesystem(sectors.clone(), (BitSet) occupied.clone());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Filesystem f) {
                return Arrays.equals(sectors, f.sectors);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(sectors);
        }

        @Override
        public String toString() {
            return Arrays.toString(sectors);
        }
    }

    record FileLocation(int id, int start, int length) {}
}
