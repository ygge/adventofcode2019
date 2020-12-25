import java.util.*;

public class Day24 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        /*
        var input = Arrays.asList("" +
                "....#",
                "#..#.",
                "#..##",
                "..#..",
                "#....");
         */
        char[][] board = new char[input.size()][];

        for (int i = 0; i < board.length; ++i) {
            board[i] = input.get(i).toCharArray();
        }
        int part1 = part1(board);
        System.out.println("Part 1: " + part1);

        for (int i = 0; i < board.length; ++i) {
            board[i] = input.get(i).toCharArray();
        }
        int part2 = part2(board);
        System.out.println("Part 2: " + part2);
    }

    private static int part2(char[][] board) {
        Map<Integer, char[][]> boards = new HashMap<>();
        boards.put(0, board);
        for (int i = 0; i < 200; ++i) {
            boards = move(boards);
        }
        int count = 0;
        for (char[][] value : boards.values()) {
            for (char[] chars : value) {
                for (char aChar : chars) {
                    if (aChar == '#') {
                        ++count;
                    }
                }
            }
        }
        return count;
    }

    private static Map<Integer, char[][]> move(Map<Integer, char[][]> boards) {
        Map<Integer, char[][]> newBoards = new HashMap<>();
        for (Map.Entry<Integer, char[][]> entry : boards.entrySet()) {
            char[][] board = entry.getValue();
            var newBoard = move(board, boards.get(entry.getKey()-1), boards.get(entry.getKey()+1));
            newBoards.put(entry.getKey(), newBoard);
        }
        int s = boards.size()/2;
        newBoards.put(s+1, move(null, boards.get(s), null));
        newBoards.put(-s-1, move(null, null, boards.get(-s)));
        return newBoards;
    }

    private static char[][] move(char[][] board, char[][] lowerBoard, char[][] higherBoard) {
        char[][] newBoard = new char[5][5];
        for (int y = 0; y < newBoard.length; ++y) {
            for (int x = 0; x < newBoard[y].length; ++x) {
                if (y == 2 && x == 2) {
                    newBoard[y][x] = '?';
                    continue;
                }
                int count = 0;
                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dx = -1; dx <= 1; ++dx) {
                        if ((dy != 0 || dx != 0) && (dy == 0 || dx == 0)) {
                            if (y+dy == 2 && x+dx == 2) {
                                if (lowerBoard != null) {
                                    if (dy == -1) {
                                        for (char chars : lowerBoard[lowerBoard.length-1]) {
                                            if (chars == '#') {
                                                ++count;
                                            }
                                        }
                                    } else if (dy == 1) {
                                        for (char chars : lowerBoard[0]) {
                                            if (chars == '#') {
                                                ++count;
                                            }
                                        }
                                    } else if (dx == -1) {
                                        for (char[] chars : lowerBoard) {
                                            if (chars[chars.length - 1] == '#') {
                                                ++count;
                                            }
                                        }
                                    } else {
                                        for (char[] chars : lowerBoard) {
                                            if (chars[0] == '#') {
                                                ++count;
                                            }
                                        }
                                    }
                                }
                            } else if (y+dy == -1) {
                                if (higherBoard != null && higherBoard[1][2] == '#') {
                                    ++count;
                                }
                            } else if (x+dx == -1) {
                                if (higherBoard != null && higherBoard[2][1] == '#') {
                                    ++count;
                                }
                            } else if (y+dy == newBoard.length) {
                                if (higherBoard != null && higherBoard[3][2] == '#') {
                                    ++count;
                                }
                            } else if (x+dx == newBoard[y].length) {
                                if (higherBoard != null && higherBoard[2][3] == '#') {
                                    ++count;
                                }
                            } else if (board != null && board[y+dy][x+dx] == '#') {
                                ++count;
                            }
                        }
                    }
                }
                boolean active = board != null && board[y][x] == '#';
                if (active && count != 1) {
                    newBoard[y][x] = '.';
                } else if (!active && (count == 1 || count == 2)) {
                    newBoard[y][x] = '#';
                } else {
                    newBoard[y][x] = active ? '#' : '.';
                }
            }
        }
        return newBoard;
    }

    private static int part1(char[][] board) {
        Set<Integer> seen = new HashSet<>();
        while (seen.add(rating(board))) {
            board = move(board);
            /*
            for (char[] chars : board) {
                System.out.println(new String(chars));
            }
            System.out.println();
             */
        }
        return rating(board);
    }

    private static char[][] move(char[][] board) {
        var newBoard = new char[board.length][board[0].length];
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                int c = count(board, x, y);
                if (board[y][x] == '#' && c != 1) {
                    newBoard[y][x] = '.';
                } else if (board[y][x] == '.' && (c == 1 || c == 2)) {
                    newBoard[y][x] = '#';
                } else {
                    newBoard[y][x] = board[y][x];
                }
            }
        }
        return newBoard;
    }

    private static int count(char[][] board, int x, int y) {
        int count = 0;
        for (int dy = -1; dy <= 1; ++dy) {
            for (int dx = -1; dx <= 1; ++dx) {
                if ((dy != 0 || dx != 0)
                        && (dy == 0 || dx == 0)
                        && y+dy >= 0 && y+dy < board.length
                        && x+dx >= 0 && x+dx < board[y].length) {
                    if (board[y+dy][x+dx] == '#') {
                        ++count;
                    }
                }
            }
        }
        return count;
    }

    private static int rating(char[][] board) {
        int p = 1;
        int sum = 0;
        for (char[] chars : board) {
            for (char aChar : chars) {
                if (aChar == '#') {
                    sum += p;
                }
                p *= 2;
            }
        }
        return sum;
    }
}
