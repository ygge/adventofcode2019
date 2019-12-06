import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class Util {

    private Util() {}

    static List<String> readStrings() {
        return readFile(Function.identity());
    }

    static List<Integer> readInts() {
        return readFile(Integer::parseInt);
    }

    private static <T> List<T> readFile(Function<String, T> converter) {
        final String className = getCallerClassName();
        final String fileName = String.format("/%s.in", className.substring(3));
        final BufferedReader in = new BufferedReader(new InputStreamReader(Util.class.getResourceAsStream(fileName)));
        final List<T> input = new ArrayList<>();
        String row;
        try {
            while ((row = in.readLine()) != null) {
                input.add(converter.apply(row));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    private static String getCallerClassName() {
        for (StackTraceElement stackTraceElement : new RuntimeException().getStackTrace()) {
            if (!stackTraceElement.getClassName().equals("Util")) {
                return stackTraceElement.getClassName();
            }
        }
        return "";
    }
}
