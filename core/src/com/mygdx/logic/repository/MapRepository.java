package com.mygdx.logic.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapRepository implements Repository {

    @Override
    public String get() {
        try{
            Path path = Paths.get("./core/src/com/mygdx/logic/data/maps.json");
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
    @Override
    public void save(JsonObject jsonObject){
        try{
            JsonArray jsonArray = new JsonParser().parse(get()).getAsJsonArray();
            jsonArray.add(jsonObject);
            String jsonText = jsonArray.toString();
            Path path = Paths.get("./core/src/com/mygdx/logic/data/maps.json");
            byte[] bytes = jsonText.getBytes();
            Files.write(path, bytes);
        }
        catch (Exception d){
            System.out.println(d);
        }
    }
}
