import java.util.Arrays;

final class IntComputer {

    private final int[] initialProgram;
    private int[] program;

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

    void run(Integer... input) {
        int inputIndex = 0;
        int index = 0;
        while (true) {
            int v = program[index];
            int code = v%100;
            if (code == 99) {
                break;
            } else if (code == 1) {
                program[program[index+3]] = getValue(program, v, index, 1) + getValue(program, v, index, 2);
                index += 4;
            } else if (code == 2) {
                program[program[index+3]] = getValue(program, v, index, 1) * getValue(program, v, index, 2);
                index += 4;
            } else if (code == 3) {
                program[program[index+1]] = input[inputIndex++];
                index += 2;
            } else if (code == 4) {
                System.out.println(getValue(program, v, index, 1));
                index += 2;
            } else if (code == 5) {
                int ifValue = getValue(program, v, index, 1);
                if (ifValue > 0) {
                    index = getValue(program, v, index, 2);
                } else {
                    index += 3;
                }
            } else if (code == 6) {
                int ifValue = getValue(program, v, index, 1);
                if (ifValue == 0) {
                    index = getValue(program, v, index, 2);
                } else {
                    index += 3;
                }
            } else if (code == 7) {
                int v1 = getValue(program, v, index, 1);
                int v2 = getValue(program, v, index, 2);
                program[program[index+3]] = v1 < v2 ? 1 : 0;
                index += 4;
            } else if (code == 8) {
                int v1 = getValue(program, v, index, 1);
                int v2 = getValue(program, v, index, 2);
                program[program[index+3]] = v1 == v2 ? 1 : 0;
                index += 4;
            } else {
                throw new RuntimeException("Wrong code: " + code);
            }
        }
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

    void reset() {
        this.program = this.initialProgram.clone();
    }
}
