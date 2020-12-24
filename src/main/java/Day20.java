import dim2.Direction;
import dim2.Pos;

import java.util.*;

public class Day20 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        char[][] board = new char[input.size()][];
        for (int y = 0; y < input.size(); ++y) {
            board[y] = input.get(y).toCharArray();
        }
        Pos start = find(board, "AA").stream().findAny().orElseThrow();
        Pos goal = find(board, "ZZ").stream().findAny().orElseThrow();

        var part1 = part1(board, start, goal);
        System.out.println("Part 1: " + part1);

        var part2 = part2(board, start, goal);
        System.out.println("Part 2: " + part2);
    }

    private static int part2(char[][] board, Pos start, Pos goal) {
        Set<State2> seen = new HashSet<>();
        Deque<State2> que = new LinkedList<>();
        que.add(new State2(start));
        while (!que.isEmpty()) {
            var state = que.pollFirst();
            if (!seen.add(state)) {
                continue;
            }
            for (Direction dir : Direction.values()) {
                Pos newPos = state.pos.move(dir);
                if (newPos.equals(goal) && state.level == 0) {
                    return state.steps + 1;
                }
                char cc = board[newPos.y][newPos.x];
                if (cc == '.') {
                    que.add(new State2(state, newPos, 0));
                } else if (cc >= 'A' && cc <= 'Z') {
                    String name = findName(board, newPos);
                    boolean isOuter = newPos.y < 2 || newPos.y >= board.length-2 || newPos.x < 2 || newPos.x >= board[newPos.y].length-2;
                    if (isOuter && state.level == 0) {
                        continue;
                    }
                    if (name.equals("AA") || name.equals("ZZ")) {
                        continue;
                    }
                    Set<Pos> portals = find(board, name);
                    portals.remove(state.pos);
                    Pos portalPos = portals.stream().findAny().orElseThrow();
                    que.add(new State2(state, portalPos, isOuter ? -1 : 1));
                }
            }
        }
        throw new RuntimeException("Could not find goal");
    }

    private static int part1(char[][] board, Pos start, Pos goal) {
        Set<State> seen = new HashSet<>();
        Deque<State> que = new LinkedList<>();
        que.add(new State(start));
        while (!que.isEmpty()) {
            var state = que.pollFirst();
            if (!seen.add(state)) {
                continue;
            }
            for (Direction dir : Direction.values()) {
                Pos newPos = state.pos.move(dir);
                if (newPos.equals(goal)) {
                    return state.steps + 1;
                }
                char cc = board[newPos.y][newPos.x];
                if (cc == '.') {
                    que.add(new State(state, newPos));
                } else if (cc >= 'A' && cc <= 'Z') {
                    String name = findName(board, newPos);
                    if (name.equals("AA")) {
                        continue;
                    }
                    Set<Pos> portals = find(board, name);
                    portals.remove(state.pos);
                    Pos portalPos = portals.stream().findAny().orElseThrow();
                    que.add(new State(state, portalPos));
                }
            }
        }
        throw new RuntimeException("Could not find goal");
    }

    private static Pos findOpen(char[][] board, Pos pos) {
        Pos other = null;
        for (int dy = -1; dy <= 1; ++dy) {
            for (int dx = -1; dx <= 1; ++dx) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                if (pos.y+dy >= 0 && pos.y+dy < board.length
                        && pos.x+dx >= 0 && pos.x+dx < board[pos.y+dy].length) {
                    char c = board[pos.y+dy][pos.x+dx];
                    if (c == '.') {
                        return new Pos(pos.x+dx, pos.y+dy);
                    } else if (c >= 'A' && c <= 'Z') {
                        other = new Pos(pos.x+dx, pos.y+dy);
                    }
                }
            }
        }
        if (other != null) {
            return findOpen(board, other);
        }
        throw new RuntimeException("No open space found around " + pos);
    }

    private static Set<Pos> find(char[][] board, String s) {
        Set<Pos> pos = new HashSet<>();
        char c = s.charAt(0);
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] == c && findName(board, new Pos(x, y)).equals(s)) {
                    pos.add(findOpen(board, new Pos(x, y)));
                }
            }
        }
        return pos;
    }

    private static String findName(char[][] board, Pos pos) {
        for (int dy = -1; dy <= 1; ++dy) {
            for (int dx = -1; dx <= 1; ++dx) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                if (pos.y+dy >= 0 && pos.y+dy < board.length
                        && pos.x+dx >= 0 && pos.x+dx < board[pos.y+dy].length) {
                    char c = board[pos.y+dy][pos.x+dx];
                    if (c >= 'A' && c <= 'Z') {
                        if (dy < 0 || dx < 0) {
                            return board[pos.y+dy][pos.x+dx] + "" + board[pos.y][pos.x];
                        } else {
                            return board[pos.y][pos.x] + "" + board[pos.y+dy][pos.x+dx];
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Name not found for pos " + pos);
    }

    private static class State {
        Pos pos;
        int steps;

        public State(Pos pos) {
            this.pos = pos;
            steps = 0;
        }

        public State(State state, Pos pos) {
            this.pos = pos;
            steps = state.steps + 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Objects.equals(pos, state.pos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos);
        }

        @Override
        public String toString() {
            return "State{" +
                    "pos=" + pos +
                    ", steps=" + steps +
                    '}';
        }
    }

    private static class State2 {
        Pos pos;
        int steps, level;

        public State2(Pos pos) {
            this.pos = pos;
            steps = 0;
            level = 0;
        }

        public State2(State2 state, Pos pos, int deltaLevel) {
            this.pos = pos;
            steps = state.steps + 1;
            level = state.level + deltaLevel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State2 state = (State2) o;
            return Objects.equals(pos, state.pos) && level == state.level;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, level);
        }

        @Override
        public String toString() {
            return "State{" +
                    "pos=" + pos +
                    "level=" + level +
                    ", steps=" + steps +
                    '}';
        }
    }
}
