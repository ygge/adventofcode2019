import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day3 {

    public static void main(String[] args) {
        final List<String> wires = Util.readStrings();

        final Set<Pos> pos = new HashSet<>();
        mapPositions(wires.get(0), pos, true);
        final Pos p = mapPositions(wires.get(1), pos, false);
        System.out.println("Part 1: " + p.value());

        final Map<Pos, Integer> stepsByPos = new HashMap<>();
        mapPositions(wires.get(0), stepsByPos, true);
        final int len = mapPositions(wires.get(1), stepsByPos, false);
        System.out.println("Part 2: " + len);
    }

    private static Pos mapPositions(String path, Set<Pos> pos, boolean map) {
        Pos p = new Pos(0, 0);
        Pos smallest = null;
        for (String w : path.split(",")) {
            char c = w.charAt(0);
            int n = Integer.parseInt(w.substring(1));
            for (int i = 0; i < n; ++i) {
                p = p.move(c);
                if (map) {
                    pos.add(p);
                } else if (pos.contains(p)) {
                    smallest = smallest == null || p.value() < smallest.value() ? p : smallest;
                }
            }
        }
        return smallest;
    }

    private static int mapPositions(String path, Map<Pos, Integer> pos, boolean map) {
        final Map<Pos, Integer> current = new HashMap<>();
        Pos p = new Pos(0, 0);
        int smallest = Integer.MAX_VALUE;
        int steps = 0;
        for (String w : path.split(",")) {
            char c = w.charAt(0);
            int n = Integer.parseInt(w.substring(1));
            for (int i = 0; i < n; ++i) {
                p = p.move(c);
                ++steps;
                if (!current.containsKey(p)) {
                    current.put(p, steps);
                    if (map) {
                        pos.put(p, steps);
                    } else if (pos.containsKey(p)) {
                        smallest = Math.min(smallest, steps + pos.get(p));
                    }
                }
            }
        }
        return smallest;
    }

    private static final class Pos {
        private final int x, y;

        private Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private Pos move(char c) {
            if (c == 'U') {
                return new Pos(x, y-1);
            } else if (c == 'R') {
                return new Pos(x+1, y);
            } else if (c == 'D') {
                return new Pos(x, y+1);
            } else if (c == 'L') {
                return new Pos(x-1, y);
            }
            throw new RuntimeException("Wrong move: " + c);
        }

        private int value() {
            return Math.abs(x)+Math.abs(y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos pos = (Pos) o;
            return x == pos.x &&
                    y == pos.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Pos{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
