package com.mygdx.logic.repository;

import com.google.gson.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserRepository implements Repository {

    @Override
    public String get(){
        try{
            Path path = Paths.get("./core/src/com/mygdx/logic/data/users.json");
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public void save(JsonObject jsonObject){
        try{
            JsonArray jsonArray = new JsonParser().parse(get()).getAsJsonArray();
            jsonArray.add(jsonObject);
            String jsonText = jsonArray.toString();
            Path path = Paths.get("./core/src/com/mygdx/logic/data/users.json");
            byte[] bytes = jsonText.getBytes();
            Files.write(path, bytes);
        }
        catch (Exception d){
            System.out.println(d);
        }
    }

    public void update(JsonObject jsonObject){
        try{
            JsonArray jsonArray = new JsonParser().parse(get()).getAsJsonArray();
            JsonArray filteredJsonArray = new JsonArray();
            jsonArray.forEach(jsonElement -> {
                if(!jsonElement.getAsJsonObject().get("userId").equals(jsonObject.get("userId"))){
                    filteredJsonArray.add(jsonElement);
                }
            });
            filteredJsonArray.add(jsonObject);
            String jsonText = filteredJsonArray.toString();
            Path path = Paths.get("./core/src/com/mygdx/logic/data/users.json");
            byte[] bytes = jsonText.getBytes();
            Files.write(path, bytes);
        }
        catch (Exception d){
            d.printStackTrace();
        }
    }

}
