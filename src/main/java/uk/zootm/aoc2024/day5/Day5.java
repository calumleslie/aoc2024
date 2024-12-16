package uk.zootm.aoc2024.day5;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.io.Resources;
import uk.zootm.aoc2024.graph.SimpleDirectedGraph;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Day5 {
    private static final Splitter PIPE = Splitter.on('|');
    private static final Splitter COMMA = Splitter.on(',');

    public static void main(String[] args) throws IOException {
        var inputFile = Resources.readLines(Day5.class.getResource("input"), StandardCharsets.UTF_8);
        var input = Input.parse(inputFile.stream());

        System.out.printf("Part 1: %d%n", part1(input));
        System.out.printf("Part 2: %d%n", part2(input));
    }

    static int part1(Input input) {
        return input.updateOrders().stream()
                .filter(updateOrder -> compliantWithRules(input.pageOrdering(), updateOrder))
                .mapToInt(Day5::middleValue)
                .sum();
    }

    static int part2(Input input) {
        return input.updateOrders().stream()
                .filter(updateOrder -> !compliantWithRules(input.pageOrdering, updateOrder))
                .map(updateOrder -> correct(input.pageOrdering(), updateOrder))
                .mapToInt(Day5::middleValue)
                .sum();
    }

    static int middleValue(List<Integer> line) {
        if (line.size() % 2 != 1) {
            throw new IllegalArgumentException("No middle value in: " + line);
        }
        return line.get(line.size() / 2);
    }

    static List<Integer> correct(SimpleDirectedGraph<Integer> pageOrdering, List<Integer> updateOrder) {
        Preconditions.checkArgument(!pageOrdering.hasCycles());
        List<Integer> result = new ArrayList<>(updateOrder);
        for (int i = 0; i < result.size() - 1; i++) {
            var page = result.get(i);
            var followingPages = result.subList(i + 1, result.size());

            var mustPrecede = pageOrdering.incomingNodes(page);

            var predecessor =
                    followingPages.stream().filter(mustPrecede::contains).findFirst();
            if (predecessor.isPresent()) {
                // Swap entries
                int j = followingPages.indexOf(predecessor.get());
                result.set(i + 1 + j, page);
                result.set(i, predecessor.get());

                // Go back and check this one is in order; note if the graph has cycles this will loop forever
                i--;
            }
        }
        return result;
    }

    static boolean compliantWithRules(SimpleDirectedGraph<Integer> pageOrdering, List<Integer> updateOrder) {
        for (int i = 0; i < updateOrder.size() - 1; i++) {
            var page = updateOrder.get(i);
            var followingPages = updateOrder.subList(i + 1, updateOrder.size());

            var mustPrecede = pageOrdering.incomingNodes(page);

            if (followingPages.stream().anyMatch(mustPrecede::contains)) {
                return false;
            }
        }
        return true;
    }

    record Input(SimpleDirectedGraph<Integer> pageOrdering, List<List<Integer>> updateOrders) {
        static Input parse(Stream<String> lines) {
            SimpleDirectedGraph<Integer> pageOrdering = new SimpleDirectedGraph<>();
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

                updateOrders.add(
                        COMMA.splitToStream(line).map(Integer::parseInt).toList());
            }

            Preconditions.checkArgument(!pageOrdering.hasCycles());

            return new Input(pageOrdering, updateOrders);
        }
    }
}
