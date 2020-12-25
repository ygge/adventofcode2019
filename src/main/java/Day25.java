import java.util.*;

public class Day25 {

    public static void main(String[] args) {
        String program = Util.readString();
        var computer = new IntComputer(program);
        run(computer, "south");
        run(computer, "take boulder");
        run(computer, "west");
        run(computer, "take asterisk");
        run(computer, "east");
        run(computer, "east");
        run(computer, "take food ration");
        run(computer, "west");
        run(computer, "north");
        run(computer, "east");
        run(computer, "take candy cane");
        run(computer, "north");
        run(computer, "east");
        run(computer, "north");
        run(computer, "take mug");
        run(computer, "south");
        run(computer, "west");
        run(computer, "north");
        run(computer, "take mutex");
        run(computer, "north");
        run(computer, "take prime number");
        run(computer, "south");
        run(computer, "south");
        run(computer, "south");
        run(computer, "east");
        run(computer, "north");
        run(computer, "take loom");
        run(computer, "south");
        run(computer, "east");
        run(computer, "south");
        run(computer, "east");
        run(computer, "east");
        List<String> items = Arrays.asList(
                "prime number",
                "candy cane",
                "loom",
                "asterisk",
                "food ration",
                "boulder",
                "mutex",
                "mug"
        );
        boolean[] state = new boolean[items.size()];
        int v = testAll(computer, state, items, 0);
        System.out.println("Part 1: " + v);
    }

    private static int testAll(IntComputer computer, boolean[] state, List<String> items, int index) {
        if (index == state.length) {
            String inv = run(computer, "inv");
            Set<String> inventory = new HashSet<>();
            boolean seen = false;
            for (String row : inv.split("\n")) {
                if (row.startsWith("Items in your")) {
                    seen = true;
                }
                if (seen) {
                    if (row.length() == 0) {
                        break;
                    }
                    if (row.startsWith("- ")){
                        inventory.add(row.substring(2));
                    }
                }
            }
            for (int i = 0; i < state.length; ++i) {
                String item = items.get(i);
                if (state[i] != inventory.contains(item)) {
                    if (state[i]) {
                        run(computer, "take " + item);
                    } else {
                        run(computer, "drop " + item);
                    }
                }
            }
            String output = run(computer, "north");
            if (output.contains("Oh, hello! You should be able to get in by typing ")) {
                int i = output.indexOf("Oh, hello! You should be able to get in by typing ") + "Oh, hello! You should be able to get in by typing ".length();
                int j = output.indexOf(' ', i+2);
                return Integer.parseInt(output.substring(i, j));
            }
            return -1;
        } else {
            state[index] = true;
            int v = testAll(computer, state, items, index + 1);
            if (v != -1) {
                return v;
            }
            state[index] = false;
            return testAll(computer, state, items, index+1);
        }
    }

    private static String run(IntComputer computer, String command) {
        List<Long> input = new ArrayList<>();
        if (command != null) {
            for (int i = 0; i < command.length(); ++i) {
                input.add((long)command.charAt(i));
            }
            input.add(10L);
        }
        List<Long> output = computer.run(input);
        StringBuilder sb = new StringBuilder();
        for (Long aLong : output) {
            sb.append((char)(long)aLong);
        }
        return sb.toString();
    }
}
