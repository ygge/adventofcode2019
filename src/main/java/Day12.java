import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day12 {

    public static void main(String[] args) {
        List<Moon> moons = initialMoons();
        for (int i = 0; i < 1000; ++i) {
            moons = simulate(moons);
        }
        int sum = 0;
        for (Moon moon : moons) {
            sum += moon.energy();
        }
        System.out.println("part 1: " + sum);

        final List<Moon> p2 = initialMoons();
        long stepsX = repeat(p2.stream().map(m -> new CoordAndVel(m.pos.x, m.vel.x)).collect(Collectors.toList()));
        long stepsY = repeat(p2.stream().map(m -> new CoordAndVel(m.pos.y, m.vel.y)).collect(Collectors.toList()));
        long stepsZ = repeat(p2.stream().map(m -> new CoordAndVel(m.pos.z, m.vel.z)).collect(Collectors.toList()));
        long res = stepsX;
        while (res%stepsY != 0 || res%stepsZ != 0) {
            res += stepsX;
        }
        System.out.println("part 2: " + res);
    }

    private static long repeat(List<CoordAndVel> moons) {
        final Set<List<CoordAndVel>> seen = new HashSet<>();
        List<CoordAndVel> list = moons;
        long steps = 0;
        while (seen.add(list)) {
            list = simulateCoord(list);
            ++steps;
        }
        return steps;
    }

    private static List<CoordAndVel> simulateCoord(List<CoordAndVel> list) {
        final List<CoordAndVel> newMoons = list.stream()
                .map(CoordAndVel::copy)
                .collect(Collectors.toList());
        for (int i = 0; i < newMoons.size(); ++i) {
            for (int j = i + 1; j < newMoons.size(); ++j) {
                updateVelocity(newMoons.get(i), newMoons.get(j));
            }
        }
        newMoons.forEach(CoordAndVel::updatePos);
        return newMoons;
    }

    private static void updateVelocity(CoordAndVel m1, CoordAndVel m2) {
        if (m1.c > m2.c) {
            --m1.v;
            ++m2.v;
        } else if (m1.c < m2.c) {
            ++m1.v;
            --m2.v;
        }
    }

    private static List<Moon> initialMoons() {
        return Arrays.asList(
                new Moon(new Coord(13, 9, 5)),
                new Moon(new Coord(8, 14, -2)),
                new Moon(new Coord(-5, 4, 11)),
                new Moon(new Coord(2, -6, 1))
        );
    }

    private static List<Moon> simulate(List<Moon> moons) {
        final List<Moon> newMoons = moons.stream()
                .map(Moon::copy)
                .collect(Collectors.toList());
        for (int i = 0; i < newMoons.size(); ++i) {
            for (int j = i + 1; j < newMoons.size(); ++j) {
                updateVelocity(newMoons.get(i), newMoons.get(j));
            }
        }
        newMoons.forEach(Moon::updatePos);
        return newMoons;
    }

    private static void updateVelocity(Moon m1, Moon m2) {
        if (m1.pos.x > m2.pos.x) {
            --m1.vel.x;
            ++m2.vel.x;
        } else if (m1.pos.x < m2.pos.x) {
            ++m1.vel.x;
            --m2.vel.x;
        }

        if (m1.pos.y > m2.pos.y) {
            --m1.vel.y;
            ++m2.vel.y;
        } else if (m1.pos.y < m2.pos.y) {
            ++m1.vel.y;
            --m2.vel.y;
        }

        if (m1.pos.z > m2.pos.z) {
            --m1.vel.z;
            ++m2.vel.z;
        } else if (m1.pos.z < m2.pos.z) {
            ++m1.vel.z;
            --m2.vel.z;
        }
    }

    private static final class Moon {
        private Coord pos, vel;

        private Moon(Coord pos) {
            this.pos = pos;
            this.vel = new Coord(0, 0, 0);
        }

        private Moon(Coord pos, Coord vel) {
            this.pos = pos;
            this.vel = vel;
        }

        void updatePos() {
            pos.x += vel.x;
            pos.y += vel.y;
            pos.z += vel.z;
        }

        int energy() {
            return pos.energy() * vel.energy();
        }

        Moon copy() {
            return new Moon(pos.copy(), vel.copy());
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Moon moon = (Moon) o;
            return Objects.equals(pos, moon.pos) &&
                    Objects.equals(vel, moon.vel);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, vel);
        }

        @Override
        public String toString() {
            return "Moon{" +
                    "pos=" + pos +
                    ", vel=" + vel +
                    '}';
        }
    }

    private static final class Coord {
        private int x, y, z;

        private Coord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        int energy() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        Coord copy() {
            return new Coord(x, y, z);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return x == coord.x &&
                    y == coord.y &&
                    z == coord.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "Coord{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    private static final class CoordAndVel {
        private int c, v;

        private CoordAndVel(int c, int v) {
            this.c = c;
            this.v = v;
        }

        private CoordAndVel copy() {
            return new CoordAndVel(c, v);
        }

        void updatePos() {
            c += v;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CoordAndVel that = (CoordAndVel) o;
            return c == that.c &&
                    v == that.v;
        }

        @Override
        public int hashCode() {
            return Objects.hash(c, v);
        }
    }
}
