package com.mygdx.logic.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mygdx.logic.model.builder.Director;
import com.mygdx.logic.model.builder.builderObjects.MatchBuilder;
import com.mygdx.logic.model.user.Match;
import com.mygdx.logic.repository.MatchRepository;
import com.mygdx.logic.repository.factory.RepositoryFactory;
import com.mygdx.logic.service.facade.ServiceStore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MatchService extends Service{
    public MatchService(){
        this.repositoryFactory = new RepositoryFactory();
        this.director = new Director();
    }
    @Override
    public void save(Object object) throws Exception {
        MatchRepository matchRepository = (MatchRepository) repositoryFactory.get("MATCH");
        Match match = (Match) object;
        JsonObject jsonObject = new JsonObject();
        if(match.getMatchId() == 0){
            int newId =  new JsonParser().parse(repositoryFactory.get("MATCH").get()).getAsJsonArray().size()+1;
            jsonObject.addProperty("matchId", (newId));
            jsonObject.addProperty("map", match.getMap().getMapId());
            jsonObject.addProperty("dateTime", LocalDateTime.now().toString());
            jsonObject.add("currentRoom",match.getCurrentRoom());
            jsonObject.addProperty("userId",match.getUser().getUserId());
            matchRepository.save(jsonObject);
            return;
        }
        jsonObject.addProperty("matchId", match.getMatchId());
        jsonObject.addProperty("map", match.getMap().getMapId());
        jsonObject.addProperty("dateTime", match.getDateTime().toString());
        jsonObject.add("currentRoom",match.getCurrentRoom());
        jsonObject.addProperty("userId",match.getUser().getUserId());
        matchRepository.update(jsonObject);
    }

    @Override
    public List<Match> get() throws Exception {
        List<Match> matches = new ArrayList<>();
        JsonArray matchArray = new JsonParser().parse(repositoryFactory.get("MATCH").get()).getAsJsonArray();

        this.jsonIterator = getJsonIterator(matchArray);

        while(jsonIterator.hasNext()){
            JsonObject jsonObject = jsonIterator.nextJson();
            MatchBuilder matchBuilder = new MatchBuilder().setMatchId(jsonObject.get("matchId").getAsInt())
                    .setMap(ServiceStore.getInstance().getMaps().stream().filter(map -> map.getMapId() == jsonObject.get("map").getAsInt()).findFirst().orElse(null))
                    .setDateTime(LocalDateTime.parse(jsonObject.get("dateTime").getAsString()))
                    .setCurrentRoom(jsonObject.get("currentRoom").getAsJsonObject())
                    .setUser(ServiceStore.getInstance().getUsers().stream().filter(user -> user.getUserId() == jsonObject.get("userId").getAsInt()).findFirst().orElse(null));
            matches.add((Match)director.buildObject(matchBuilder));
        }
        return matches;
    }

    public List<Match> getChallengeMatches() throws Exception {
        List<Match> matches = new ArrayList<>();
        JsonArray matchArray = new JsonParser().parse(repositoryFactory.get("MATCH").get()).getAsJsonArray();

        this.jsonIterator = getJsonIterator(matchArray);

        while(jsonIterator.hasNext()){
            JsonObject jsonObject = jsonIterator.nextJson();
            MatchBuilder matchBuilder = new MatchBuilder().setMatchId(jsonObject.get("matchId").getAsInt())
                    .setMap(ServiceStore.getInstance().getChallenges().stream().filter(map -> map.getMapId() == jsonObject.get("map").getAsInt()).findFirst().orElse(null))
                    .setDateTime(LocalDateTime.parse(jsonObject.get("dateTime").getAsString()))
                    .setCurrentRoom(jsonObject.get("currentRoom").getAsJsonObject())
                    .setUser(ServiceStore.getInstance().getUsers().stream().filter(user -> user.getUserId() == jsonObject.get("userId").getAsInt()).findFirst().get());
            matches.add((Match)director.buildObject(matchBuilder));
        }
        return matches;
    }

}
