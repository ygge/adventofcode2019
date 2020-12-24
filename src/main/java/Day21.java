import java.util.ArrayList;
import java.util.List;

public class Day21 {

    public static void main(String[] args) {
        String program = Util.readString();
        var computer = new IntComputer(program);

        long part1 = part1(computer);
        System.out.println("Part 1: " + part1);

        long part2 = part2(computer);
        System.out.println("Part 2: " + part2);
    }

    private static long part2(IntComputer computer) {
        computer.reset();
        var program = "" +
                "NOT A J\n" +
                "NOT B T\n" +
                "OR T J\n" +
                "NOT C T\n" +
                "OR T J\n" +
                "AND D J\n" +
                "NOT E T\n" +
                "NOT T T\n" +
                "OR H T\n" +
                "AND T J\n" +
                "RUN\n";
        List<Long> instructions = new ArrayList<>();
        for (int i = 0; i < program.length(); ++i) {
            instructions.add((long)program.charAt(i));
        }
        List<Long> output = computer.run(instructions);
        for (long c : output) {
            System.out.print((char)c);
        }
        System.out.println();
        return output.get(output.size()-1);
    }

    private static long part1(IntComputer computer) {
        var program = "" +
                "NOT A J\n" +
                "NOT B T\n" +
                "OR T J\n" +
                "NOT C T\n" +
                "OR T J\n" +
                "AND D J\n" +
                "WALK\n";
        List<Long> instructions = new ArrayList<>();
        for (int i = 0; i < program.length(); ++i) {
            instructions.add((long)program.charAt(i));
        }
        List<Long> output = computer.run(instructions);
        for (long c : output) {
            System.out.print((char)c);
        }
        System.out.println();
        return output.get(output.size()-1);
    }
}
