import dim2.Direction;
import dim2.Pos;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class Day15 {

    public static void main(String[] args) {
        final IntComputer program = new IntComputer(Util.readString());

        final Map<Pos, Character> seen = new HashMap<>();
        seen.put(new Pos(0, 0), 'S');
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        Pos pos = new Pos(0, 0);
        while (true) {
            final Direction dir = bestMove(seen, pos);
            if (dir == null) {
                break;
            }
            int move = toMove(dir);
            int result = (int) (long) program.run(Collections.singletonList((long) move)).get(0);
            final Pos newPos = pos.move(dir);
            if (result == 2) {
                seen.put(newPos, 'O');
            } else if (result == 1) {
                seen.put(newPos, '.');
            } else if (result == 0) {
                seen.put(newPos, '#');
            }
            minX = Math.min(minX, newPos.x);
            minY = Math.min(minY, newPos.y);
            maxX = Math.max(maxX, newPos.x);
            maxY = Math.max(maxY, newPos.y);
            if (result != 0) {
                pos = newPos;
            }
        }
        printBoard(seen, minX, minY, maxX, maxY, null);

        int steps = calcSteps(seen);
        System.out.println("part 1: " + steps);

        int minutes = calcMinutes(seen);
        System.out.println("part 1: " + minutes);
    }

    private static int calcMinutes(Map<Pos, Character> seen) {
        final Pos startPos = seen.entrySet().stream()
                .filter(entry -> entry.getValue() == 'O')
                .map(Map.Entry::getKey)
                .findAny()
                .orElseThrow();
        int max = 0;
        final Queue<Node> que = new LinkedList<>();
        final Node startNode = new Node(startPos, null, 0);
        que.add(startNode);
        final Set<Pos> path = new HashSet<>();
        path.add(startNode.pos);
        while (!que.isEmpty()) {
            final Node node = que.poll();
            max = Math.max(node.steps, max);
            for (Direction dir : Direction.values()) {
                Pos newPos = node.pos.move(dir);
                if (path.contains(newPos)) {
                    continue;
                }
                if (seen.get(newPos) == '#') {
                    continue;
                }
                Node newNode = new Node(newPos, dir, node.steps+1);
                path.add(newPos);
                que.add(newNode);
            }
        }
        return max;
    }

    private static int calcSteps(Map<Pos, Character> seen) {
        final Pos startPos = new Pos(0, 0);
        final Pos endPos = seen.entrySet().stream()
                .filter(entry -> entry.getValue() == 'O')
                .map(Map.Entry::getKey)
                .findAny()
                .orElseThrow();
        final Queue<Node> que = new LinkedList<>();
        final Node startNode = new Node(startPos, null);
        que.add(startNode);
        final Map<Pos, Node> path = new HashMap<>();
        path.put(startNode.pos, startNode);
        while (!que.isEmpty()) {
            final Node node = que.poll();
            for (Direction dir : Direction.values()) {
                Pos newPos = node.pos.move(dir);
                if (path.containsKey(newPos)) {
                    continue;
                }
                if (newPos.equals(endPos)) {
                    int steps = 1;
                    Pos p = node.pos;
                    while (!p.equals(startPos)) {
                        final Direction d = path.get(p).dir;
                        p = p.move(d.reverse());
                        ++steps;
                    }
                    return steps;
                }
                if (seen.get(newPos) == '#') {
                    continue;
                }
                Node newNode = new Node(newPos, dir);
                path.put(newPos, newNode);
                que.add(newNode);
            }
        }
        throw new IllegalStateException();
    }

    private static void printBoard(Map<Pos, Character> seen, int minX, int minY, int maxX, int maxY, Pos droid) {
        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                char c = seen.getOrDefault(new Pos(x, y), ' ');
                if (new Pos(x, y).equals(droid)) {
                    c = 'D';
                }
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private static int toMove(Direction dir) {
        switch (dir) {
            case UP:
                return 1;
            case LEFT:
                return 3;
            case DOWN:
                return 2;
            case RIGHT:
                return 4;
        }
        throw new RuntimeException();
    }

    private static Direction bestMove(Map<Pos, Character> seen, Pos pos) {
        if (!seen.containsKey(pos.move(Direction.UP))) {
            return Direction.UP;
        } else if (!seen.containsKey(pos.move(Direction.RIGHT))) {
            return Direction.RIGHT;
        } else if (!seen.containsKey(pos.move(Direction.DOWN))) {
            return Direction.DOWN;
        } else if (!seen.containsKey(pos.move(Direction.LEFT))) {
            return Direction.LEFT;
        }

        final Queue<Node> que = new LinkedList<>();
        final Node startNode = new Node(pos, null);
        que.add(startNode);
        final Map<Pos, Node> path = new HashMap<>();
        path.put(pos, startNode);
        while (!que.isEmpty()) {
            final Node node = que.poll();
            for (Direction dir : Direction.values()) {
                Pos newPos = node.pos.move(dir);
                if (path.containsKey(newPos)) {
                    continue;
                }
                if (!seen.containsKey(newPos)) {
                    Direction d = path.get(node.pos).dir;
                    Pos p = node.pos;
                    while (!p.equals(pos)) {
                        d = path.get(p).dir;
                        p = p.move(d.reverse());
                    }
                    return d;
                }
                if (seen.get(newPos) == '#') {
                    continue;
                }
                Node newNode = new Node(newPos, dir);
                path.put(newPos, newNode);
                que.add(newNode);
            }
        }
        return null;
    }

    private static class Node {
        private final Pos pos;
        private final Direction dir;
        private final int steps;

        private Node(Pos pos, Direction dir) {
            this(pos, dir, 0);
        }

        private Node(Pos pos, Direction dir, int steps) {
            this.pos = pos;
            this.dir = dir;
            this.steps = steps;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(pos, node.pos) &&
                    dir == node.dir;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, dir);
        }
    }
}
