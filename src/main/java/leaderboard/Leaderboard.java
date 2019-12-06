package leaderboard;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Leaderboard {

    public static void main(String[] args) throws FileNotFoundException {
        final String file = Leaderboard.class.getResource("/kodsnack.json").getFile();
        final LeaderboardModel main = new Gson().fromJson(new FileReader(file), LeaderboardModel.class);

        final List<Programmer> programmers = main.members.values().stream()
                .map(Programmer::new)
                .collect(Collectors.toList());

        int maxDays = 0;
        for (int day = 1; day <= 25; ++day) {
            for (int part = 1; part <= 2; ++part) {
                final String dayString = Integer.toString(day);
                final String partString = Integer.toString(part);
                sortByDayAndStar(programmers, dayString, partString);
                int score = programmers.size();
                for (Programmer programmer : programmers) {
                    if (programmer.getTime(dayString, partString).isPresent()) {
                        maxDays = day;
                    } else {
                        score = 0;
                    }
                    programmer.pointsByPart.add(score);
                    programmer.score += score;
                    if (score > 0) {
                        --score;
                    }
                }
            }
        }

        programmers.sort((p1, p2) -> p2.score - p1.score);
        System.out.print("Name                         ");
        for (int day = 1; day <= maxDays; ++day) {
            System.out.printf("  Day %2d  ", day);
        }
        System.out.println(" Totalt");
        for (Programmer programmer : programmers) {
            System.out.print(programmer.getName());
            System.out.print("                             ".substring(programmer.getName().length()));
            for (int i = 0; i < 2*maxDays; ++i) {
                System.out.printf(" %3d ", programmer.pointsByPart.get(i));
            }
            System.out.printf(" %5d\n", programmer.score);
        }
    }

    private static void sortByDayAndStar(List<Programmer> programmers, String day, String part) {
        programmers.sort((p1, p2) -> {
            final Optional<String> v1 = p1.getTime(day, part);
            final Optional<String> v2 = p2.getTime(day, part);
            if (v1.isEmpty() && v2.isEmpty()) {
                return 0;
            } else if (v1.isEmpty()) {
                return 1;
            } else if (v2.isEmpty()) {
                return -1;
            }
            return v1.get().compareTo(v2.get());
        });
    }
}
