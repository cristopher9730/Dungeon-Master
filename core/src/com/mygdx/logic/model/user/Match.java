package com.mygdx.logic.model.user;

import com.google.gson.JsonObject;
import com.mygdx.logic.model.builder.builderObjects.MatchBuilder;
import com.mygdx.logic.model.mapElements.Map;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Match {
    private int matchId;
    private Map map;
    private LocalDateTime dateTime;
    private JsonObject currentRoom;
    private User user;

    public Match(MatchBuilder matchBuilder) {
        this.matchId = matchBuilder.getMatchId();
        this.map = matchBuilder.getMap();
        this.dateTime = matchBuilder.getDateTime();
        this.currentRoom = matchBuilder.getCurrentRoom();
        this.user = matchBuilder.getUser();
    }
}
