package com.mygdx.logic.service.iterator;

import com.google.gson.JsonObject;

public interface Iterator {
    boolean hasNext();
    int getIndex();
    Object next() throws Exception;
    JsonObject nextJson() throws Exception;
}
