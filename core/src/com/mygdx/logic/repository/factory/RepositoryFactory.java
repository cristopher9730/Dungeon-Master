package com.mygdx.logic.repository.factory;

import com.mygdx.logic.repository.*;

public class RepositoryFactory implements Factory {

    public Repository get(String param){
        if(param == null){
            return null;
        }
        if(param.equalsIgnoreCase("USERS")){
            return new UserRepository();

        } else if(param.equalsIgnoreCase("ITEMS")){
            return new ItemRepository();

        } else if(param.equalsIgnoreCase("MAPS")){
            return new MapRepository();
        }
        else if(param.equalsIgnoreCase("CHALLENGES")){
            return new ChallengeRepository();

        }else if(param.equalsIgnoreCase("PURCHASES")){
            return new PurchaseRepository();

        }else if(param.equalsIgnoreCase("MATCH")){
            return new MatchRepository();
        }
        return null;
    }
}
