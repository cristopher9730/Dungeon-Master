package com.mygdx.logic.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mygdx.logic.model.builder.Director;
import com.mygdx.logic.repository.UserRepository;
import com.mygdx.logic.repository.factory.RepositoryFactory;
import com.mygdx.logic.model.user.User;
import com.mygdx.logic.model.builder.builderObjects.UserBuilder;
import com.mygdx.logic.service.facade.ServiceStore;

import java.util.ArrayList;
import java.util.List;


public class UserService extends Service{
    public UserService(){
        this.director = new Director();
        this.repositoryFactory = new RepositoryFactory();
    }

    @Override
    public List<User> get() throws Exception{

        JsonArray object = new JsonParser().parse(repositoryFactory.get("USERS").get()).getAsJsonArray();
        List<User> userList = new ArrayList<>();
        this.jsonIterator = getJsonIterator(object);
        while (jsonIterator.hasNext()){
            JsonObject jsonObject = jsonIterator.nextJson();
            UserBuilder userBuilder = new UserBuilder()
                    .setUserId(jsonObject.get("userId").getAsInt())
                    .setCoins(jsonObject.get("coins").getAsInt())
                    .setName(jsonObject.get("name").getAsString())
                    .setItems(ServiceStore.getInstance().getItemsByUser(jsonObject.get("userId").getAsInt()));
            userList.add((User)director.buildObject(userBuilder));
        }
        return userList;
    }

    @Override
    public void save(Object object) throws Exception {
        UserRepository userRepository = (UserRepository) repositoryFactory.get("USERS");
        User user = (User) object;
        JsonObject jsonObject = new JsonObject();
        if(user.getUserId() == 0){
            int newId =  new JsonParser().parse(repositoryFactory.get("USERS").get()).getAsJsonArray().size()+1;
            jsonObject.addProperty("userId", (newId));
            jsonObject.addProperty("name", user.getName());
            jsonObject.addProperty("coins", 0);
            userRepository.save(jsonObject);
            return;
        }
        jsonObject.addProperty("userId",user.getUserId());
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("coins", user.getCoins());
        userRepository.update(jsonObject);
    }
}
