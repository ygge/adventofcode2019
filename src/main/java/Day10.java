import java.util.ArrayList;
import java.util.List;

public class Day10 {

    public static void main(String[] args) {
        final List<String> input = Util.readStrings();

        final boolean[][] map = new boolean[input.size()][input.get(0).length()];
        for (int i = 0; i < input.size(); ++i) {
            for (int j = 0; j < input.get(i).length(); ++j) {
                map[i][j] = input.get(i).charAt(j) == '#';
            }
        }
        int best = 0;
        Pos bestPos = null;
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[i].length; ++j) {
                final List<Pos> seen = calc(map, i, j);
                if (seen.size() > best) {
                    bestPos = new Pos(j, i);
                    best = seen.size();
                }
            }
        }
        assert bestPos != null;
        System.out.println("part 1: " + best);

        calcPart2(map, bestPos);
    }

    private static void calcPart2(boolean[][] map, Pos pos) {
        int total = 0;
        while (true) {
            final List<Pos> destroyed = calc(map, pos.y, pos.x);
            if (total + destroyed.size() >= 200) {
                destroyed.sort((o1, o2) -> {
                    final double a1 = angle(o1, pos);
                    final double a2 = angle(o2, pos);
                    return a1 < a2 ? -1 : 1;
                });
                final Pos destroy200 = destroyed.get(200 - total - 1);
                System.out.println("part 2: " + (destroy200.x * 100 + destroy200.y));
                break;
            }
            total += destroyed.size();
            destroyed.forEach(p -> map[p.y][p.x] = false);
        }
    }

    private static double angle(Pos p1, Pos p2) {
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        if (dy <= 0) {
            if (dx < 0) {
                return Math.atan(Math.abs(dy) * 1.0 / Math.abs(dx)) + 3 * Math.PI / 2;
            }
            return Math.atan(Math.abs(dx) * 1.0 / Math.abs(dy));
        }
        if (dx <= 0) {
            return Math.atan(Math.abs(dx) * 1.0 / Math.abs(dy)) + Math.PI;
        }
        return Math.atan(Math.abs(dy) * 1.0 / Math.abs(dx)) + Math.PI / 2;
    }

    private static List<Pos> calc(boolean[][] map, int y, int x) {
        final List<Pos> seen = new ArrayList<>();
        if (!map[y][x]) {
            return seen;
        }
        boolean[][] t = new boolean[map.length][map[y].length];
        for (int i = 1; y - i >= 0 || y + i < map.length || x - i >= 0 || x + i <= map.length; ++i) {
            for (int dy = -i; dy <= i; ++dy) {
                addSeen(map, seen, y, x, dy, i, t);
                addSeen(map, seen, y, x, dy, -i, t);
            }
            for (int dx = -i; dx <= i; ++dx) {
                addSeen(map, seen, y, x, i, dx, t);
                addSeen(map, seen, y, x, -i, dx, t);
            }
        }
        return seen;
    }

    private static void addSeen(boolean[][] map, List<Pos> seen, int y, int x, int dy, int dx, boolean[][] t) {
        if (onMap(map, y + dy, x + dx)) {
            boolean found = false;
            if (!t[y + dy][x + dx]) {
                for (int i = 1; onMap(map, y + dy * i, x + dx * i); ++i) {
                    if (!t[y + dy * i][x + dx * i] && map[y + dy * i][x + dx * i] && !found) {
                        seen.add(new Pos(x + dx * i, y + dy * i));
                        found = true;
                    }
                    t[y + dy * i][x + dx * i] = true;
                }
            }
        }
    }

    private static boolean onMap(boolean[][] map, int y, int x) {
        return y >= 0 && y < map.length && x >= 0 && x < map[y].length;
    }

    private static final class Pos {
        private final int x, y;

        private Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
