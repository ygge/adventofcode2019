import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class IntComputer {

    private final int[] initialProgram;
    private int[] program;
    private int currentIndex;
    private boolean running;

    IntComputer(String p) {
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

    List<Integer> run(Integer... input) {
        return run(Arrays.asList(input), true);
    }

    List<Integer> run(List<Integer> inputs) {
        return run(inputs, false);
    }

    void reset() {
        this.program = this.initialProgram.clone();
        currentIndex = 0;
        running = false;
    }

    boolean isRunning() {
        return running;
    }

    private List<Integer> run(List<Integer> inputs, boolean verifyHalting) {
        final List<Integer> ret = new ArrayList<>();
        int inputIndex = 0;
        running = true;
        while (true) {
            int v = program[currentIndex];
            int code = v%100;
            if (code == 99) {
                running = false;
                break;
            } else if (code == 1) {
                program[program[currentIndex+3]] = getValue(program, v, currentIndex, 1) + getValue(program, v, currentIndex, 2);
                currentIndex += 4;
            } else if (code == 2) {
                program[program[currentIndex+3]] = getValue(program, v, currentIndex, 1) * getValue(program, v, currentIndex, 2);
                currentIndex += 4;
            } else if (code == 3) {
                if (inputIndex == inputs.size()) {
                    if (verifyHalting) {
                        throw new RuntimeException("Program did not halt, expecting more input");
                    }
                    break;
                }
                program[program[currentIndex+1]] = inputs.get(inputIndex++);
                currentIndex += 2;
            } else if (code == 4) {
                int value = getValue(program, v, currentIndex, 1);
                ret.add(value);
                currentIndex += 2;
            } else if (code == 5) {
                int ifValue = getValue(program, v, currentIndex, 1);
                if (ifValue > 0) {
                    currentIndex = getValue(program, v, currentIndex, 2);
                } else {
                    currentIndex += 3;
                }
            } else if (code == 6) {
                int ifValue = getValue(program, v, currentIndex, 1);
                if (ifValue == 0) {
                    currentIndex = getValue(program, v, currentIndex, 2);
                } else {
                    currentIndex += 3;
                }
            } else if (code == 7) {
                int v1 = getValue(program, v, currentIndex, 1);
                int v2 = getValue(program, v, currentIndex, 2);
                program[program[currentIndex+3]] = v1 < v2 ? 1 : 0;
                currentIndex += 4;
            } else if (code == 8) {
                int v1 = getValue(program, v, currentIndex, 1);
                int v2 = getValue(program, v, currentIndex, 2);
                program[program[currentIndex+3]] = v1 == v2 ? 1 : 0;
                currentIndex += 4;
            } else {
                throw new RuntimeException("Wrong code: " + code);
            }
        }
        return ret;
    }

    private int getValue(int[] program, int code, int index, int pos) {
        int type = code/100;
        int p = pos;
        while (p > 1) {
            type /= 10;
            --p;
        }
        type = type%10;
        if (type == 0) {
            return program[program[index+pos]];
        }
        return program[index+pos];
    }
}
