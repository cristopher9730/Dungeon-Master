//package com.mygdx.ui.screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//
//import com.mygdx.ui.Boot;
//
//
//
//public class StoreScreen extends CustomScreen{
//    public StoreScreen(Boot boot) {
//        super(boot);
//        Gdx.graphics.setWindowedMode(550, 550);
//        boot.setSprite(new Sprite(new Texture("map/Store.png")));
//        boot.setBatch(new SpriteBatch());
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0,0,0,1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        boot.getBatch().begin();
//        boot.getSprite().draw(boot.getBatch());
//        boot.getBatch().end();
//    }
//}

package com.mygdx.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.logic.model.user.Item;
import com.mygdx.logic.service.facade.ServiceStore;
import com.mygdx.ui.helper.ScreenBodyHelper;
import com.mygdx.ui.helper.StoreScreenHelper;
import com.mygdx.ui.command.clickables.ClickableLabel;
import com.mygdx.logic.model.mapElements.UiRoom;
import com.mygdx.ui.Boot;

import com.mygdx.ui.command.Invoker;
import com.mygdx.ui.command.actions.Redirect;


import java.util.ArrayList;
import java.util.List;

public class StoreScreen extends CustomScreen {

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private OrthographicCamera camera;
    private Stage stage;
    private List<UiRoom> roomsToStore;
    private TiledMap tiledMap;
    private ClickableLabel cancel;
    private Invoker cancelInvoker;
    private List<Item> itemList;

    private Redirect redirectMainScreen;

    private StoreScreenHelper storeScreenHelper;

    private ScreenBodyHelper screenBodyHelper;

    public StoreScreen(Boot boot){
        super(boot);

        this.cancelInvoker = new Invoker();

        this.cancel = new ClickableLabel(100,35,225,400,new Texture("menuOptions/cancel_2.png"),new Texture("menuOptions/cancel.png"));

        Gdx.graphics.setWindowedMode(550,550);

        this.tiledMap = new TmxMapLoader().load("map/Store.tmx");

        this.orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        this.stage = new Stage();

        this.camera = new OrthographicCamera();

        this.roomsToStore = new ArrayList<>();

        this.storeScreenHelper = new StoreScreenHelper();

        this.screenBodyHelper = new ScreenBodyHelper();

    }

    public void setUserCoins(){
        Label label1 = new Label("Coins: " + boot.getLoggedUser().getCoins(), boot.getSkin());
        label1.setSize(300,100);
        label1.setPosition(370,430);
        stage.addActor(label1);
    }

    @Override
    public void show() {

        try {
            setUserCoins();
            this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.itemList = ServiceStore.getInstance().getItems();

            storeScreenHelper.getSelectableItems(boot.getLoggedUser().getItems(),itemList, boot, stage).forEach(actor -> stage.addActor(actor));
            Gdx.input.setInputProcessor(stage);

            cancel.setBoot(boot);
            cancel.setRedirectScreen("mainScreen");
            redirectMainScreen = new Redirect(cancel);
            cancelInvoker.takeAction(redirectMainScreen);
            setUserCoins();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
        orthogonalTiledMapRenderer.getBatch().begin();
        stage.act();
        stage.draw();

        screenBodyHelper.handleButton(cancel,cancelInvoker,orthogonalTiledMapRenderer.getBatch());
        orthogonalTiledMapRenderer.getBatch().end();
    }

    @Override
    public void dispose(){
        stage.dispose();
    }
}

