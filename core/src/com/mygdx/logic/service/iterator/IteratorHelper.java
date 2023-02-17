package com.mygdx.logic.service.iterator;

import com.google.gson.JsonArray;
import com.mygdx.logic.service.iterator.Iterator;
import com.mygdx.logic.service.iterator.JsonArrayIterator;
import com.mygdx.logic.service.iterator.ObjectIterator;

import java.util.List;

public abstract class IteratorHelper {
    protected Iterator iterator;
    protected Iterator jsonIterator;
    public <T> Iterator getIterator(List<T> array) {
        return new ObjectIterator(array);
    }
    public Iterator getJsonIterator(JsonArray jsonArray) {
        return new JsonArrayIterator(jsonArray);
    }
}
