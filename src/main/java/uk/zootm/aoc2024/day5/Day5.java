package uk.zootm.aoc2024.day5;

import com.google.common.base.Splitter;
import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 {
    private static final Splitter PIPE = Splitter.on('|');
    private static final Splitter COMMA = Splitter.on(',');

    public static void main(String[] args) throws IOException {
        var inputFile = Resources.readLines(Day5.class.getResource("input"), StandardCharsets.UTF_8);
        var input = Input.parse(inputFile.stream());

        System.out.printf("Part 1: %d%n", part1(input));
    }

    static int part1(Input input) {
        return input.updateOrders().stream()
                .filter(updateOrder -> compliantWithRules(input.pageOrdering(), updateOrder))
                .mapToInt(Day5::middleValue)
                .sum();
    }

    static int middleValue(List<Integer> line) {
        if(line.size() % 2 != 1) {
            throw new IllegalArgumentException("No middle value in: " + line);
        }
        return line.get(line.size() / 2);
    }

    static boolean compliantWithRules(DirectedGraph<Integer> pageOrdering, List<Integer> updateOrder) {
        for (int i = 0; i < updateOrder.size() - 1; i++) {
            var page = updateOrder.get(i);
            var followingPages = updateOrder.subList(i+1, updateOrder.size());

            var mustPrecede = pageOrdering.incoming(page);

            if(followingPages.stream().anyMatch(mustPrecede::contains)) {
                return false;
            }
        }
        return true;
    }

    record Input(DirectedGraph<Integer> pageOrdering, List<List<Integer>> updateOrders) {
        static Input parse(Stream<String> lines) {
            DirectedGraph<Integer> pageOrdering = new DirectedGraph<>();
            Iterator<String> lineIterator = lines.iterator();
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                if (line.isEmpty()) {
                    // Move to next section
                    break;
                }

                var bits = PIPE.split(line).iterator();
                pageOrdering.addEdge(Integer.parseInt(bits.next()), Integer.parseInt(bits.next()));
            }

            List<List<Integer>> updateOrders = new ArrayList<>();
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();

                updateOrders.add(COMMA.splitToStream(line)
                        .map(Integer::parseInt)
                        .toList());
            }

            return new Input(pageOrdering, updateOrders);
        }
    }
}
