package com.mygdx.logic.repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ItemRepository implements Repository {

    @Override
    public String get(){
        try{
            Path path = Paths.get("./core/src/com/mygdx/logic/data/items.json");
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void save(JsonObject jsonObject) throws Exception {
        throw new Exception("Method not supported for items");
    }

    public String getItemUserRelations(){
        try{
            Path path = Paths.get("./core/src/com/mygdx/logic/data/users-items.json");
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
