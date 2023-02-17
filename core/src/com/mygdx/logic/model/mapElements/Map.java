package com.mygdx.logic.model.mapElements;

import com.google.gson.JsonArray;
import com.mygdx.logic.model.builder.builderObjects.MapBuilder;
import com.mygdx.logic.model.user.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Map {

    private int mapId;
    private User creator;
    private String name;
    private JsonArray rooms;
    private LocalDate creationDate;

    public Map(MapBuilder mapBuilder){
        this.mapId = mapBuilder.getMapId();
        this.creator = mapBuilder.getCreator();
        this.name = mapBuilder.getName();
        this.rooms = mapBuilder.getRooms();
        this.creationDate = mapBuilder.getCreationDate();
    }
}
