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
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[i].length; ++j) {
                int count = calc(map, i, j);
                if (count > best) {
                    System.out.println(j + " " + i);
                    best = count;
                }
            }
        }
        System.out.println("part 1: " + best);
    }

    private static int calc(boolean[][] map, int y, int x) {
        if (!map[y][x]) {
            return 0;
        }
        int count = 0;
        boolean[][] t = new boolean[map.length][map[y].length];
        for (int i = 1; y-i >= 0 || y+i < map.length || x-i >= 0 || x+i <= map.length; ++i) {
            for (int dy = -i; dy <= i; ++dy) {
                if (ok(map, y, x, dy, i, t)) {
                    ++count;
                }
                if (ok(map, y, x, dy, -i, t)) {
                    ++count;
                }
            }
            for (int dx = -i; dx <= i; ++dx) {
                if (ok(map, y, x, i, dx, t)) {
                    ++count;
                }
                if (ok(map, y, x, -1, dx, t)) {
                    ++count;
                }
            }
        }
        return count;
    }

    private static boolean ok(boolean[][] map, int y, int x, int dy, int dx, boolean[][] t) {
        if (onMap(map, y+dy, x+dx)) {
            if (map[y+dy][x+dx] && !t[y+dy][x+dx]) {
                for (int i = 1; onMap(map, y+dy*i, x+dx*i); ++i) {
                    t[y+dy*i][x+dx*i] = true;
                }
                return true;
            }
        }
        return false;
    }

    private static boolean onMap(boolean[][] map, int y, int x) {
        return y >= 0 && y < map.length && x >= 0 && x < map[y].length;
    }
}
