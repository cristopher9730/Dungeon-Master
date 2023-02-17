package com.mygdx.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mygdx.logic.helper.Helper;
import com.mygdx.logic.model.builder.builderObjects.MatchBuilder;
import com.mygdx.logic.model.entity.bridge.Trap;
import com.mygdx.logic.model.entity.enemy.possibleEnemies.Boss;
import com.mygdx.logic.model.entity.enemy.possibleEnemies.FinalBoss;
import com.mygdx.logic.model.builder.builderObjects.ItemBuilder;
import com.mygdx.logic.model.entity.enemy.possibleEnemies.Spawn;
import com.mygdx.logic.model.mapElements.Map;
import com.mygdx.logic.model.entity.enemy.Enemy;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.bridge.Ranged;
import com.mygdx.logic.model.user.Item;
import com.mygdx.logic.model.user.Match;
import com.mygdx.logic.model.user.possibleItems.Chest;
import com.mygdx.logic.model.user.possibleItems.Ending;
import com.mygdx.logic.model.user.possibleItems.Healing;
import com.mygdx.logic.helper.Constants;
import com.mygdx.logic.model.entity.player.Player;
import com.mygdx.logic.service.facade.ServiceStore;
import com.mygdx.ui.Boot;
import com.mygdx.ui.helper.ScreenBodyHelper;


import java.util.ArrayList;
import java.util.List;

public class GameScreen extends CustomScreen {

    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Enemy enemy;
    private Player player;
    private Rectangle rectanglePlayer;
    private Rectangle rectangleEnemy;
    private Rectangle itemRectangle;

    private List<Item> currentMapItems;

    private Stage stage;

    private Map map;
    private double coorX;
    private double coorY;

    private float elapsed;

    private JsonObject currentRoom;

    private Label playerHealth;

    private Label enemyHealth;

    private ScreenBodyHelper screenBodyHelper;

    private Dialog dialog;

    private Match currentMatch;

