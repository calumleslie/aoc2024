package uk.zootm.aoc2024.day14;

import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day14.Day14.Quadrant;
import uk.zootm.aoc2024.day14.Day14.Robot;
import uk.zootm.aoc2024.grid.Vector;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day14Test {
    @Test
    public void steps_example() {
        var robot = Robot.parse("p=2,4 v=2,-3");
        var bounds = new Vector(11, 7);

        assertThat(robot.move(bounds, 1)).isEqualTo(robot.withPosition(new Vector(4, 1)));
        assertThat(robot.move(bounds, 2)).isEqualTo(robot.withPosition(new Vector(6, 5)));
        assertThat(robot.move(bounds, 3)).isEqualTo(robot.withPosition(new Vector(8, 2)));
        assertThat(robot.move(bounds, 4)).isEqualTo(robot.withPosition(new Vector(10, 6)));
        assertThat(robot.move(bounds, 5)).isEqualTo(robot.withPosition(new Vector(1, 3)));
    }

    @Test
    public void quadrant_forBounds() {
        var bounds = new Vector(11, 7);

        assertThat(Quadrant.forBounds(bounds)).containsExactlyInAnyOrder(
                new Quadrant(new Vector(0, 0), new Vector(5, 3)),
                new Quadrant(new Vector(6, 0), new Vector(5, 3)),
                new Quadrant(new Vector(0, 4), new Vector(5, 3)),
                new Quadrant(new Vector(6, 4), new Vector(5, 3)));
    }

    @Test
    public void quadrantCountAndSafetyFactor_fromExample() {
        var bounds = new Vector(11, 7);

        var robots = """
                p=0,4 v=3,-3
                p=6,3 v=-1,-3
                p=10,3 v=-1,2
                p=2,0 v=2,-1
                p=0,0 v=1,3
                p=3,0 v=-2,-2
                p=7,6 v=-1,-3
                p=3,0 v=-1,-2
                p=9,3 v=2,3
                p=7,3 v=-1,2
                p=2,4 v=2,-3
                p=9,5 v=-3,-3
                """.strip().lines().map(Robot::parse);

        var steppedRobots = robots.map(robot -> robot.move(bounds, 100)).toList();

        assertThat(Day14.quadrantCount(steppedRobots, bounds)).containsExactlyInAnyOrder(1L, 3L, 4L, 1L);
        assertThat(Day14.safetyFactor(steppedRobots, bounds)).isEqualTo(12);
    }
}
