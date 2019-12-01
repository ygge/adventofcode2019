import java.io.IOException;
import java.util.List;

public class Day1 {

    public static void main(String[] args) throws IOException {
        List<Integer> masses = Util.readInts();
        int fuelP1 = calculateFuel(masses, false);
        System.out.println("part1: " + fuelP1);
        int fuelP2 = calculateFuel(masses, true);
        System.out.println("part2: " + fuelP2);
    }

    private static int calculateFuel(List<Integer> masses, boolean includeFuelWeight) {
        return masses.stream()
                    .map(m -> calculateMass(m, includeFuelWeight))
                    .reduce(0, Integer::sum);
    }

    private static int calculateMass(int mass, boolean includeFuelWeight) {
        int fuel = mass/3 - 2;
        if (includeFuelWeight) {
            int m = fuel;
            while (true) {
                m = m/3 - 2;
                if (m <= 0) {
                    break;
                }
                fuel += m;
            }
        }
        return fuel;
    }
}
