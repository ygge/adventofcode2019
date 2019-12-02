import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    public static void main(String[] args) throws IOException {
        final List<Integer> program = Arrays.stream(Util.readStrings().get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        program.set(1, 12);
        program.set(2, 2);

        System.out.println("part 1:" + runProgram(program).get(0));

        boolean done = false;
        for (int noun = 0; !done && noun < 100; ++noun) {
            for (int verb = 0; verb < 100; ++verb) {
                program.set(1, noun);
                program.set(2, verb);
                final List<Integer> p = runProgram(program);
                if (p.get(0) == 19690720) {
                    System.out.println(String.format("part 2: %d %d = %d", noun, verb, 100*noun+verb));
                    done = true;
                    break;
                }
            }
        }
    }

    private static List<Integer> runProgram(List<Integer> program) {
        final List<Integer> p = new ArrayList<>(program);
        int index = 0;
        while (true) {
            int code = p.get(index);
            if (code == 99) {
                break;
            } else if (code == 1) {
                p.set(p.get(index + 3), p.get(p.get(index + 1)) + p.get(p.get(index + 2)));
            } else if (code == 2) {
                p.set(p.get(index + 3), p.get(p.get(index + 1)) * p.get(p.get(index + 2)));
            } else {
                throw new RuntimeException("Wrong code: " + code);
            }
            index += 4;
        }
        return p;
    }
}
