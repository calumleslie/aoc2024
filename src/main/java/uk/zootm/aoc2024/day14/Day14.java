package uk.zootm.aoc2024.day14;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import uk.zootm.aoc2024.grid.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day14 {

    public static void main(String[] args) throws IOException {
        var lines = Resources.readLines(Day14.class.getResource("input"), StandardCharsets.UTF_8);
        var initialRobots = lines.stream().map(Robot::parse).toList();
        var bounds = new Vector(101, 103);

        var part1Robots = initialRobots.stream().map(robot -> robot.move(bounds,100)).toList();

        System.out.printf("Part 1: %d%n", safetyFactor(part1Robots, bounds));

        var out = Files.createTempDirectory("aoc-images");
        var iterations = 10_000;

        System.out.printf("Part 2: Writing %d images to %s%n", iterations, out);

        // These numbers from inspection; every 101 elements (for my input) the result looks more "well formed"
        int start = 95;
        int incr = 101;
        for(int i = 95; i < (start + iterations * incr); i += 101) {
            var steps = i;
            var robots = initialRobots.stream().map(robot -> robot.move(bounds, steps)).toList();

            var file = out.resolve(String.format("%06d.png", i));
            drawToFile(file, robots, bounds);
        }
    }

    static void drawToFile(Path file, List<Robot> robots, Vector bounds) throws IOException {
        var image = draw(robots, bounds);
        ImageIO.write(image, "png", file.toFile());
    }

    static BufferedImage draw(List<Robot> robots, Vector bounds) {
        var image = new BufferedImage(bounds.x(), bounds.y(), BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();

        graphics.setPaint(Color.WHITE);
        graphics.fillRect(0, 0, bounds.x(), bounds.y());

        for(var robot : robots) {
            image.setRGB(robot.position().x(), robot.position().y(), Color.BLACK.getRGB());
        }

        return image;
    }

    static long safetyFactor(List<Robot> robots, Vector bounds) {
        return quadrantCount(robots, bounds).reduce((x, y) -> x * y).getAsLong();
    }

    static LongStream quadrantCount(List<Robot> robots, Vector bounds) {
        return Quadrant.forBounds(bounds).mapToLong(quadrant -> robots.stream().filter(r -> quadrant.contains(r.position())).count());
    }

    record Quadrant(Vector origin, Vector size) {
        boolean contains(Vector pos) {
            return origin.x() <= pos.x() && pos.x() < end().x() && origin.y() <= pos.y() && pos.y() < end().y();
        }

        Vector end() {
            return origin.plus(size);
        }

        static Stream<Quadrant> forBounds(Vector bounds) {
            Preconditions.checkArgument(bounds.x() % 2 == 1);
            Preconditions.checkArgument(bounds.y() % 2 == 1);
            var size = new Vector(bounds.x() / 2, bounds.y() / 2);

            return Stream.of(
                    new Quadrant(new Vector(0, 0), size),
                    new Quadrant(new Vector(size.x() + 1, 0), size),
                    new Quadrant(new Vector(0, size.y() + 1), size),
                    new Quadrant(new Vector(size.x() + 1, size.y() + 1), size));
        }
    }

    record Robot(Vector position, Vector velocity) {
        private static final Pattern ROBOT =
                Pattern.compile("p=(?<px>-?[0-9]+),(?<py>-?[0-9]+) v=(?<vx>-?[0-9]+),(?<vy>-?[0-9]+)");

        Robot(int px, int py, int vx, int vy) {
            this(new Vector(px, py), new Vector(vx, vy));
        }

        Robot move(Vector bounds, int steps) {
            // TODO: If magnitude here gets high this may need to change to longs
            return withPosition(position.plus(velocity.times(steps)).clamp(bounds));
        }

        Robot withPosition(Vector newPosition) {
            return new Robot(newPosition, velocity);
        }

        static Robot parse(String string) {
            var matcher = ROBOT.matcher(string);
            Preconditions.checkArgument(matcher.matches(), "Can't parse: [%s]", string);
            return new Robot(
                    Integer.parseInt(matcher.group("px")),
                    Integer.parseInt(matcher.group("py")),
                    Integer.parseInt(matcher.group("vx")),
                    Integer.parseInt(matcher.group("vy")));
        }
    }
}
