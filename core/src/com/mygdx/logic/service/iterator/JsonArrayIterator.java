package com.mygdx.logic.service.iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonArrayIterator implements Iterator{

    private JsonArray jsonArray;

    private int index;

    public JsonArrayIterator(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public boolean hasNext() {
        if(index < jsonArray.size()){
            return true;
        }
        index = 0;
        return false;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Object next() throws Exception {
        throw new Exception("Method not allowed");
    }

    @Override
    public JsonObject nextJson() {
        if(this.hasNext()){
            return jsonArray.get(index++).getAsJsonObject();
        }
        return new JsonObject();
    }
}
