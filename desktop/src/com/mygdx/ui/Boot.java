package com.mygdx.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.logic.model.mapElements.Map;
import com.mygdx.logic.model.entity.player.Player;
import com.mygdx.logic.model.user.User;
import com.mygdx.ui.screens.UserListScreen;
import lombok.Data;

@Data
public class Boot extends Game {

    public static Boot INSTANCE;

    private Batch batch;
    private Texture background;
    private Sprite sprite;
    private Skin skin;
    private User loggedUser;

    private Map selectedMap;

    private Player selectedPlayer;

    private boolean isChallenge;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public Boot(){
        INSTANCE = this;
    }

    public Batch getBatch() {
        return batch;
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void create() {
        try {
            this.background = new Texture("img/mainMenuBackground1.png");
            this.sprite = new Sprite(background);
            this.batch = new SpriteBatch();
            this.skin = new Skin(new FileHandle("./assets/skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));
            setScreen(new UserListScreen(INSTANCE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

