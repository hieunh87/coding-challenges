package com.elsa.learning.leaderboard.repository;

import com.elsa.learning.leaderboard.entity.Leaderboard;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LeaderboardRepository implements PanacheRepository<Leaderboard> {
    public void updateScore(Leaderboard leaderboard) {
        // Find the existing leaderboard entry or create a new one if it doesn't exist
        Leaderboard existedLeaderboard = find("roomUuid = ?1 and userUuid = ?2", leaderboard.getRoomUuid(), leaderboard.getUserUuid())
                .firstResult();
        if (existedLeaderboard != null) {
            existedLeaderboard.setScore(leaderboard.getScore());
            persist(existedLeaderboard);
        } else {
            persist(leaderboard);
        }
    }

    public List<Leaderboard> getLeaderboardsByRoomUuid(String roomUuid) {
        return list("roomUuid ?1 order by score desc", roomUuid);
    }

}
