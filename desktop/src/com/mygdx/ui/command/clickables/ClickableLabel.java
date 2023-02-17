package com.mygdx.ui.command.clickables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.ui.Boot;
import com.mygdx.ui.helper.MapScreenHelper;
import com.mygdx.ui.screens.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClickableLabel implements Clickable{

    private MapScreenHelper mapScreenHelper;

    //base attributes
    private int width;
    private int height;
    private float y;

    private float x;
    private Texture texture;
    private Texture inactiveTexture;

    //action attirbutes
    private Boot boot;
    private String redirectScreen;
    private String buttonAction;
    private Object objectToSave;
    private Stage stage;
    private String customStringField;

    public ClickableLabel(int width, int height, float x, float y, Texture texture, Texture inactiveTexture) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.inactiveTexture = inactiveTexture;
        this.mapScreenHelper = new MapScreenHelper();
    }

    @Override
    public void redirect() {
        switch (redirectScreen) {
            case "worldCreator":
                boot.getScreen().dispose();
                boot.setScreen(new WorldCreatorScreen(boot));
                break;
            case "playScreen":
                boot.getScreen().dispose();
                boot.setScreen(new GameScreen(boot));
                break;
            case "storeScreen":
                boot.getScreen().dispose();
                boot.setScreen(new StoreScreen(boot));
                break;
            case "mainScreen":
                boot.getScreen().dispose();
                boot.setScreen(new MainScreen(boot));
                break;
            case "pickScreen":
                boot.getScreen().dispose();
                boot.setScreen(new MapPickerScreen(boot));
                break;
        }
    }

    @Override
    public void saveObject() throws Exception {
        mapScreenHelper.validateAndSave(objectToSave, stage, customStringField);
    }

    @Override
    public void logUser() {}

    @Override
    public void exit() {
        Gdx.app.exit();
    }
}