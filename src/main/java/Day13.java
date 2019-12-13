import dim2.Pos;

import java.util.Collections;
import java.util.List;

public class Day13 {

    public static void main(String[] args) {
        final String code = Util.readString();

        final IntComputer program = new IntComputer(code);
        final List<Long> out = program.run();
        char[][] board = parseBoard(out);
        int blocks = 0;
        for (char[] row : board) {
            for (char c : row) {
                if (c == '.') {
                    ++blocks;
                }
            }
        }
        printBoard(board);
        System.out.println("part 1: " + blocks);

        program.reset();
        program.set(0, 2);
        final List<Long> game = program.run(Collections.singletonList(0L));
        board = parseBoard(game);
        Pos ball = null;
        int p = 0;
        int score = 0;
        int pDir = 0;
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                char c = board[y][x];
                if (c == 'O') {
                    ball = new Pos(x, y);
                } else if (c == '-') {
                    p = x;
                }
            }
        }
        assert ball != null;
        while (blocks > 0) {
            final List<Long> state = program.run(Collections.singletonList((long)pDir));
            if (state.size() == 0) {
                System.out.println("Game over!");
                break;
            }
            for (int i = 0; i < state.size(); i += 3) {
                final int x = (int)(long)state.get(i);
                if (x == -1) {
                    score = (int)(long)state.get(i+2);
                    continue;
                }
                final int y = (int)(long)state.get(i+1);
                final char c = toChar((int)(long)state.get(i+2));
                if (board[y][x] == '.' && c != '.') {
                    --blocks;
                }
                if (c == 'O') {
                    ball = new Pos(x, y);
                } else if (c == '-') {
                    p = x;
                }
                board[y][x] = c;
            }
            pDir = ball.x - p;
        }
        System.out.println("part 2: " + score);
    }

    private static char[][] parseBoard(List<Long> out) {
        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < out.size(); i += 3) {
            maxX = Math.max(maxX, (int)(long)out.get(i));
            maxY = Math.max(maxY, (int)(long)out.get(i+1));
        }
        final char[][] board = new char[maxY+1][maxX+1];
        for (int i = 0; i < out.size(); i += 3) {
            final int x = (int)(long)out.get(i);
            if (x == -1) {
                continue;
            }
            final int y = (int)(long)out.get(i+1);
            board[y][x] = toChar((int) (long) out.get(i + 2));
        }
        return board;
    }

    private static void printBoard(char[][] board) {
        for (char[] row : board) {
            System.out.println(new String(row));
        }
    }

    private static char toChar(int v) {
        switch(v) {
            case 0: return ' ';
            case 1: return '#';
            case 2: return '.';
            case 3: return '-';
            case 4: return 'O';
            default:
                throw new RuntimeException("Incorrect type: " + v);
        }
    }
}
