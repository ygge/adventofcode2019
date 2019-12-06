package leaderboard;

import java.util.Map;

public class MemberModel {

    public String id, name, last_star_ts;
    public int local_score, stars, global_score;
    public Map<String, Map<String, DayStarTimeModel>> completion_day_level;
}
