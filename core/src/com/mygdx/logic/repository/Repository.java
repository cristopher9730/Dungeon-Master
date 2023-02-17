package com.mygdx.logic.repository;

import com.google.gson.JsonObject;

public interface Repository {
    String get();
    void save(JsonObject jsonObject) throws Exception;
}
