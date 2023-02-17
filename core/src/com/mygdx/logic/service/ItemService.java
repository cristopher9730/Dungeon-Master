package com.mygdx.logic.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mygdx.logic.model.builder.Director;
import com.mygdx.logic.model.builder.builderObjects.ItemBuilder;
import com.mygdx.logic.repository.factory.RepositoryFactory;
import com.mygdx.logic.model.user.Item;
import com.mygdx.logic.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class ItemService extends Service {

    public ItemService(){
        this.repositoryFactory = new RepositoryFactory();
        this.director = new Director();
    }

    public List<Item> getItemsByUser(int userId) throws Exception {
        ItemRepository itemRepository = (ItemRepository) repositoryFactory.get("ITEMS");
        JsonArray relationArray = new JsonParser().parse(itemRepository.getItemUserRelations()).getAsJsonArray();
        List<Item> itemArray = get();
        List<Item> filteredItems = new ArrayList<>();
        this.iterator = getIterator(itemArray);
        this.jsonIterator = getJsonIterator(relationArray);

        while(iterator.hasNext()){
            Item item = (Item) iterator.next();
            while (jsonIterator.hasNext()){
                JsonObject jsonObject = jsonIterator.nextJson();
                if (jsonObject.get("userId").getAsInt() == userId && jsonObject.get("itemId").getAsInt() == item.getItemId()){
                    filteredItems.add(item);
                }
            }
        }

        return filteredItems;
    }

    @Override
    public List<Item> get() throws Exception {
        List<Item> items = new ArrayList<>();
        JsonArray itemArray = new JsonParser().parse(repositoryFactory.get("ITEMS").get()).getAsJsonArray();
        this.jsonIterator = getJsonIterator(itemArray);

        while(jsonIterator.hasNext()){
            JsonObject jsonObject = jsonIterator.nextJson();
            ItemBuilder itemBuilder = new ItemBuilder()
                    .setItemId(jsonObject.get("itemId").getAsInt())
                    .setName(jsonObject.get("name").getAsString())
                    .setStoreImage(jsonObject.get("image").getAsString())
                    .setPrice(jsonObject.get("price").getAsInt());
            items.add((Item) director.buildObject(itemBuilder));
        }

        return items;
    }
}
