package leaderboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Programmer {

    private final MemberModel member;
    final List<Integer> pointsByPart = new ArrayList<>();
    final List<Integer> solvedSecondsByPart = new ArrayList<>();
    int score = 0;

    Programmer(MemberModel member) {
        this.member = member;
    }

    Optional<String> getTimeString(String day, String part) {
        return Optional.ofNullable(member.completion_day_level.get(day))
                .flatMap(partMap -> Optional.ofNullable(partMap.get(part)))
                .map(partValue -> partValue.get_star_ts);
    }

    String getName() {
        if (this.member.name != null) {
            return this.member.name;
        }
        return String.format("anon %s", member.id);
    }
}
