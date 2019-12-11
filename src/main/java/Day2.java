public class Day2 {

    public static void main(String[] args) {
        final IntComputer computer = new IntComputer(Util.readString());

        computer.set(1, 12);
        computer.set(2, 2);
        computer.run();
        System.out.println("part 1: " + computer.get(0));

        boolean done = false;
        for (int noun = 0; !done && noun < 100; ++noun) {
            for (int verb = 0; verb < 100; ++verb) {
                computer.reset();
                computer.set(1, noun);
                computer.set(2, verb);
                computer.run();
                if (computer.get(0) == 19690720) {
                    System.out.println(String.format("part 2: %d %d = %d", noun, verb, 100*noun+verb));
                    done = true;
                    break;
                }
            }
        }
    }
}
