package uk.zootm.aoc2024.day5;

import com.google.common.base.Splitter;
import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day5Test {
    private static final String EXAMPLE = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13
            
            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
            """;

    @Test
    public void input_parse() {
        String inputString = """
                47|53
                97|13
                97|61
                
                75,47,61,53,29
                97,61,53,29,13
                75,29,13
                """;
        Day5.Input input = inputOf(inputString);

        DirectedGraph expectedPageOrdering = new DirectedGraph();
        expectedPageOrdering.addEdge(47, 53);
        expectedPageOrdering.addEdge(97, 13);
        expectedPageOrdering.addEdge(97, 61);


        assertThat(input.pageOrdering()).isEqualTo(expectedPageOrdering);
        assertThat(input.updateOrders()).containsExactly(
                List.of(75, 47, 61, 53, 29),
                List.of(97, 61, 53, 29, 13),
                List.of(75, 29, 13)
        );

        System.out.println(input);
    }

    @Test
    public void compliantWithRules_example() {
        Day5.Input example = inputOf(EXAMPLE);
        DirectedGraph exampleOrdering = example.pageOrdering();

        assertCompliance(exampleOrdering, "75,47,61,53,29").isTrue();
        assertCompliance(exampleOrdering, "97,61,53,29,13").isTrue();
        assertCompliance(exampleOrdering, "75,29,13").isTrue();
        assertCompliance(exampleOrdering, "75,97,47,61,53").isFalse();
        assertCompliance(exampleOrdering, "61,13,29").isFalse();
        assertCompliance(exampleOrdering, "97,13,75,29,47").isFalse();
    }

    @Test
    public void part1_example() {
        Day5.Input example = inputOf(EXAMPLE);

        assertThat(Day5.part1(example)).isEqualTo(143);
    }

    private AbstractBooleanAssert<?> assertCompliance(DirectedGraph ordering, String line) {
        var update = Splitter.on(',')
                .splitToStream(line)
                .map(Integer::parseInt)
                .collect(Collectors.toUnmodifiableList());

        return assertThat(Day5.compliantWithRules(ordering, update));
    }

    private static Day5.Input inputOf(String inputString) {
        Day5.Input input = Day5.Input.parse(inputString.strip().lines());
        return input;
    }
}