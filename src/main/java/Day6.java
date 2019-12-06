import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Day6 {

    public static void main(String[] args) {
        final List<String> input = Util.readStrings();

        final int p1 = calcPart1(input);
        System.out.println("part 1: " + p1);

        calcPart2(input);
    }

    private static void calcPart2(List<String> input) {
        final Map<String, Node> nodes = new HashMap<>();
        for (String s : input) {
            final String[] split = s.split("\\)");
            final String a = split[0];
            final String b = split[1];
            if (!nodes.containsKey(b)) {
                nodes.put(b, new Node(b));
            }
            final Node node = nodes.get(b);
            if (!nodes.containsKey(a)) {
                nodes.put(a, new Node(a));
            }
            final Node near = nodes.get(a);
            near.near.add(node);
            node.near.add(near);
        }

        final Queue<Node> que = new LinkedList<>();
        que.add(nodes.get("YOU"));
        while (!que.isEmpty()) {
            final Node node = que.poll();
            int steps = node.steps;
            if (node.id.equals("SAN")) {
                System.out.println("part 2: " + (steps-2));
                break;
            }
            for (Node near : node.near) {
                if (near.steps == 0) {
                    near.steps = steps+1;
                    que.add(near);
                }
            }
        }
    }

    private static int calcPart1(List<String> input) {
        final Map<String, List<String>> orbits = new HashMap<>();
        for (String s : input) {
            final String[] split = s.split("\\)");
            final String a = split[0];
            final String b = split[1];
            orbits.putIfAbsent(a, new ArrayList<>());
            orbits.get(a).add(b);
        }
        return count(orbits, 0, "COM");
    }

    private static int count(Map<String, List<String>> orbits, int steps, String current) {
        final List<String> p = orbits.get(current);
        if (p == null) {
            return steps;
        }
        int sum = steps;
        for (String pp : p) {
            sum += count(orbits, steps+1, pp);
        }
        return sum;
    }

    private static class Node {
        private final String id;
        private final List<Node> near = new ArrayList<>();
        private int steps;

        private Node(String id) {
            this.id = id;
        }
    }
}
