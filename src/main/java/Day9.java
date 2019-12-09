import java.util.List;

public class Day9 {

    public static void main(String[] args) {
        final String code = Util.readStrings().get(0);

        final IntComputer program = new IntComputer(code);
        final List<Long> p1 = program.run(1L);
        System.out.println(p1);

        program.reset();
        final List<Long> p2 = program.run(2L);
        System.out.println(p2);
    }
}
