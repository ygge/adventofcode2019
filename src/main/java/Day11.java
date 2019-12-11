import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day11 {

    public static void main(String[] args) {
        final String code = Util.readString();
        final IntComputer program = new IntComputer(code);

        final Map<Pos, Integer> colors = new HashMap<>();
        Pos pos = new Pos(0, 0);
        colors.put(pos, 1);
        Direction dir = Direction.UP;
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        do {
            int c = colors.getOrDefault(pos, 0);
            List<Long> result = program.run(Collections.singletonList((long)c));
            colors.put(pos, (int)(long)result.get(0));
            dir = dir.turn((int)(long)result.get(1));
            pos = pos.move(dir);
            minX = Math.min(minX, pos.x);
            minY = Math.min(minY, pos.y);
            maxX = Math.max(maxX, pos.x);
            maxY = Math.max(maxY, pos.y);
        } while (program.isRunning());
        System.out.println("part 1: " + colors.keySet().size());

        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                int c = colors.getOrDefault(new Pos(x, y), -1);
                if (c == 0) {
                    System.out.print(" ");
                } else if (c == 1) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private static class Pos {
        final int x, y;

        private Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Pos move(Direction dir) {
            return new Pos(x+dir.dx, y+dir.dy);
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

    private enum Direction {
        UP(-1, 0),
        LEFT(0, -1),
        DOWN(1, 0),
        RIGHT(0, 1);

        private final int dy, dx;

        Direction(int dy, int dx) {

            this.dy = dy;
            this.dx = dx;
        }

        Direction turn(int dir) {
            switch(this) {
                case UP:
                    return dir == 0 ? LEFT : RIGHT;
                case LEFT:
                    return dir == 0 ? DOWN : UP;
                case DOWN:
                    return dir == 0 ? RIGHT : LEFT;
                case RIGHT:
                    return dir == 0 ? UP : DOWN;
            }
            throw new IllegalStateException();
        }
    }
}
