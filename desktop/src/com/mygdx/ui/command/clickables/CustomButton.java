package com.mygdx.ui.command.clickables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.logic.model.user.User;
import com.mygdx.logic.service.facade.ServiceStore;
import com.mygdx.ui.Boot;
import com.mygdx.ui.screens.GameScreen;
import com.mygdx.ui.screens.MainScreen;
import com.mygdx.ui.screens.StoreScreen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomButton extends TextButton implements Clickable{

    private Boot boot;

    private String redirectScreen;
    private Object objectToSave;

    private Stage stage;

    public CustomButton(String text, Skin skin) {
        super(text, skin);
    }

    @Override
    public void redirect() {
        switch (redirectScreen) {
            case "worldCreator":
                boot.setScreen(new StoreScreen(boot));
                break;
            case "playScreen":
                boot.setScreen(new GameScreen(boot));
                break;
            case "storeScreen":
                boot.setScreen(new StoreScreen(boot));
                break;
            case "mainScreen":
                boot.setScreen(new MainScreen(boot));
                break;
        }
    }

    @Override
    public void saveObject() throws Exception {
        ServiceStore.getInstance().saveUser(objectToSave);
    }

    @Override
    public void logUser() {
        boot.setLoggedUser((User)objectToSave);
    }

    @Override
    public void exit() {
        Gdx.app.exit();
    }
}
