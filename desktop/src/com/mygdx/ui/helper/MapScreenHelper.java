package com.mygdx.ui.helper;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mygdx.logic.model.mapElements.Map;
import com.mygdx.logic.model.mapElements.UiRoom;
import com.mygdx.logic.service.facade.ServiceStore;
import com.mygdx.logic.service.iterator.IteratorHelper;
import com.mygdx.ui.Boot;

import java.util.ArrayList;
import java.util.List;

public class MapScreenHelper extends IteratorHelper {
    public java.util.List<UiRoom> getSelectableRooms(Stage stage, java.util.List<UiRoom> roomsToStore, Boot boot) {
        java.util.List<UiRoom> rooms = new ArrayList<>();
        final Texture texture1 = new Texture("map/rooms/roomOneEntrance.png");
        final UiRoom image1 = new UiRoom(texture1,stage, roomsToStore, false, true, false, false, boot.getLoggedUser());
        image1.setBounds(650,250,50,50);

        final Texture texture2 = new Texture("map/rooms/roomFrontDoors.png");
        final UiRoom image2 = new UiRoom(texture2,stage, roomsToStore, true, true, false, false, boot.getLoggedUser());
        image2.setBounds(750,250,50,50);

        final Texture texture3 = new Texture("map/rooms/roomTwoEntrances.png");
        final UiRoom image3 = new UiRoom(texture3,stage, roomsToStore, false, true, true, false, boot.getLoggedUser());
        image3.setBounds(850,250,50,50);

        final Texture texture4 = new Texture("map/rooms/roomFourEntrances.png");
        final UiRoom image4 = new UiRoom(texture4,stage, roomsToStore, true, true, true, true, boot.getLoggedUser());
        image4.setBounds(700,150,50,50);

        final Texture texture5 = new Texture("map/rooms/roomThreeEntrances.png");
        final UiRoom image5 = new UiRoom(texture5,stage, roomsToStore, false, true, true, true, boot.getLoggedUser());
        image5.setBounds(800,150,50,50);

        rooms.add(image1);
        rooms.add(image2);
        rooms.add(image3);
        rooms.add(image4);
        rooms.add(image5);

        return rooms;
    }

    public boolean roomValidation(JsonArray roomsToStore, String mapName){
        try{
            return validateRoomAmount(roomsToStore)
                    && validateFinalBoss(roomsToStore)
                    && validateDisconnectedRooms(roomsToStore)
                    && validateStartAndEndRooms(roomsToStore)
                    && validateName(mapName);
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public void validateAndSave(Object object, Stage stage, String mapName) throws Exception {
        Map map = (Map) object;
        if(roomValidation(map.getRooms(),mapName)){
            ServiceStore.getInstance().saveMap(map);
            return;
        }

        Skin skin = new Skin(new FileHandle("./assets/skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));
        Dialog dialog = new Dialog("Warning",skin,"dialog");
        TextButton textButton = new TextButton("Ok",skin);
        Table table = new Table(skin);
        if(!validateRoomAmount(map.getRooms())){
            table.add(new Label("The map must have between 5 and 20 rooms", skin));
            table.row();
        }
        if(!validateStartAndEndRooms(map.getRooms())){
            table.add(new Label("Make sure start and end rooms are set", skin));
            table.row();
        }
        if(!validateFinalBoss(map.getRooms())){
            table.add(new Label("A final boss is required", skin));
            table.row();
        }
        if(!validateDisconnectedRooms(map.getRooms())){
            table.add(new Label("There must not be disconnected rooms", skin));
            table.row();
        }
        if(!validateName(mapName)){
            table.add(new Label("The map must have a name", skin));
            table.row();
        }
        table.add(textButton);
        dialog.add(table);
        textButton.addListener(new ClickListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dialog.remove();
                return true;
            }
        });
        dialog.show(stage);
    }

    public JsonArray getStorableRooms(List<UiRoom> rooms){
        JsonArray jsonArray = new JsonArray();
        rooms.forEach(roomActor -> jsonArray.add(new JsonParser().parse(roomActor.toString())));
        return jsonArray;
    }
    
    private boolean validateName(String mapName){
        return !mapName.isEmpty();
    }

    private boolean validateRoomAmount(JsonArray rooms){
        return rooms.size() >= 5 && rooms.size() <= 20;
    }

    private boolean validateStartAndEndRooms(JsonArray rooms) throws Exception {
        this.iterator = getJsonIterator(rooms);
        boolean hasStartRoom = false;
        boolean hasEndRoom = false;
        while (iterator.hasNext()){
            JsonObject jsonObject = iterator.nextJson();
            if(jsonObject.get("isStartRoom").getAsBoolean()){
                hasStartRoom = true;
            }
            else if(jsonObject.get("isEndRoom").getAsBoolean()){
                hasEndRoom = true;
            }
        }
        return hasStartRoom && hasEndRoom;
    }

    private boolean validateFinalBoss(JsonArray rooms) throws Exception {
        this.iterator = getJsonIterator(rooms);
        while (iterator.hasNext()){
            JsonObject jsonObject = iterator.nextJson();
            if(jsonObject.get("hasFinalBoss").getAsBoolean()){
                 return true;
            }
        }
        return false;
    }

    private boolean validateDisconnectedRooms(JsonArray rooms) throws Exception {
        boolean noDisconnectedRooms = true;
        this.iterator = getJsonIterator(rooms);
        while (iterator.hasNext()) {
            JsonObject jsonObject = iterator.nextJson();
            if (jsonObject.get("color").getAsString().equals("ff0000ff")) {
                noDisconnectedRooms = false;
                break;
            }
        }
        return noDisconnectedRooms;
    }
}
