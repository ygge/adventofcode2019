import dim2.Pos;

import java.util.HashMap;
import java.util.Map;

public class Day19 {

    public static void main(String[] args) {
        var input = Util.readString();
        IntComputer comp = new IntComputer(input);
        int count = 0;
        for (int x = 0; x < 50; ++x) {
            for (int y = 0; y < 50; ++y) {
                comp.reset();
                var out = comp.run((long)x, (long)y);
                if (out.get(0) == 1) {
                    ++count;
                }
            }
        }
        System.out.println("part 1: " + count);

        int x = 0;
        Map<Pos, Boolean> map = new HashMap<>();
        for (int y = 0; x >= 0; ++y) {
            int tx = x;
            boolean out = false;
            for (int i = 0; i < 15; ++i) {
                out = calc(map, comp, x, y);
                if (out) {
                    break;
                }
                ++x;
            }
            if (!out) {
                x = tx;
                continue;
            }
            int dx = 0;
            while (out) {
                ++dx;
                out = calc(map, comp, x+dx, y);
            }
            int dd = 0;
            while (dx-dd >= 100) {
                int dy = 1;
                for (; dy < 100; ++dy) {
                    out = calc(map, comp, x+dd, y+dy);
                    if (!out) {
                        break;
                    }
                }
                if (dy == 100) {
                    boolean ok = true;
                    for (int i = 0; i < 100 && ok; ++i) {
                        for (int j = 0; j < 100; ++j) {
                            if (!calc(map, comp, x+dd+i, y+j)) {
                                ok = false;
                                break;
                            }
                        }
                    }
                    if (ok) {
                        System.out.println("part 2: " + ((x+dd)*10000+y));
                        x = -1;
                        break;
                    }
                }
                ++dd;
            }
        }
    }

    private static boolean calc(Map<Pos, Boolean> map, IntComputer comp, int x, int y) {
        Pos pos = new Pos(x, y);
        if (map.containsKey(pos)) {
            return map.get(pos);
        }
        comp.reset();
        if (x < 0 || y < 0) {
            throw new RuntimeException();
        }
        var out = comp.run((long)x, (long)y).get(0);
        var res = out == 1;
        map.put(pos, res);
        return res;
    }
}
