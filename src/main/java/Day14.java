import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {

    public static void main(String[] args) {
        List<String> input = Util.readStrings();

        final Map<String, Node> nodes = new HashMap<>();
        for (String s : input) {
            final String[] split = s.split("=>");
            final String in = split[0];
            final Node node = new Node(split[1]);
            if (nodes.containsKey(node.type)) {
                throw new RuntimeException("Bad!");
            }
            nodes.put(node.type, node);
            for (String raw : in.split(",")) {
                final String[] s1 = raw.trim().split(" ");
                node.toProduce.add(new Edge(s1[1], Integer.parseInt(s1[0])));
            }
        }
        calcPart1(nodes);
        calcPart2(nodes);
    }

    private static void calcPart2(Map<String, Node> nodes) {
        final Map<String, Integer> left = new HashMap<>();
        final Node fuelNode = nodes.get("FUEL");
        long ore = 0;
        int fuel = 0;
        while (true) {
            final int needed = calcNeeded(nodes, left, fuelNode, fuelNode.quantity);
            ore += needed;
            if (ore > 1000000000000L) {
                break;
            }
            fuel += fuelNode.quantity;
        }
        System.out.println("part 2: " + fuel);
    }

    private static void calcPart1(Map<String, Node> nodes) {
        final Map<String, Integer> left = new HashMap<>();
        final Node fuelNode = nodes.get("FUEL");
        final int needed = calcNeeded(nodes, left, fuelNode, fuelNode.quantity);
        System.out.println("part 1: " + needed);
    }

    private static int calcNeeded(Map<String, Node> nodes, Map<String, Integer> left, Node node, int quantity) {
        if (node == null) {
            return quantity;
        }
        final Integer r = left.getOrDefault(node.type, 0);
        final int q = ((quantity-r)+node.quantity-1)/node.quantity;
        left.put(node.type, node.quantity*q-(quantity-r));
        int total = 0;
        for (Edge edge : node.toProduce) {
            final Node raw = nodes.get(edge.rawMaterial);
            total += calcNeeded(nodes, left, raw, q*edge.quantity);
        }
        return total;
    }

    private static final class Node {
        private final String type;
        private final int quantity;
        private final List<Edge> toProduce = new ArrayList<>();

        private Node(String typeAndQuantity) {
            String[] s = typeAndQuantity.trim().split(" ");
            this.type = s[1].trim();
            this.quantity = Integer.parseInt(s[0].trim());
        }
    }

    private static final class Edge {
        private final String rawMaterial;
        private final int quantity;

        private Edge(String rawMaterial, int quantity) {
            this.rawMaterial = rawMaterial;
            this.quantity = quantity;
        }
    }
}
