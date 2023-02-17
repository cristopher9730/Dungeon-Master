package com.mygdx.logic.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mygdx.logic.model.builder.Director;
import com.mygdx.logic.model.builder.builderObjects.MapBuilder;
import com.mygdx.logic.model.mapElements.Map;
import com.mygdx.logic.repository.factory.RepositoryFactory;
import com.mygdx.logic.service.facade.ServiceStore;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MapService extends Service{

    public MapService(){
        this.repositoryFactory = new RepositoryFactory();
        this.director = new Director();
    }

    @Override
    public void save(Object object) throws Exception {
        Map map=(Map) object;
        int newId = new JsonParser().parse(repositoryFactory.get("MAPS").get()).getAsJsonArray().size()+4;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mapId",(newId));
        jsonObject.add("rooms",map.getRooms());
        jsonObject.addProperty("creationDate", LocalDate.now().toString());
        jsonObject.addProperty("name", map.getName());
        jsonObject.addProperty("userId", map.getCreator().getUserId());
        repositoryFactory.get("MAPS").save(jsonObject);
    }

    @Override
    public List<Map> get() throws Exception {
        List<Map> maps = new ArrayList<>();
        JsonArray mapArray = new JsonParser().parse(repositoryFactory.get("MAPS").get()).getAsJsonArray();

        this.jsonIterator = getJsonIterator(mapArray);

        while(jsonIterator.hasNext()){
            JsonObject jsonObject = jsonIterator.nextJson();
            MapBuilder mapBuilder = new MapBuilder().setMapId(jsonObject.get("mapId").getAsInt())
                    .setCreator(ServiceStore.getInstance().getUsers().stream().filter(user -> user.getUserId() == jsonObject.get("userId").getAsInt()).findFirst().get())
                    .setName(jsonObject.get("name").getAsString())
                    .setRooms(jsonObject.get("rooms").getAsJsonArray())
                    .setCreationDate(LocalDate.parse(jsonObject.get("creationDate").getAsString()));
            maps.add((Map)director.buildObject(mapBuilder));
        }
        return maps;
    }

}
