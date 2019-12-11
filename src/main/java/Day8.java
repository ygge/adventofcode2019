import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {

    public static void main(String[] args) {
        String image = Util.readString();

        List<Map<Integer, Integer>> count = new ArrayList<>();
        for (int i = 0; i < image.length(); ++i) {
            int n = image.charAt(i)-'0';
            int layer = i/(25*6);
            if (count.size() == layer) {
                count.add(new HashMap<>());
            }
            Map<Integer, Integer> map = count.get(layer);
            Integer v = map.get(n);
            map.put(n, (v == null ? 0 : v) + 1);
        }
        int fewest = Integer.MAX_VALUE;
        int ans = 0;
        for (Map<Integer, Integer> map : count) {
            int zeros = map.getOrDefault(0, 0);
            if (zeros < fewest) {
                ans = map.getOrDefault(1, 0) * map.getOrDefault(2, 0);
                fewest = zeros;
            }
        }
        System.out.println("part 1: " + ans);

        System.out.println("part 2:");
        int x = 25;
        int y = 6;
        for (int i = 0; i < y; ++i) {
            for (int j = 0; j < x; ++j) {
                int c = 2;
                int layer = 0;
                while (c == 2) {
                    int index = layer * (y * x) + i * x + j;
                    c = image.charAt(index) - '0';
                    ++layer;
                }
                System.out.print(c == 0 ? " " : "x");
            }
            System.out.println();
        }
    }
}