    public GameScreen(Boot boot) {
        super(boot);
        try {
            Gdx.graphics.setWindowedMode(550, 550);
            this.stage = new Stage();
            this.screenBodyHelper = new ScreenBodyHelper();
            this.currentMapItems = new ArrayList<>();
            this.camera = new OrthographicCamera();
            this.camera.setToOrtho(false, 550, 550);
            this.world = new World(new Vector2(0, 0), false);
            this.box2DDebugRenderer = new Box2DDebugRenderer();
            startRoomSetup();
            this.tiledMap = new TmxMapLoader().load(getStartRoomTileMap(map.getRooms()));
            parseMapObjets(tiledMap.getLayers().get("Capa de Objetos 1").getObjects(), false, 0,0);
            this.player.setMapUsables(currentMapItems);
            this.orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
            updateMatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setCurrentMatch() throws Exception {
        currentMatch = boot.isChallenge() ? ServiceStore.getInstance().getChallengeMatches().stream().filter(match -> match.getMap() != null).filter(match -> match.getUser().getUserId() == boot.getLoggedUser().getUserId() && map.getMapId() == match.getMap().getMapId()).findFirst().orElse(null) :
                ServiceStore.getInstance().getMatches().stream().filter(match -> match.getMap() != null).filter(match -> match.getUser().getUserId() == boot.getLoggedUser().getUserId() && map.getMapId() == match.getMap().getMapId()).findFirst().orElse(null);
    }

    public void parseMapObjets(MapObjects mapObjects , boolean changeMap, float newPosX, float newPosY){
        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof PolygonMapObject) {
                screenBodyHelper.createStaticBody((PolygonMapObject) mapObject, world);
            }

            if (mapObject instanceof RectangleMapObject) {
                rectanglePlayer = ((RectangleMapObject) mapObject).getRectangle();
                rectangleEnemy = ((RectangleMapObject) mapObject).getRectangle();
                itemRectangle = ((RectangleMapObject) mapObject).getRectangle();
                String rectangleName = mapObject.getName();

                if (rectangleName.equals("Player")) {
                    Body body = Helper.createBody(
                            changeMap? newPosX : rectanglePlayer.getX() + rectanglePlayer.getWidth() / 2,
                            changeMap? newPosY : rectanglePlayer.getY() + rectanglePlayer.getHeight() / 2,
                            rectanglePlayer.getWidth(),
                            rectanglePlayer.getHeight(),
                            false,
                            world

                    );
                    boot.getSelectedPlayer().setWidth(rectanglePlayer.getWidth());
                    boot.getSelectedPlayer().setWidth(rectanglePlayer.getHeight());
                    boot.getSelectedPlayer().setBody(body);
                    boot.getSelectedPlayer().setWorld(world);
                    boot.getSelectedPlayer().setEntityRectangle(rectanglePlayer);
                    player = boot.getSelectedPlayer();

                }

                if (rectangleName.equals("Enemy")) {
                    Body body = Helper.createBody(
                            rectangleEnemy.getX() + rectangleEnemy.getWidth() / 2,
                            rectangleEnemy.getY() + rectangleEnemy.getHeight() / 2,
                            rectangleEnemy.getWidth(),
                            rectangleEnemy.getHeight(),
                            false,
                            world
                    );
                    selectEnemy(body);
                }

                if (rectangleName.equals("Healing") && currentRoom.get("hasHealing").getAsBoolean()) {
                    Body body = Helper.createBody(
                            itemRectangle.getX() + itemRectangle.getWidth() / 2,
                            itemRectangle.getY() + itemRectangle.getHeight() / 2,
                            itemRectangle.getWidth(),
                            itemRectangle.getHeight(),
                            true,
                            world
                    );

                    Item item = (Healing) director.buildObject(new ItemBuilder()
                            .setMapItemSubclass("Healing")
                            .setHeight(itemRectangle.getHeight())
                            .setWidth(itemRectangle.getWidth())
                            .setBody(body)
                            .setRectangle(itemRectangle));
                    this.currentMapItems.add(item);
                }

                if(rectangleName.equals("Chest") && currentRoom.get("hasChest").getAsBoolean()){
                    Body body = Helper.createBody(
                            itemRectangle.getX() + itemRectangle.getWidth() / 2,
                            itemRectangle.getY() + itemRectangle.getHeight() / 2,
                            itemRectangle.getWidth(),
                            itemRectangle.getHeight(),
                            true,
                            world
                    );


                    Item item = (Chest) director.buildObject(new ItemBuilder()
                            .setMapItemSubclass("Chest")
                            .setHeight(itemRectangle.getHeight())
                            .setWidth(itemRectangle.getWidth())
                            .setBody(body)
                            .setRectangle(itemRectangle));
                    this.currentMapItems.add(item);
                }
                if(rectangleName.equals("Ending") && currentRoom.get("isEndRoom").getAsBoolean()){
                    Body body = Helper.createBody(
                            itemRectangle.getX() + itemRectangle.getWidth() / 2,
                            itemRectangle.getY() + itemRectangle.getHeight() / 2,
                            itemRectangle.getWidth(),
                            itemRectangle.getHeight(),
                            true,
                            world
                    );


                    Item item = (Ending) director.buildObject(new ItemBuilder()
                            .setMapItemSubclass("Ending")
                            .setHeight(itemRectangle.getHeight())
                            .setWidth(itemRectangle.getWidth())
                            .setBody(body)
                            .setRectangle(itemRectangle));
                    this.currentMapItems.add(item);
                }
            }
        }
    }

    public void startRoomSetup() throws Exception {
        map = boot.getSelectedMap();
        setCurrentMatch();
        if(currentMatch == null){
            this.currentRoom = setStartRoomObject(map.getRooms());
            return;
        }
        getMatchSavedRoom();
    }

    private void getMatchSavedRoom(){
        for (int i = 0; i < map.getRooms().size(); i++) {
            JsonObject room = (JsonObject) map.getRooms().get(i);
            if (room.get("x").getAsDouble() == currentMatch.getCurrentRoom().get("x").getAsDouble() && room.get("y").getAsDouble() == currentMatch.getCurrentRoom().get("y").getAsDouble()) {
                coorX = room.get("x").getAsDouble();
                coorY = room.get("y").getAsDouble();
                this.currentRoom = room;
                break;
            }
        }
    }

    public void checkCoordinate() {
        if (player.getPosY() > 554) {
            coorY += 50;
            changeMap(275,58);
        }
        if (player.getPosY() < 4) {
            coorY -= 50;
            changeMap(276,500);
        }
        if (player.getPosX() > 547) {
            coorX += 50;
            changeMap(50,275);
        }
        if (player.getPosX() < 2) {
            coorX -= 50;
            changeMap(510,275);
        }
    }

