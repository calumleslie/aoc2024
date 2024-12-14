package uk.zootm.aoc2024.day13;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day13 {
    private static final Pattern PROBLEM_PATTERN = Pattern.compile("""
            Button\\ A:\\ X\\+(?<ax>[0-9]+),\\ Y\\+(?<ay>[0-9]+)\\n
            Button\\ B:\\ X\\+(?<bx>[0-9]+),\\ Y\\+(?<by>[0-9]+)\\n
            Prize:\\ X=(?<px>[0-9]+),\\ Y=(?<py>[0-9]+)\\n
            """.trim(), Pattern.COMMENTS | Pattern.MULTILINE);

    public static void main(String[] args) throws IOException {
        String inString = Resources.toString(Day13.class.getResource("input"), StandardCharsets.UTF_8);

        System.out.printf("Part 1: %d%n", costToWin(Problem.parse(inString)));
    }

    static long costToWin(Stream<Problem> problems) {
        // Enforcing the 100 press limit has no effect on the outcome so tough
        return problems.flatMap(problem -> problem.pressesToWin().stream())
                .mapToLong(Solution::cost)
                .sum();
    }

    record Button(long x, long y) {
    }

    record Solution(long aPresses, long bPresses) {
        long totalPresses() {
            return aPresses + bPresses;
        }

        long cost() {
            return (3*aPresses) + bPresses;
        }
    }

    record Problem(Button buttonA, Button buttonB, long prizeX, long prizeY) {

        Problem(long ax, long ay, long bx, long by, long prizeX, long prizeY) {
            this(new Button(ax, ay), new Button(bx, by), prizeX, prizeY);
        }

        // TODO: Do we need to handle collinearity here? Otherwise the solution should be exact each time
        Optional<Solution> pressesToWin() {
            // We have:
            // 1. axA + bxB = px
            // 2. ayA + byB = py
            // Rearrange to cancel B terms:
            // 3. (ax*by)A + (bx*by)B = px*by (3*by)
            // 4. (ay*bx)A + (bx*by)B = py*bx (4*bx)
            // 5. (ax*by)A - (ay*bx)A = px*by - py*bx (3-4)
            // 6. A = (px*by - py * bx) / (ax*by - ay*bx) (5 / A terms from LHS)

            long numerator = prizeX * buttonB.y() - prizeY * buttonB.x();
            long denominator = buttonA.x() * buttonB().y() - buttonA.y() * buttonB.x();

            if (numerator % denominator == 0) {
                long aPresses = numerator / denominator;
                // Derive B from A
                long bPresses = (prizeX() - buttonA().x() * aPresses) / buttonB.x();

                return Optional.of(new Solution(aPresses, bPresses));
            } else {
                return Optional.empty();
            }
        }

        static Stream<Problem> parse(String input) {
            return PROBLEM_PATTERN.matcher(input).results()
                    .map(Problem::fromMatchResult);
        }

        private static Problem fromMatchResult(MatchResult result) {
            return new Problem(
                    Long.parseLong(result.group("ax")),
                    Long.parseLong(result.group("ay")),
                    Long.parseLong(result.group("bx")),
                    Long.parseLong(result.group("by")),
                    Long.parseLong(result.group("px")),
                    Long.parseLong(result.group("py")));
        }
    }
}
