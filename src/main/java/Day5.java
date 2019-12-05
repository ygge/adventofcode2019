import java.io.IOException;

public class Day5 {

    public static void main(String[] args) throws IOException {
        final IntComputer computer = new IntComputer(Util.readStrings().get(0));

        // part 1
        computer.run(1);

        // part 2
        computer.reset();
        computer.run(5);
    }
}