    public void changeMap(float newPosX, float newPosY) {
        try {
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            bodies.clear();
            if(player.getEntityMode() instanceof Ranged){
                ((Ranged) player.getEntityMode()).getProyectiles().clear();
                ((Ranged) player.getEntityMode()).getProyectileRectangles().clear();
            }
            if(player.getEntityMode() instanceof Trap){
                ((Trap) player.getEntityMode()).getTraps().clear();
                ((Trap) player.getEntityMode()).getTrapDamageRectangles().clear();
            }
            this.tiledMap.dispose();
            this.enemy = null;
            this.world = new World(new Vector2(0, 0), false);
            this.tiledMap = new TmxMapLoader().load(getNextRoomTileMap(map.getRooms()));
            this.currentRoom = setCurrentRoomObject(map.getRooms());
            this.currentMapItems = new ArrayList<>();
            this.stage.getActors().clear();
            updateMatch();
            parseMapObjets(tiledMap.getLayers().get("Capa de Objetos 1").getObjects(), true, newPosX, newPosY);
            this.player.setMapUsables(currentMapItems);
            this.orthogonalTiledMapRenderer.setMap(tiledMap);
            this.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String getNextRoomTileMap(JsonArray rooms) {
        for (int i = 0; i < rooms.size(); i++) {
            JsonObject room = (JsonObject) rooms.get(i);
            if (room.get("x").getAsDouble() == coorX && room.get("y").getAsDouble() == coorY) {
                coorX = room.get("x").getAsDouble();
                coorY = room.get("y").getAsDouble();
                return selectTileMap(room.get("leftDoor").getAsBoolean(),
                        room.get("bottomDoor").getAsBoolean(),
                        room.get("rightDoor").getAsBoolean(),
                        room.get("upDoor").getAsBoolean());
            }
        }
        return "";
    }

    public JsonObject setCurrentRoomObject(JsonArray rooms) {
        for (int i = 0; i < rooms.size(); i++) {
            JsonObject room = (JsonObject) rooms.get(i);
            if (room.get("x").getAsDouble() == coorX && room.get("y").getAsDouble() == coorY) {
                coorX = room.get("x").getAsDouble();
                coorY = room.get("y").getAsDouble();
                return room;
            }
        }
        return null;
    }

    public JsonObject setStartRoomObject(JsonArray rooms) {
        for (int i = 0; i < rooms.size(); i++) {
            JsonObject room = (JsonObject) rooms.get(i);
            if (room.get("isStartRoom").getAsBoolean()) {
                return room;
            }
        }
        return null;
    }

    public String getStartRoomTileMap(JsonArray rooms){
        if(currentMatch == null){
            for (int i = 0; i < rooms.size(); i++) {
                JsonObject room = (JsonObject) rooms.get(i);
                if(currentMatch == null){
                    if(room.get("isStartRoom").getAsBoolean()) {
                        coorX = room.get("x").getAsDouble();
                        coorY = room.get("y").getAsDouble();
                        return selectTileMap(room.get("leftDoor").getAsBoolean(),
                                room.get("bottomDoor").getAsBoolean(),
                                room.get("rightDoor").getAsBoolean(),
                                room.get("upDoor").getAsBoolean());
                    }
                }
            }
        }
        coorX = currentMatch.getCurrentRoom().get("x").getAsDouble();
        coorY = currentMatch.getCurrentRoom().get("y").getAsDouble();
        return selectTileMap(currentMatch.getCurrentRoom().get("leftDoor").getAsBoolean(),
                currentMatch.getCurrentRoom().get("bottomDoor").getAsBoolean(),
                currentMatch.getCurrentRoom().get("rightDoor").getAsBoolean(),
                currentMatch.getCurrentRoom().get("upDoor").getAsBoolean());
    }

    public String selectTileMap(boolean leftDoor, boolean bottonDoor, boolean rightDoor, boolean upDoor) {
        if (leftDoor && !bottonDoor && !rightDoor && upDoor) {
            return "map/Mapa1.tmx";
        } else if (leftDoor && bottonDoor && rightDoor && upDoor) {
            return selectRoomFourDoors();
        } else if (!leftDoor && bottonDoor && !rightDoor && !upDoor) {
            return "map/Mapa3.tmx";
        } else if (!leftDoor && bottonDoor && rightDoor && !upDoor) {
            return "map/Mapa 4.tmx";
        } else if (!leftDoor && !bottonDoor && !rightDoor && upDoor) {
            return "map/Mapa5.tmx";
        } else if (!leftDoor && bottonDoor && rightDoor && upDoor) {
            return "map/Mapa6.tmx";//((int)(Math.random()*2+1)==1) ? "map/Mapa6.tmx" : "map/Mapa15.tmx";
        } else if (leftDoor && bottonDoor && !rightDoor && upDoor) {
            return ((int) (Math.random() * 2 + 1) == 1) ? "map/Mapa7.tmx" : "map/Mapa14.tmx";
        } else if (!leftDoor && !bottonDoor && rightDoor && upDoor) {
            return "map/Mapa8.tmx";
        } else if (!leftDoor && bottonDoor && !rightDoor && upDoor) {
            return "map/Mapa9.tmx";
        } else if (leftDoor && bottonDoor && rightDoor && !upDoor) {
            return "map/Mapa12.tmx";
        } else if (leftDoor && !bottonDoor && !rightDoor && !upDoor) {
            return "map/oneEntranceLeft.tmx";
        } else if (!leftDoor && !bottonDoor && rightDoor && !upDoor) {
            return "map/oneEntranceRight.tmx";
        } else if (leftDoor && !bottonDoor && rightDoor && !upDoor) {
            return "map/leftAndRight.tmx";
        } else if (leftDoor && bottonDoor && !rightDoor && !upDoor) {
            return "map/bottomAndLeft.tmx";
        } else {
            return "map/Mapa13.tmx";
        }
    }

    public String selectRoomFourDoors() {
        int i = (int) (Math.random() * 3 + 1);
        if (i == 1) {
            return "map/Mapa2.tmx";
        } else if (i == 2) {
            return "map/Mapa10.tmx";
        } else return "map/Mapa11.tmx";
    }

    public void updateMatch() throws Exception {
        setCurrentMatch();
        if (currentMatch == null){
            MatchBuilder match = new MatchBuilder()
                    .setMatchId(0)
                    .setMap(map)
                    .setCurrentRoom(currentRoom)
                    .setUser(boot.getLoggedUser());
            ServiceStore.getInstance().saveMatch(director.buildObject(match));
            return;
        }
        if(currentRoom.get("hasCheckpoint").getAsBoolean()){
            currentMatch.setCurrentRoom(currentRoom);
            ServiceStore.getInstance().saveMatch(currentMatch);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        renderCheckpointLabel();
        playerHealth = new Label(String.valueOf(this.player.getHealth()),boot.getSkin());
        stage.addActor(playerHealth);
        if(enemy != null){
            enemyHealth = new Label(String.valueOf(this.enemy.getHealth()),boot.getSkin());
            stage.addActor(enemyHealth);
        }
        player.setItemList(boot.getLoggedUser().getItems());
        createWinDialog();
    }

    @Override
    public void render(float delta) {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsed += Gdx.graphics.getDeltaTime();

        world.step(1 / 60f, 6, 2);

        camera.update();
        orthogonalTiledMapRenderer.getBatch().setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
        orthogonalTiledMapRenderer.getBatch().begin();
        player.update();
        orthogonalTiledMapRenderer.getBatch().draw((player.isAttacking() ? player.getAttackTexture():this.player.getTexture()).getKeyFrame(elapsed), player.getBody().getPosition().x * Constants.PPM - 23f, player.getBody().getPosition().y * Constants.PPM - 24f, 50, 50);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            boot.getLoggedUser().setCoins(boot.getLoggedUser().getCoins()+player.getPickedCoins());
            dispose();
            boot.setScreen(new MapPickerScreen(boot));
        }
        renderHealth();
        if (enemy != null && bodies.contains(enemy.getBody(),false)) {
            enemy.update();
            orthogonalTiledMapRenderer.getBatch().draw((enemy.isAttacking() ? enemy.getAttackTexture():this.enemy.getTexture()).getKeyFrame(elapsed), enemy.getBody().getPosition().x * Constants.PPM - 23f, enemy.getBody().getPosition().y * Constants.PPM - 24f, 50, 50);
        }
        currentMapItems.forEach(item -> {
            if (bodies.contains(item.getBody(), false)) {
                item.update();
                orthogonalTiledMapRenderer.getBatch().draw(item.getTexture().getKeyFrame(elapsed), item.getBody().getPosition().x * Constants.PPM - 23f, item.getBody().getPosition().y * Constants.PPM - 24f, 50, 50);
            }
        });

        if(player.getEntityMode() instanceof Ranged){
            ((Ranged)player.getEntityMode()).getProyectiles().forEach(body -> {
                if(bodies.contains(body, false)){
                    orthogonalTiledMapRenderer.getBatch().draw(((Ranged)player.getEntityMode()).getProyectileSkin().getKeyFrame(elapsed), body.getPosition().x * Constants.PPM - 23f, body.getPosition().y * Constants.PPM - 24f, 50, 50);
                }
            });
        }

        if(enemy != null && enemy.getEntityMode() instanceof Ranged){
            ((Ranged)enemy.getEntityMode()).getProyectiles().forEach(body -> {
                if(bodies.contains(body, false)){
                    orthogonalTiledMapRenderer.getBatch().draw(((Ranged)enemy.getEntityMode()).getProyectileSkin().getKeyFrame(elapsed), body.getPosition().x * Constants.PPM - 23f, body.getPosition().y * Constants.PPM - 24f, 50, 50);
                }
            });
        }

        if(player.getEntityMode() instanceof Trap){
            ((Trap)player.getEntityMode()).getTraps().forEach(body -> {
                if(bodies.contains(body, false)){
                    orthogonalTiledMapRenderer.getBatch().draw(((Trap)player.getEntityMode()).getTrapSkin().getKeyFrame(elapsed), body.getPosition().x * Constants.PPM - 23f, body.getPosition().y * Constants.PPM - 24f, 50, 50);
                }
            });
        }
        checkCoordinate();

        orthogonalTiledMapRenderer.getBatch().end();

        if(player.isOpenedFinalDoor()){
            refreshMatch();
            dialog.show(stage);
        }
        //box2DDebugRenderer.render(world, camera.combined.scl(Constants.PPM));
    }

    private void refreshMatch() {
        try {
            currentMatch.setCurrentRoom(setStartRoomObject(map.getRooms()));
            ServiceStore.getInstance().saveMatch(currentMatch);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void selectEnemy(Body body) {
        if (currentRoom.get("hasEnemy").getAsBoolean()) {
            this.enemy = (Spawn) director.buildObject(new EntityBuilder("spawn")
                    .setHeight(rectangleEnemy.getHeight())
                    .setWidth(rectangleEnemy.getWidth())
                    .setBody(body)
                    .setOpponent(player)
                    .setWorld(world)
                    .setEntityRectangle(rectangleEnemy));
            this.player.setOpponent(enemy);
        } else if (currentRoom.get("hasBoss").getAsBoolean()) {
            this.enemy = (Boss) director.buildObject(new EntityBuilder("boss")
                    .setHeight(rectangleEnemy.getHeight())
                    .setWidth(rectangleEnemy.getWidth())
                    .setBody(body)
                    .setOpponent(player)
                    .setWorld(world)
                    .setEntityRectangle(rectangleEnemy));
            this.player.setOpponent(enemy);
        } else if (currentRoom.get("hasFinalBoss").getAsBoolean()) {
            this.enemy = (FinalBoss) director.buildObject(new EntityBuilder("finalBoss")
                    .setHeight(rectangleEnemy.getHeight())
                    .setWidth(rectangleEnemy.getWidth())
                    .setBody(body)
                    .setOpponent(player)
                    .setWorld(world)
                    .setEntityRectangle(rectangleEnemy));
            this.player.setOpponent(enemy);
        }
    }


    @Override
    public void dispose() {
        try {
            ServiceStore.getInstance().saveUser(boot.getLoggedUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renderHealth(){
        playerHealth.setText(String.valueOf(player.getHealth()));
        playerHealth.setPosition(player.getPosX(), player.getPosY()+10);
        if(player.getHealth()==0){
            stage.getActors().removeValue(playerHealth,false);
        }
        if(enemy != null){
            enemyHealth.setText(String.valueOf(enemy.getHealth()));
            enemyHealth.setPosition(enemy.getPosX(), enemy.getPosY()+10);
            if(enemy.isDead()){
                stage.getActors().removeValue(enemyHealth,false);
            }
        }
        stage.draw();
    }

    private void createWinDialog(){
        dialog = new Dialog("SUCCESS", boot.getSkin(),"dialog");
        Table table = new Table(boot.getSkin());
        Label label1 = new Label("YOU HAVE BEATEN THE GAME!",boot.getSkin());
        Label label2 = new Label("Press [ESC] to return to Pick-A-Map",boot.getSkin());
        table.add(label1);
        table.row();
        table.add(label2);
        table.row();
        dialog.getContentTable().add(table);
    }

    public void renderCheckpointLabel(){
        if(currentRoom.get("hasCheckpoint").getAsBoolean()) {
            Label label1 = new Label("Checkpoint", boot.getSkin());
            label1.setSize(300,100);
            label1.setPosition(370,430);
            stage.addActor(label1);
        }
    }
}
