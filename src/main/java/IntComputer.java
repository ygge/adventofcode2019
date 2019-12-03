import java.util.Arrays;

final class IntComputer {

    private final int[] initialProgram;
    private int[] program;

    IntComputer(String p) {
        final int[] a = new int[0];
        this.initialProgram = Arrays.stream(p.split(","))
                .map(Integer::parseInt)
                .mapToInt(Integer::intValue)
                .toArray();
        reset();
    }

    void set(int index, int num) {
        program[index] = num;
    }

    int get(int index) {
        return program[index];
    }

    void run() {
        int index = 0;
        while (true) {
            int code = program[index];
            if (code == 99) {
                break;
            } else if (code == 1) {
                program[program[index+3]] = program[program[index+1]] + program[program[index+2]];
                index += 4;
            } else if (code == 2) {
                program[program[index+3]] = program[program[index+1]] * program[program[index+2]];
                index += 4;
            } else {
                throw new RuntimeException("Wrong code: " + code);
            }
        }
    }

    void reset() {
        this.program = this.initialProgram.clone();
    }
}
