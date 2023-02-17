package com.mygdx.logic.service;

import com.google.gson.JsonArray;
import com.mygdx.logic.model.builder.Director;
import com.mygdx.logic.repository.factory.RepositoryFactory;
import com.mygdx.logic.service.iterator.Iterator;
import com.mygdx.logic.service.iterator.IteratorHelper;
import com.mygdx.logic.service.iterator.JsonArrayIterator;
import com.mygdx.logic.service.iterator.ObjectIterator;

import java.util.List;

public abstract class Service extends IteratorHelper {
    protected RepositoryFactory repositoryFactory;

    protected Director director;

    public <T> List<T> get() throws Exception{return null;}
    public void save(Object object) throws Exception{}
}
