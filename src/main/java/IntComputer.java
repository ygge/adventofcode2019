import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class IntComputer {

    private static final int MEMORY_SIZE = 1000000;

    private final long[] initialProgram;
    private long[] program;
    private int currentIndex, relativeBase, maxIndex;
    private boolean running;

    IntComputer(String p) {
        this.initialProgram = Arrays.stream(p.split(","))
                .map(Long::parseLong)
                .mapToLong(Long::longValue)
                .toArray();
        maxIndex = this.initialProgram.length;
        reset();
    }

    void set(int index, long num) {
        program[index] = num;
    }

    long get(int index) {
        return program[index];
    }

    List<Long> run(Long... input) {
        return run(Arrays.asList(input), true);
    }

    List<Long> run(List<Long> inputs) {
        return run(inputs, false);
    }

    void reset() {
        this.program = new long[MEMORY_SIZE];
        System.arraycopy(this.initialProgram, 0, this.program, 0, this.initialProgram.length);
        currentIndex = 0;
        relativeBase = 0;
        running = false;
    }

    boolean isRunning() {
        return running;
    }

    private List<Long> run(List<Long> inputs, boolean verifyHalting) {
        final List<Long> ret = new ArrayList<>();
        int inputIndex = 0;
        running = true;
        while (true) {
            if (currentIndex >= maxIndex) {
                throw new RuntimeException(String.format("Index out of bounds for program (%d >= %d)", currentIndex, maxIndex));
            }
            int v = (int)program[currentIndex];
            int code = v%100;
            if (code == 99) {
                running = false;
                break;
            } else if (code == 1) {
                program[getIndex(program, v, currentIndex, 3)] = getValue(program, v, currentIndex, 1) + getValue(program, v, currentIndex, 2);
                currentIndex += 4;
            } else if (code == 2) {
                program[getIndex(program, v, currentIndex, 3)] = getValue(program, v, currentIndex, 1) * getValue(program, v, currentIndex, 2);
                currentIndex += 4;
            } else if (code == 3) {
                if (inputIndex == inputs.size()) {
                    if (verifyHalting) {
                        throw new RuntimeException("Program did not halt, expecting more input");
                    }
                    break;
                }
                program[getIndex(program, v, currentIndex, 1)] = inputs.get(inputIndex++);
                currentIndex += 2;
            } else if (code == 4) {
                long value = getValue(program, v, currentIndex, 1);
                ret.add(value);
                currentIndex += 2;
            } else if (code == 5) {
                long ifValue = getValue(program, v, currentIndex, 1);
                if (ifValue > 0) {
                    currentIndex = (int)getValue(program, v, currentIndex, 2);
                } else {
                    currentIndex += 3;
                }
            } else if (code == 6) {
                long ifValue = getValue(program, v, currentIndex, 1);
                if (ifValue == 0) {
                    currentIndex = (int)getValue(program, v, currentIndex, 2);
                } else {
                    currentIndex += 3;
                }
            } else if (code == 7) {
                long v1 = getValue(program, v, currentIndex, 1);
                long v2 = getValue(program, v, currentIndex, 2);
                program[getIndex(program, v, currentIndex, 3)] = v1 < v2 ? 1 : 0;
                currentIndex += 4;
            } else if (code == 8) {
                long v1 = getValue(program, v, currentIndex, 1);
                long v2 = getValue(program, v, currentIndex, 2);
                program[getIndex(program, v, currentIndex, 3)] = v1 == v2 ? 1 : 0;
                currentIndex += 4;
            } else if (code == 9) {
                relativeBase += getValue(program, v, currentIndex, 1);
                currentIndex += 2;
            } else {
                throw new RuntimeException("Wrong code: " + code);
            }
        }
        return ret;
    }

    private int getIndex(long[] program, int code, int index, int pos) {
        int type = code/100;
        int p = pos;
        while (p > 1) {
            type /= 10;
            --p;
        }
        type = type%10;
        if (type == 0) {
            return (int) program[index+pos];
        } else if (type == 1){
            return index+pos;
        } else if (type == 2) {
            return (int) program[index+pos]+relativeBase;
        }
        throw new RuntimeException(String.format("Unsupported type %d", type));
    }

    private long getValue(long[] program, int code, int index, int pos) {
        return program[getIndex(program, code, index, pos)];
    }
}
