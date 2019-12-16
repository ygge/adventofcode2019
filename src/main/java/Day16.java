public class Day16 {

    private static final int[] PATTERN = new int[]{0, 1, 0, -1};

    public static void main(String[] args) {
        String code = Util.readString();
        for (int i = 0; i < 100; ++i) {
            code = runFFT(code);
        }
        System.out.println("part 1: " + code.substring(0, 8));
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
