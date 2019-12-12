import dim2.Direction;
import dim2.Pos;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
