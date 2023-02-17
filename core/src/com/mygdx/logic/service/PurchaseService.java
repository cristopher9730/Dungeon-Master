package com.mygdx.logic.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mygdx.logic.model.user.Purchase;
import com.mygdx.logic.repository.factory.RepositoryFactory;

public class PurchaseService extends Service{
    public PurchaseService() {
        this.repositoryFactory = new RepositoryFactory();
    }

    @Override
    public void save (Object object) throws Exception {
        Purchase purchase=(Purchase) object;
        int newId = new JsonParser().parse(repositoryFactory.get("PURCHASES").get()).getAsJsonArray().size()+1;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("itemUserId",(newId));
        jsonObject.addProperty("userId",purchase.getUserId());
        jsonObject.addProperty("itemId",purchase.getItemId());
        repositoryFactory.get("PURCHASES").save(jsonObject);
    }
}