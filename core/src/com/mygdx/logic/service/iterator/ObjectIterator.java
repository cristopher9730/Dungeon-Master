package com.mygdx.logic.service.iterator;

import com.google.gson.JsonObject;
import lombok.Data;

import java.util.List;

public class ObjectIterator implements Iterator{

    private List<Object> objectList;

    private int index;

    public <T> ObjectIterator(List<T> objectList) {
        this.objectList = (List<Object>) objectList;
    }

    @Override
    public boolean hasNext() {
        if(index < objectList.size()){
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
    public Object next() {
        if(this.hasNext()){
            return objectList.get(index++);
        }
        return new Object();
    }

    @Override
    public JsonObject nextJson() throws Exception {
        throw new Exception("Method not allowed");
    }
}
