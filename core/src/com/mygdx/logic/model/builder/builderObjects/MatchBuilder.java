package com.mygdx.logic.model.builder.builderObjects;

import com.google.gson.JsonObject;
import com.mygdx.logic.model.builder.Builder;
import com.mygdx.logic.model.mapElements.Map;
import com.mygdx.logic.model.user.Match;
import com.mygdx.logic.model.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchBuilder implements Builder {

    private Match match;
    private int matchId;
    private Map map;
    private LocalDateTime dateTime;
    private JsonObject currentRoom;
    private User user;

    public MatchBuilder() {
        this.match = new Match(this);
    }

    public MatchBuilder setMatchId(int matchId) {
        this.match.setMatchId(matchId);
        return this;
    }

    public MatchBuilder setMap(Map map) {
        this.match.setMap(map);
        return this;
    }

    public MatchBuilder setDateTime(LocalDateTime dateTime) {
        this.match.setDateTime(dateTime);
        return this;
    }

    public MatchBuilder setCurrentRoom(JsonObject currentRoom) {
        this.match.setCurrentRoom(currentRoom);
        return this;
    }

    public MatchBuilder setUser(User user) {
        this.match.setUser(user);
        return this;
    }

    @Override
    public Object build() {
        return this.match;
    }
}
