import java.util.*;

public class Day23 {

    public static void main(String[] args) {
        var program = Util.readString();

        int part1 = part1(program);
        System.out.println("Part 1: " + part1);

        long part2 = part2(program);
        System.out.println("Part 2: " + part2);
    }

    private static long part2(String program) {
        var comp = new IntComputer[50];
        var in = new ArrayList<LinkedList<Long>>();
        long natX = -1;
        long natY = -1;
        long lastY = -1;
        for (int i = 0; i < 50; ++i) {
            in.add(new LinkedList<>());
            comp[i] = new IntComputer(program);
            var output = comp[i].run(Collections.singletonList((long) i));
            for (int j = 0; j < output.size(); ++j) {
                int address = (int)(long) output.get(j);
                if (address == -1) {
                    continue;
                }
                if (address == 255) {
                    natX = output.get(j+1);
                    natY = output.get(j+2);
                } else {
                    in.get((int)(long) address).add(output.get(j+1));
                    in.get((int)(long) address).add(output.get(j+2));
                }
                j += 2;
            }
        }
        while (true) {
            for (int i = 0; i < comp.length; ++i) {
                var input = new ArrayList<>(in.get(i));
                if (input.isEmpty()) {
                    input.add(-1L);
                }
                in.get(i).clear();
                List<Long> output = comp[i].run(input);
                for (int j = 0; j < output.size(); ++j) {
                    int address = (int)(long) output.get(j);
                    if (address == -1) {
                        continue;
                    }
                    if (address == 255) {
                        natX = output.get(j+1);
                        natY = output.get(j+2);
                    } else {
                        in.get((int)(long) address).add(output.get(j+1));
                        in.get((int)(long) address).add(output.get(j+2));
                    }
                    j += 2;
                }
            }
            if (empty(in)) {
                if (lastY == natY) {
                    return lastY;
                }
                lastY = natY;
                in.get(0).add(natX);
                in.get(0).add(natY);
            }
        }
    }

    private static boolean empty(List<LinkedList<Long>> in) {
        for (LinkedList<Long> longs : in) {
            if (!longs.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static int part1(String program) {
        var comp = new IntComputer[50];
        var in = new ArrayList<LinkedList<Long>>();
        for (int i = 0; i < 50; ++i) {
            in.add(new LinkedList<>());
            comp[i] = new IntComputer(program);
            var output = comp[i].run(Collections.singletonList((long) i));
            handleOutput(in, output);
        }
        while (true) {
            for (int i = 0; i < comp.length; ++i) {
                var input = new ArrayList<>(in.get(i));
                if (input.isEmpty()) {
                    input.add(-1L);
                }
                in.get(i).clear();
                List<Long> output = comp[i].run(input);
                Integer ret = handleOutput(in, output);
                if (ret != null) {
                    return ret;
                }
            }
        }
    }

    private static Integer handleOutput(ArrayList<LinkedList<Long>> in, List<Long> output) {
        for (int j = 0; j < output.size(); ++j) {
            int address = (int)(long) output.get(j);
            if (address == -1) {
                continue;
            }
            if (address == 255) {
                return (int)(long) output.get(j+2);
            } else {
                in.get((int)(long) address).add(output.get(j+1));
                in.get((int)(long) address).add(output.get(j+2));
                j += 2;
            }
        }
        return null;
    }
}
