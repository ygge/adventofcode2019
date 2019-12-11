public class Day5 {

    public static void main(String[] args) {
        final IntComputer computer = new IntComputer(Util.readString());

        // part 1
        computer.run(1L);

        // part 2
        computer.reset();
        computer.run(5L);
    }
}
