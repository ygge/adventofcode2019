public class Day4 {

    public static void main(String[] args) {
        final int start = 147981;
        final int end = 691423;

        int p1 = 0;
        int p2 = 0;
        for (int i = start; i <= end; ++i) {
            final String n = Integer.toString(i);
            boolean ok = true;
            boolean seenSame = false;
            boolean twoSame = false;
            for (int c = 1; c < n.length(); ++c) {
                if (n.charAt(c) < n.charAt(c-1)) {
                    ok = false;
                }
                if (n.charAt(c) == n.charAt(c-1)) {
                    seenSame = true;
                    if ((c == 1 || (n.charAt(c-2) != n.charAt(c)))
                            && (c == n.length()-1 || n.charAt(c+1) != n.charAt(c))) {
                        twoSame = true;
                    }
                }
            }
            if (ok && seenSame) {
                ++p1;
                if (twoSame) {
                    ++p2;
                }
            }
        }
        System.out.println("part 1: " + p1);
        System.out.println("part 2: " + p2);
    }
}
