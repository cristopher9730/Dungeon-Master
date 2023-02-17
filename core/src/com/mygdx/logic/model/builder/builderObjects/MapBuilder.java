package com.mygdx.logic.model.builder.builderObjects;

import com.google.gson.JsonArray;
import com.mygdx.logic.model.builder.Builder;
import com.mygdx.logic.model.mapElements.Map;
import com.mygdx.logic.model.user.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MapBuilder implements Builder {

    private Map map;
    private int mapId;
    private User creator;
    private String name;
    private JsonArray rooms;
    private LocalDate creationDate;

    public MapBuilder(){
        this.map = new Map(this);
    }

    public MapBuilder setMapId(int mapId) {
        this.map.setMapId(mapId);
        return this;
    }

    public MapBuilder setCreator(User creator) {
        this.map.setCreator(creator);
        return this;
    }

    public MapBuilder setName(String name) {
        this.map.setName(name);
        return this;
    }

    public MapBuilder setRooms(JsonArray rooms) {
        this.map.setRooms(rooms);
        return this;
    }

    public MapBuilder setCreationDate(LocalDate creationDate) {
        this.map.setCreationDate(creationDate);
        return this;
    }
    public Map build(){
        return this.map;
    }

}
