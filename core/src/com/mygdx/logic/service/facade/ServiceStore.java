package com.mygdx.logic.service.facade;

import com.mygdx.logic.model.mapElements.Map;
import com.mygdx.logic.model.user.Item;
import com.mygdx.logic.model.user.Match;
import com.mygdx.logic.model.user.User;
import com.mygdx.logic.service.*;


import java.util.List;

public class ServiceStore {
    private Service items;
    private Service users;
    private Service maps;
    private Service challenges;
    private Service purchases;
    private Service matches;

    private static ServiceStore INSTANCE = null;

    public static ServiceStore getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new ServiceStore();

        return INSTANCE;
    }

    private ServiceStore(){
        this.items = new ItemService();
        this.users = new UserService();
        this.maps = new MapService();
        this.challenges = new ChallengeService();
        this.purchases = new PurchaseService();
        this.matches = new MatchService();
    }

    public List<Item> getItems() throws Exception {
        return items.get();
    }

    public List<Map> getMaps() throws Exception {
        return maps.get();
    }

    public List<User> getUsers() throws Exception {
        return users.get();
    }

    public List<Map> getChallenges() throws Exception {
        return challenges.get();
    }

    public void saveUser(Object object) throws Exception {
        users.save(object);
    }

    public void saveMap(Object object) throws Exception {
        maps.save(object);
    }

    public void savePurchase(Object object) throws Exception{
        purchases.save(object);
    }

    public List<Item> getItemsByUser(int userId) throws Exception{
        ItemService itemService = (ItemService) items;
        return itemService.getItemsByUser(userId);
    }

    public void saveMatch(Object object) throws Exception {
        matches.save(object);
    }

    public List<Match> getMatches() throws Exception {
        return matches.get();
    }

    public List<Match> getChallengeMatches() throws Exception{
        MatchService matchService = (MatchService) matches;
        return matchService.getChallengeMatches();
    }
}
