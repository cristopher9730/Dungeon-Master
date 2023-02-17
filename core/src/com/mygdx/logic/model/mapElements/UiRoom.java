package com.mygdx.logic.model.mapElements;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.logic.model.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class UiRoom extends Image {

    private Texture texture;
    private boolean upDoor;
    private boolean bottomDoor;
    private boolean leftDoor;
    private boolean rightDoor;
    private Stage stage;
    private List<UiRoom> uiRooms;
    private boolean isEndRoom;
    private boolean isStartRoom;

    private boolean hasHealing;

    private boolean hasCheckpoint;

    private boolean hasEnemy;

    private boolean hasBoss;

    private boolean hasFinalBoss;

    private boolean hasChest;
    private User loggedUser;

    public UiRoom(Texture texture, Stage stage, List<UiRoom> currentRooms, boolean upDoor, boolean bottomDoor, boolean leftDoor, boolean rightDoor, User loggedUser){
        super(texture);
        this.texture = texture;
        this.loggedUser = loggedUser;
        this.upDoor = upDoor;
        this.bottomDoor = bottomDoor;
        this.leftDoor = leftDoor;
        this.rightDoor = rightDoor;
        addKeyInput(this, currentRooms);
        addClickInput(this,stage, currentRooms);

    }



    private void addKeyInput(final UiRoom uiRoom, final List<UiRoom> uiRooms){
        this.uiRooms = uiRooms;
        uiRoom.addListener(new InputListener(){
            public boolean keyDown(InputEvent inputEvent, int keyCode){
                MoveToAction mba = new MoveToAction();
                mba.setPosition(getX(),getY());
                switch(keyCode) {
                    case Input.Keys.D:
                        if (getX() <= 450) {
                            mba.setPosition(getX() + 50, getY());
                        }
                        break;
                    case Input.Keys.A:
                        if (getX() >= 100) {
                            mba.setPosition(getX() - 50, getY());
                        }
                        break;
                    case Input.Keys.S:
                        if (getY() >= 100) {
                            mba.setPosition(getX(), getY()-50);
                        }
                        break;
                    case Input.Keys.W:
                        if (getY() <= 450) {
                            mba.setPosition(getX(), getY()+50);
                        }
                        break;
                    case Input.Keys.K:
                        remove();
                        uiRooms.remove(UiRoom.this);
                        validateRoomsWithColor();
                        break;
                    case Input.Keys.Q:
                        setRotation(getRotation()+90);
                        setOrigin(getWidth()/2, getHeight()/2);
                        mba.setPosition(getX(),getY());
                        rotateDoorsToTheLeft();
                        break;
                    case Input.Keys.E:
                        setRotation(getRotation()-90);
                        setOrigin(getWidth()/2, getHeight()/2);
                        mba.setPosition(getX(),getY());
                        rotateDoorsToTheRight();
                        break;
                    default: mba.setPosition(getX(), getY()); break;
                }
                addAction(mba);
                return true;
            }

            public boolean keyUp(InputEvent inputEvent, int keyCode){
                validateRoomsWithColor();
                return true;
            }
        });
    }


    private void addClickInput(UiRoom actor, Stage stage1, List<UiRoom> roomsToStore1){
        this.stage = stage1;
        this.uiRooms = roomsToStore1;
        actor.addListener(new ClickListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                switch (button){
                    case Input.Buttons.LEFT:
                        if(getX() > 450){
                            final UiRoom movingActor =  new UiRoom(getTexture(), stage, uiRooms, isUpDoor(),isBottomDoor(),isLeftDoor(),isRightDoor(), loggedUser);
                            movingActor.setColor(Color.RED);
                            movingActor.setBounds(50,50,50,50);
                            stage.addActor(movingActor);
                            stage.setKeyboardFocus(movingActor);
                            uiRooms.add(movingActor);
                        }
                        else{
                            stage.setKeyboardFocus(UiRoom.this);
                        }
                        break;
                    case Input.Buttons.RIGHT:
                        if(getX() < 500){
                            createDialog(actor);
                            stage.setKeyboardFocus(UiRoom.this);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void rotateDoorsToTheLeft() {
        if (isLeftDoor() && isBottomDoor() && !isUpDoor() && !isRightDoor()) {
            setLeftDoor(false);
            setRightDoor(true);
            return;
        }
        if (isLeftDoor() && !isBottomDoor() && isUpDoor() && !isRightDoor()) {
            setUpDoor(false);
            setBottomDoor(true);
            return;
        }
        if (!isLeftDoor() && !isBottomDoor() && isUpDoor() && isRightDoor()) {
            setRightDoor(false);
            setLeftDoor(true);
            return;
        }
        if (!isLeftDoor() && isBottomDoor() && !isUpDoor() && isRightDoor()) {
            setBottomDoor(false);
            setUpDoor(true);
            return;
        }
        if (!isLeftDoor() && isBottomDoor() && !isUpDoor() && !isRightDoor()) {
            setBottomDoor(false);
            setRightDoor(true);
            return;
        }
        if (isLeftDoor() && !isBottomDoor() && !isUpDoor() && !isRightDoor()) {
            setLeftDoor(false);
            setBottomDoor(true);
            return;
        }
        if (!isLeftDoor() && !isBottomDoor() && isUpDoor() && !isRightDoor()) {
            setUpDoor(false);
            setLeftDoor(true);
            return;
        }
        if (!isLeftDoor() && !isBottomDoor() && !isUpDoor() && isRightDoor()) {
            setRightDoor(false);
            setUpDoor(true);
            return;
        }

        if (!isLeftDoor() && isBottomDoor() && isUpDoor() && !isRightDoor()) {
            setBottomDoor(false);
            setUpDoor(false);
            setRightDoor(true);
            setLeftDoor(true);
            return;
        }

        if (isLeftDoor() && !isBottomDoor() && !isUpDoor() && isRightDoor()) {
            setRightDoor(false);
            setLeftDoor(false);
            setUpDoor(true);
            setBottomDoor(true);
            return;
        }

        if (isLeftDoor() && isBottomDoor() && !isUpDoor() && isRightDoor()) {
            setLeftDoor(false);
            setUpDoor(true);
            return;
        }

        if (!isLeftDoor() && isBottomDoor() && isUpDoor() && isRightDoor()) {
            setBottomDoor(false);
            setLeftDoor(true);
            return;
        }

        if (isLeftDoor() && !isBottomDoor() && isUpDoor() && isRightDoor()) {
            setRightDoor(false);
            setBottomDoor(true);
            return;
        }

        if (isLeftDoor() && isBottomDoor() && isUpDoor() && !isRightDoor()) {
            setUpDoor(false);
            setRightDoor(true);
        }
    }

    private void rotateDoorsToTheRight(){
        if(isLeftDoor() && isBottomDoor() && !isUpDoor() && !isRightDoor()){
            setBottomDoor(false);
            setUpDoor(true);
            return;
        }
        if(isLeftDoor() && !isBottomDoor() && isUpDoor() && !isRightDoor()){
            setLeftDoor(false);
            setRightDoor(true);
            return;
        }
        if(!isLeftDoor() && !isBottomDoor() && isUpDoor() && isRightDoor()){
            setUpDoor(false);
            setBottomDoor(true);
            return;
        }
        if(!isLeftDoor() && isBottomDoor() && !isUpDoor() && isRightDoor()){
            setRightDoor(false);
            setLeftDoor(true);
            return;
        }
        if(!isLeftDoor() && isBottomDoor() && !isUpDoor() && !isRightDoor()){
            setBottomDoor(false);
            setLeftDoor(true);
            return;
        }
        if(isLeftDoor() && !isBottomDoor() && !isUpDoor() && !isRightDoor()){
            setLeftDoor(false);
            setUpDoor(true);
            return;
        }
        if(!isLeftDoor() && !isBottomDoor() && isUpDoor() && !isRightDoor()){
            setUpDoor(false);
            setRightDoor(true);
            return;
        }
        if(!isLeftDoor() && !isBottomDoor() && !isUpDoor() && isRightDoor()){
            setRightDoor(false);
            setBottomDoor(true);
            return;
        }

        if(!isLeftDoor() && isBottomDoor() && isUpDoor() && !isRightDoor()){
            setBottomDoor(false);
            setUpDoor(false);
            setRightDoor(true);
            setLeftDoor(true);
            return;
        }

        if(isLeftDoor() && !isBottomDoor() && !isUpDoor() && isRightDoor()){
            setLeftDoor(false);
            setRightDoor(false);
            setUpDoor(true);
            setBottomDoor(true);
            return;
        }

        if (isLeftDoor() && isBottomDoor() && !isUpDoor() && isRightDoor()) {
            setRightDoor(false);
            setUpDoor(true);
            return;
        }

        if (!isLeftDoor() && isBottomDoor() && isUpDoor() && isRightDoor()) {
            setUpDoor(false);
            setLeftDoor(true);
            return;
        }

        if (isLeftDoor() && !isBottomDoor() && isUpDoor() && isRightDoor()) {
            setLeftDoor(false);
            setBottomDoor(true);
            return;
        }

        if (isLeftDoor() && isBottomDoor() && isUpDoor() && !isRightDoor()) {
            setBottomDoor(false);
            setRightDoor(true);
        }
    }

    public void validateRoomsWithColor(){
        uiRooms.forEach(actor -> {
            if(isConnected(actor, uiRooms)){
                actor.setColor(Color.WHITE);
            }
            else{
                actor.setColor(Color.RED);
            }
        });
    }

    public boolean isConnected(UiRoom uiRoom, List<UiRoom> uiRooms){
        return getSurroundingRooms(uiRoom, uiRooms).stream().anyMatch(
                actor -> ((uiRoom.isUpDoor() && actor.isBottomDoor() && actor.getY() == uiRoom.getY() + 50) ||
                        (uiRoom.isBottomDoor() && actor.isUpDoor() && actor.getY() == uiRoom.getY() - 50) ||
                        (uiRoom.isRightDoor() && actor.isLeftDoor() && actor.getX() == uiRoom.getX() + 50) ||
                        (uiRoom.isLeftDoor() && actor.isRightDoor() && actor.getX() == uiRoom.getX() - 50)));
    }

    public List<UiRoom> getSurroundingRooms(UiRoom uiRoom, List<UiRoom> uiRooms){
        List<UiRoom> surroundingRooms = new ArrayList<>();
        uiRooms.forEach(actor -> {
            if(uiRoom.getY()+50 == actor.getY() && uiRoom.getX() == actor.getX()
                    || uiRoom.getY() == actor.getY() && uiRoom.getX() == actor.getX()-50
                    || uiRoom.getY() == actor.getY() && uiRoom.getX() == actor.getX()+50
                    || uiRoom.getX() == actor.getX() && uiRoom.getY()-50 == actor.getY()){
                surroundingRooms.add(actor);
            }
        });
        return surroundingRooms;
    }

    public void createDialog(UiRoom uiRoom){
        Skin skin = new Skin(new FileHandle("./assets/skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));
        Dialog dialog = new Dialog("Specs", skin,"dialog");
        CheckBox startCheckbox = new CheckBox("Start Room", skin);
        CheckBox endCheckbox = new CheckBox("End Room", skin);

        ButtonGroup<CheckBox> buttonGroup = new ButtonGroup<>();
        buttonGroup.add(startCheckbox);
        buttonGroup.add(endCheckbox);
        buttonGroup.setMinCheckCount(0);
        buttonGroup.setMaxCheckCount(1);

        startCheckbox.setChecked(uiRoom.isStartRoom);
        endCheckbox.setChecked(uiRoom.isEndRoom);

        startCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                uiRoom.setStartRoom(startCheckbox.isChecked());
                validateEdgeAndFinalBossRooms(uiRoom);
            }
        });

        endCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                uiRoom.setEndRoom(endCheckbox.isChecked());
                validateEdgeAndFinalBossRooms(uiRoom);
            }
        });

        Table table = new Table(skin);

        table.add(startCheckbox);
        table.add(endCheckbox);
        table.row();

        setRoomEnemy(skin,uiRoom,table);
        setRoomItemList(skin,uiRoom, table);
        dialog.getContentTable().add(table);
        dialog.button("Close");
        dialog.show(stage);
    }

    private void validateEdgeAndFinalBossRooms(UiRoom uiRoomParam){

        List<UiRoom> filteredUiRooms = uiRooms;
        filteredUiRooms.remove(uiRoomParam);

        if(uiRoomParam.isStartRoom){
            filteredUiRooms.forEach(roomActor1 -> {
                    roomActor1.setStartRoom(false);
            });
        }
        else if(uiRoomParam.isEndRoom){
            filteredUiRooms.forEach(roomActor1 -> {
                    roomActor1.setEndRoom(false);
            });
        }
        else if(uiRoomParam.isHasFinalBoss()){
            filteredUiRooms.forEach(roomActor1 -> {
                roomActor1.setHasFinalBoss(false);
            });
        }
        filteredUiRooms.add(uiRoomParam);
        this.uiRooms = filteredUiRooms;
    }


    public void setRoomItemList(Skin skin,UiRoom uiRoom, Table table){
        ButtonGroup<CheckBox> itemButtons = new ButtonGroup<>();

        itemButtons.setMaxCheckCount(2);
        itemButtons.setMinCheckCount(0);

        if(loggedUser.getItems().size()>0){
            loggedUser.getItems().forEach(item -> {
                if(item.getItemId() == 1){
                    CheckBox healing = new CheckBox("Healing", skin);
                    healing.setChecked(uiRoom.isHasHealing());
                    healing.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            uiRoom.setHasHealing(healing.isChecked());
                        }
                    });
                    itemButtons.add(healing);
                    table.add(healing);
                }
                else if(item.getItemId() == 2){
                    CheckBox checkPoint = new CheckBox("Checkpoint", skin);
                    checkPoint.setChecked(uiRoom.isHasCheckpoint());
                    checkPoint.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            uiRoom.setHasCheckpoint(checkPoint.isChecked());
                        }
                    });
                    itemButtons.add(checkPoint);
                    table.add(checkPoint);
                }
            });
        }
        CheckBox chest = new CheckBox("Chest", skin);
        chest.setChecked(uiRoom.isHasChest());
        chest.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                uiRoom.setHasChest(chest.isChecked());
            }
        });
        itemButtons.add(chest);
        table.add(chest);
        table.row();
    }

    public void setRoomEnemy(Skin skin,UiRoom uiRoom, Table table){
        ButtonGroup<CheckBox> enemyButtons = new ButtonGroup<>();

        enemyButtons.setMaxCheckCount(1);
        enemyButtons.setMinCheckCount(0);

        CheckBox enemy = new CheckBox("Enemy", skin);
        enemy.setChecked(uiRoom.isHasEnemy());
        enemyButtons.add(enemy);
        enemy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                uiRoom.setHasEnemy(enemy.isChecked());
            }
        });
        CheckBox boss = new CheckBox("Boss", skin);
        boss.setChecked(uiRoom.isHasBoss());
        enemyButtons.add(boss);
        boss.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                uiRoom.setHasBoss(boss.isChecked());
            }
        });


        CheckBox finalBoss = new CheckBox("Final Boss", skin);
        finalBoss.setChecked(uiRoom.isHasFinalBoss());
        enemyButtons.add(finalBoss);
        finalBoss.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                uiRoom.setHasFinalBoss(finalBoss.isChecked());
                validateEdgeAndFinalBossRooms(uiRoom);
            }
        });

        table.add(enemy);
        table.add(boss);
        table.add(finalBoss);
        table.row();
    }

    @Override
    public String toString() {
        return String.format("{\"x\":%s,\"y\":%s,\"leftDoor\":%s,\"bottomDoor\":%s,\"rightDoor\":%s,\"upDoor\":%s, \"isStartRoom\":%s, \"isEndRoom\":%s, \"color\":%s, \"hasHealing\":%s, \"hasCheckpoint\":%s, \"hasChest\":%s, \"hasEnemy\":%s, \"hasBoss\":%s, \"hasFinalBoss\":%s}"
                ,getX(),getY(),isLeftDoor(),isBottomDoor(),isRightDoor(), isUpDoor(), isStartRoom(), isEndRoom(), getColor(), isHasHealing(), isHasCheckpoint(), isHasChest(),isHasEnemy(), isHasBoss(), isHasFinalBoss());
    }

}
