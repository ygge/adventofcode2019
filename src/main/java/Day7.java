import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day7 {

    public static void main(String[] args) {
        final String code = Util.readStrings().get(0);
        final IntComputer program = new IntComputer(code);

        final Set<Integer> seen = new HashSet<>();
        int p1 = run(program, seen, 0, 0);
        System.out.println("part 1: " + p1);

        int p2 = runPart2(code);
        System.out.println("part 2: " + p2);
    }

    private static int runPart2(String code) {
        final List<IntComputer> programs = new ArrayList<>();
        final int[] startValues = new int[5];
        final Set<Integer> seen = new HashSet<>();
        return run(code, programs, seen, startValues, 0);
    }

    private static int run(String code, List<IntComputer> programs, Set<Integer> seen, int[] startValues, int index) {
        if (index == 5) {
            for (IntComputer program : programs) {
                program.reset();
            }
            List<Integer> input = Collections.singletonList(0);
            boolean running = true;
            boolean firstRun = true;
            while (running) {
                running = false;
                for (int i = 0; i < 5; ++i) {
                    final IntComputer program = programs.get(i);
                    if (firstRun) {
                        final List<Integer> t = new ArrayList<>();
                        t.add(startValues[i]);
                        t.addAll(input);
                        input = t;
                    }
                    input = program.run(input);
                    if (program.isRunning()) {
                        running = true;
                    }
                }
                firstRun = false;
            }
            return input.get(0);
        }
        int best = Integer.MIN_VALUE;
        for (int i = 5; i < 10; ++i) {
            if (seen.add(i)) {
                programs.add(new IntComputer(code));
                startValues[index] = i;
                int thrust = run(code, programs, seen, startValues,index+1);
                best = Math.max(best, thrust);
                seen.remove(i);
            }
        }
        return best;
    }

    private static int run(IntComputer program, Set<Integer> seen, int index, int input) {
        if (index == 5) {
            return input;
        }
        int best = Integer.MIN_VALUE;
        for (int i = 0; i < 5; ++i) {
            if (seen.add(i)) {
                program.reset();
                int value = program.run(i, input).get(0);
                int thrust = run(program, seen, index+1, value);
                best = Math.max(best, thrust);
                seen.remove(i);
            }
        }
        return best;
    }
}
