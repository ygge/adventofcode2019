public class Day16 {

    private static final int[] PATTERN = new int[]{0, 1, 0, -1};

    public static void main(String[] args) {
        String code = Util.readString();
        //String code = "03036732577212944063491565474664";
        String part1 = part1(code);
        System.out.println("part 1: " + part1.substring(0, 8));

        String part2 = part2(code);
        System.out.println("part 2: " + part2);
    }

    private static String part2(String code) {
        int offset = Integer.parseInt(code.substring(0, 7));
        System.out.println(offset);
        int len = code.length();
        int[] data = new int[len*10000];
        for (int i = 0; i < len; ++i) {
            int v = code.charAt(i)-'0';
            for (int j = 0; j < 10000; ++j) {
                data[len*j+i] = v;
            }
        }
        for (int i = 0; i < 100; ++i) {
            for (int j = data.length-2; j >= offset; --j) {
                data[j] = Math.abs(data[j]+data[j+1])%10;
            }
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            res.append(data[offset+i]);
        }
        return res.toString();
    }

    private static String part1(String code) {
        for (int i = 0; i < 100; ++i) {
            code = runFFT(code);
        }
        return code;
    }

    private static String runFFT(String code) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length(); ++i) {
            int sum = 0;
            int index = i == 0 ? 1 : 0;
            int used = i == 0 ? 0 : 1;
            for (int j = 0; j < code.length(); ++j) {
                int v = code.charAt(j) - '0';
                int f = PATTERN[index];
                if (++used == i+1) {
                    used = 0;
                    if (++index == PATTERN.length) {
                        index = 0;
                    }
                }
                sum += v*f;
            }
            sb.append(Math.abs(sum%10));
        }
        return sb.toString();
    }
}
