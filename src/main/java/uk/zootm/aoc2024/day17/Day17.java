package uk.zootm.aoc2024.day17;

import com.google.common.collect.Streams;
import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day17 {
    private static final int ADV = 0;
    private static final int BXL = 1;
    private static final int BST = 2;
    private static final int JNZ = 3;
    private static final int BXC = 4;
    private static final int OUT = 5;
    private static final int BDV = 6;
    private static final int CDV = 7;

    public static void main(String[] args) throws IOException {
        var input = Resources.toString(Day17.class.getResource("input"), StandardCharsets.UTF_8);
        var computer1 = Computer.parse(input).findFirst().orElseThrow();
        System.out.println(computer1);

        System.out.println(
                "Part 1: " + computer1.execute().mapToObj(Long::toString).collect(Collectors.joining(",")));

        // TODO: Part 2
    }

    static class Computer implements Iterator<OptionalLong> {
        private static final Pattern PATTERN = Pattern.compile(
                """
                Register A: (?<a>[0-9]+)
                Register B: (?<b>[0-9]+)
                Register C: (?<c>[0-9]+)

                Program: (?<p>[0-9,]+)
                """
                        .trim(),
                Pattern.MULTILINE);

        private long registerA;
        private long registerB;
        private long registerC;
        private byte[] memory;
        private int pc = 0;
        private boolean trace = false;

        Computer(long registerA, long registerB, long registerC, byte[] memory) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.memory = memory;
            validateMemory();
        }

        static Stream<Computer> parse(String input) {
            return PATTERN.matcher(input)
                    .results()
                    .map(match -> new Computer(
                            Integer.parseInt(match.group("a")),
                            Integer.parseInt(match.group("b")),
                            Integer.parseInt(match.group("c")),
                            readMemory(match.group("p"))));
        }

        static byte[] readMemory(String memoryString) {
            String[] bits = memoryString.split(",");
            byte[] result = new byte[bits.length];
            for (int i = 0; i < bits.length; i++) {
                result[i] = Byte.parseByte(bits[i]);
            }
            return result;
        }

        Computer copyReset() {
            return new Computer(registerA, registerB, registerC, memory);
        }

        Computer copyWithNewA(long newA) {
            return new Computer(newA, registerB, registerC, memory);
        }

        private void validateMemory() {
            for (int i = 0; i < memory.length; i++) {
                byte b = memory[i];
                if (b < 0 || b > 7) {
                    throw new PanicException("Invalid value %d found at memory location %d", b, i);
                }
            }
        }

        // Intended for testing
        long[] registers() {
            return new long[] {registerA, registerB, registerC};
        }

        byte[] memory() {
            return memory;
        }

        public LongStream execute() {
            return Streams.stream(this).flatMapToLong(out -> out.stream());
        }

        @Override
        public boolean hasNext() {
            return pc < memory.length;
        }

        @Override
        public OptionalLong next() {
            var instruction = readLiteral(pc);
            var result =
                    switch (instruction) {
                        case ADV -> adv(readCombo(pc + 1));
                        case BXL -> bxl(readLiteral(pc + 1));
                        case BST -> bst(readCombo(pc + 1));
                        case JNZ -> jnz(readLiteral(pc + 1));
                        case BXC -> bxc(); // No argument (still 2-wide however)
                        case OUT -> out(readCombo(pc + 1));
                        case BDV -> bdv(readCombo(pc + 1));
                        case CDV -> cdv(readCombo(pc + 1));
                        default -> throw new PanicException("Unknown instruction code: %d", instruction);
                    };
            pc = result.jump().orElse(pc + 2);
            if (trace) {
                System.out.println(this);
                System.out.println("OUT: " + result.output());
            }
            return result.output();
        }

        void trace(boolean value) {
            trace = value;
        }

        private Result adv(long operand) {
            registerA = xdv(operand);
            return Result.trivial();
        }

        private Result bxl(long operand) {
            registerB ^= operand;
            return Result.trivial();
        }

        private Result bst(long operand) {
            registerB = operand % 8;
            return Result.trivial();
        }

        private Result jnz(int operand) {
            if (registerA == 0) {
                return Result.trivial();
            }
            return Result.jump(operand);
        }

        private Result bxc() {
            registerB ^= registerC;
            return Result.trivial();
        }

        private Result out(long operand) {
            return Result.out(operand % 8);
        }

        private Result bdv(long operand) {
            registerB = xdv(operand);
            return Result.trivial();
        }

        private Result cdv(long operand) {
            registerC = xdv(operand);
            return Result.trivial();
        }

        private long xdv(long operand) {
            long numerator = registerA;
            long denominator = 1L << operand;
            return numerator / denominator;
        }

        private long readCombo(int index) {
            int value = readLiteral(index);
            return switch (value) {
                case 0, 1, 2, 3 -> value;
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                default -> throw new PanicException("found reserved combo opcode %d at %d", value, index);
            };
        }

        private int readLiteral(int index) {
            if (index >= memory.length) {
                throw new PanicException("Out of bounds memory access to %d", index);
            }
            return memory[index];
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new long[] {registerA, registerB, registerC, Arrays.hashCode(memory), pc});
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Computer computer) {
                return registerA == computer.registerA
                        && registerB == computer.registerB
                        && registerC == computer.registerC
                        && pc == computer.pc
                        && Objects.deepEquals(memory, computer.memory);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("Register A: ").append(registerA).append('\n');
            builder.append("Register B: ").append(registerB).append('\n');
            builder.append("Register C: ").append(registerC).append('\n');
            builder.append('\n');
            for (int i = 0; i < memory.length; i++) {
                builder.append(memory[i]);
                if (i < memory.length - 1) {
                    builder.append(',');
                }
            }
            builder.append('\n');
            int indent = 2 * pc;
            for (int i = 0; i < indent; i++) {
                builder.append(' ');
            }
            builder.append('^');
            return builder.toString();
        }

        class PanicException extends RuntimeException {
            PanicException(String format, Object... args) {
                this(String.format(format, args));
            }

            PanicException(String message) {
                super(message + "\nComputer state:\n" + Computer.this);
            }
        }
    }

    record Result(OptionalLong output, OptionalInt jump) {
        private static final Result TRIVIAL = new Result(OptionalLong.empty(), OptionalInt.empty());

        static Result jump(int destination) {
            return new Result(OptionalLong.empty(), OptionalInt.of(destination));
        }

        static Result out(long value) {
            return new Result(OptionalLong.of(value), OptionalInt.empty());
        }

        static Result trivial() {
            return TRIVIAL;
        }
    }
}
