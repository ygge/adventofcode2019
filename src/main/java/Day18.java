import dim2.Direction;
import dim2.Pos;

import java.util.*;

public class Day18 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        char[][] board = new char[input.size()][];
        for (int i = 0; i < input.size(); ++i) {
            board[i] = input.get(i).toCharArray();
        }
        Pos pos = findStart(board);
        int numKeys = countKeys(board);

        int part1 = search(board, pos, numKeys);
        System.out.println("part 1: " + part1);

        board[pos.y][pos.x] = '#';
        board[pos.y][pos.x-1] = '#';
        board[pos.y][pos.x+1] = '#';
        board[pos.y-1][pos.x] = '#';
        board[pos.y+1][pos.x] = '#';
        List<Pos> startPos = Arrays.asList(
                new Pos(pos.x-1, pos.y-1),
                new Pos(pos.x-1, pos.y+1),
                new Pos(pos.x+1, pos.y-1),
                new Pos(pos.x+1, pos.y+1)
        );
        int part2 = search(board, startPos, numKeys);
        System.out.println("part 2: " + part2);
    }

    private static int search(char[][] board, List<Pos> startPos, int numKeys) {
        Set<State2> seen = new HashSet<>();
        PriorityQueue<State2> que = new PriorityQueue<>(Comparator.comparingInt(s -> s.steps));
        que.add(new State2(startPos));
        int best = Integer.MAX_VALUE;
        while (!que.isEmpty()) {
            var state = que.poll();
            if (!seen.add(state) || state.steps >= best) {
                continue;
            }
            for (int i = 0; i < state.pos.size(); ++i) {
                var newStates = search(board, state.pos.get(i), state.keys);
                for (State newState : newStates) {
                    State2 newState2 = new State2(state, state.pos);
                    newState2.pos.set(i, newState.pos);
                    newState2.steps += newState.steps;
                    newState2.keys.addAll(newState.keys);
                    if (newState2.keys.size() == numKeys) {
                        if (newState2.steps < best) {
                            best = newState2.steps;
                        }
                    } else {
                        que.add(newState2);
                    }
                }
            }
        }
        return best;
    }

    private static List<State> search(char[][] board, Pos pos, Set<Character> keys) {
        List<State> possible = new ArrayList<>();
        Set<State> seen = new HashSet<>();
        Deque<State> que = new LinkedList<>();
        que.add(new State(pos, keys));
        while (!que.isEmpty()) {
            var state = que.pollFirst();
            if (!seen.add(state)) {
                continue;
            }
            char c = board[state.pos.y][state.pos.x];
            if (c >= 'a' && c <= 'z' && !state.keys.contains(c)) {
                state.keys.add(c);
                possible.add(state);
                continue;
            }
            for (Direction dir : Direction.values()) {
                Pos newPos = state.pos.move(dir);
                char cc = board[newPos.y][newPos.x];
                if (cc != '#' && (cc < 'A' || cc > 'Z' || state.keys.contains((char)(cc-'A'+'a')))) {
                    que.add(new State(state, newPos));
                }
            }
        }
        return possible;
    }

    private static int search(char[][] board, Pos pos, int numKeys) {
        Set<State> seen = new HashSet<>();
        Deque<State> que = new LinkedList<>();
        que.add(new State(pos));
        while (!que.isEmpty()) {
            var state = que.pollFirst();
            if (!seen.add(state)) {
                continue;
            }
            char c = board[state.pos.y][state.pos.x];
            if (c >= 'a' && c <= 'z') {
                state.keys.add(c);
                if (state.keys.size() == numKeys) {
                    return state.steps;
                }
            }
            for (Direction dir : Direction.values()) {
                Pos newPos = state.pos.move(dir);
                char cc = board[newPos.y][newPos.x];
                if (cc != '#' && (cc < 'A' || cc > 'Z' || state.keys.contains((char)(cc-'A'+'a')))) {
                    que.add(new State(state, newPos));
                }
            }
        }
        throw new RuntimeException("Not all keys found");
    }

    private static int countKeys(char[][] board) {
        int count = 0;
        for (char[] chars : board) {
            for (char aChar : chars) {
                if (aChar >= 'a' && aChar <= 'z') {
                    ++count;
                }
            }
        }
        return count;
    }

    private static Pos findStart(char[][] board) {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] == '@') {
                    return new Pos(j, i);
                }
            }
        }
        throw new RuntimeException("Player not found");
    }

    private static class State {
        Pos pos;
        Set<Character> keys;
        int steps;

        public State(Pos pos) {
            this.pos = pos;
            this.keys = new HashSet<>();
            steps = 0;
        }

        public State(State state, Pos pos) {
            this.pos = pos;
            this.keys = new HashSet<>(state.keys);
            steps = state.steps + 1;
        }

        public State(Pos pos, Set<Character> keys) {
            this.pos = pos;
            this.keys = new HashSet<>(keys);
            steps = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Objects.equals(pos, state.pos) && Objects.equals(keys, state.keys);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, keys);
        }

        @Override
        public String toString() {
            return "State{" +
                    "pos=" + pos +
                    ", keys=" + keys +
                    ", steps=" + steps +
                    '}';
        }
    }

    private static class State2 {
        List<Pos> pos;
        Set<Character> keys;
        int steps;

        public State2(List<Pos> pos) {
            this.pos = pos;
            this.keys = new HashSet<>();
            steps = 0;
        }

        public State2(State2 state, List<Pos> pos) {
            this.pos = new ArrayList<>(pos);
            this.keys = new HashSet<>(state.keys);
            steps = state.steps;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State2 state = (State2) o;
            return Objects.equals(pos, state.pos) && Objects.equals(keys, state.keys) && steps == state.steps;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, keys);
        }

        @Override
        public String toString() {
            return "State2{" +
                    "pos=" + pos +
                    ", keys=" + keys +
                    ", steps=" + steps +
                    '}';
        }
    }
}
