package uk.zootm.aoc2024.day17;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Preconditions;
import org.junit.jupiter.api.Test;
import uk.zootm.aoc2024.day17.Day17.Computer;

public class Day17Test {
    @Test
    public void next_bst() {
        var comp = new Computer(0, 0, 9, memory(2, 6));

        assertThat(comp.next()).isEmpty();
        assertThat(comp.registers()).containsExactly(0, 1, 9);
    }

    @Test
    public void execute_example1() {
        var comp = new Computer(10, 0, 0, memory(5, 0, 5, 1, 5, 4));

        assertThat(comp.execute()).containsExactly(0L, 1L, 2L);
    }

    @Test
    public void execute_example2() {
        var comp = new Computer(2024, 0, 0, memory(0, 1, 5, 4, 3, 0));

        assertThat(comp.execute()).containsExactly(4L, 2L, 5L, 6L, 7L, 7L, 7L, 7L, 3L, 1L, 0L);
        assertThat(comp.registers()).containsExactly(0, 0, 0);
    }

    @Test
    public void execute_example3() {
        var comp = new Computer(0, 29, 0, memory(1, 7));

        assertThat(comp.execute()).isEmpty();
        assertThat(comp.registers()).containsExactly(0, 26, 0);
    }

    @Test
    public void execute_example4() {
        var comp = new Computer(0, 2024, 43690, memory(4, 0));

        assertThat(comp.execute()).isEmpty();
        assertThat(comp.registers()).containsExactly(0, 44354, 43690);
    }

    @Test
    public void parse_example() {
        assertThat(
                        Computer.parse(
                                """
                Register A: 729
                Register B: 0
                Register C: 0

                Program: 0,1,5,4,3,0
                """))
                .containsExactly(new Computer(729, 0, 0, memory(0, 1, 5, 4, 3, 0)));
    }

    @Test
    public void execute_exampleFromParsed() {
        var comp = new Computer(729, 0, 0, memory(0, 1, 5, 4, 3, 0));

        assertThat(comp.execute()).containsExactly(4L, 6L, 3L, 5L, 6L, 3L, 5L, 2L, 1L, 0L);
    }

    private byte[] memory(int... values) {
        byte[] result = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            int value = values[i];
            Preconditions.checkArgument(value >= 0 && value < 8);
            result[i] = (byte) values[i];
        }
        return result;
    }
}
