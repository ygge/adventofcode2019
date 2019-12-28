import java.util.ArrayList;
import java.util.List;

public class Day17 {

    public static void main(String[] args) {
        final IntComputer program = new IntComputer(Util.readString());

        final List<Long> output = program.run();

        final int width = calcWidth(output);
        final int height = output.size()/(width+1);
        final char[][] board = new char[height][width];

        int x = 0;
        int y = 0;
        for (Long c : output) {
            System.out.printf("%c", (char)(long)c);
            if (c == 10) {
                x = 0;
                ++y;
            } else {
                board[y][x] = (char)(long)c;
                ++x;
            }
        }

        int sum = 0;
        for (y = 1; y < height-1; ++y) {
            for (x = 1; x < width-1; ++x) {
                if (board[y][x] == '#' && intersection(board, x, y)) {
                    sum += y*x;
                }
            }
        }
        System.out.println("part 1: " + sum);

        program.reset();
        program.set(0, 2);
        final List<Long> input = new ArrayList<>();
        input.addAll(toAscii("B,C,B,A,B,C,A,B,C,A"));
        input.addAll(toAscii("R,12,R,4,L,12,L,12"));
        input.addAll(toAscii("R,8,R,10,R,10"));
        input.addAll(toAscii("R,4,R,8,R,10,R,12"));
        input.addAll(toAscii("n"));
        final List<Long> dust = program.run(input);
        System.out.println("part 2: " + dust.get(dust.size()-1));
    }

    private static List<Long> toAscii(String s) {
        final List<Long> ascii = new ArrayList<>();
        for (int i = 0; i < s.length(); ++i) {
            ascii.add((long)s.charAt(i));
        }
        ascii.add((long)10);
        return ascii;
    }

    private static boolean intersection(char[][] board, int x, int y) {
        return board[y-1][x] == '#' && board[y][x+1] == '#' && board[y+1][x] == '#' && board[y][x-1] == '#';
    }

    private static int calcWidth(List<Long> board) {
        int index = 0;
        for (Long c : board) {
            if (c == 10) {
                return index;
            }
            ++index;
        }
        throw new IllegalStateException("Not good!");
    }
}
// R,8,R,10,R,10,R,4,R,8,R,10,R,12,R,8,R,10,R,10,R,12,R,4,L,12,L,12,R,8,R,10,R,10,R,4,R,8,R,10,R,12,R,12,R,4,L,12,L,12,
// R,8,R,10,R,10,R,4,R,8,R,10,R,12,R,12,R,4,L,12,L,12

// B,C,A,B,A,
// B,A

// A - L,12,L,12
// B - R,12,R,4,R,8,R,10,R,10,R,4,R,8,R,10,R,12
// C - R,8,R,10,R,10


// B,C,B,A,B,C,A,B,C,A

// A - R,12,R,4,L,12,L,12
// B - R,8,R,10,R,10
// C - R,4,R,8,R,10,R,12
