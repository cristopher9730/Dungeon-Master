package com.mygdx.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.logic.model.builder.builderObjects.MapBuilder;
import com.mygdx.ui.helper.ScreenBodyHelper;
import com.mygdx.logic.helper.GifHelper;
import com.mygdx.ui.helper.MapScreenHelper;
import com.mygdx.ui.command.ButtonAction;
import com.mygdx.ui.command.clickables.ClickableLabel;
import com.mygdx.logic.model.mapElements.UiRoom;
import com.mygdx.ui.Boot;

import com.mygdx.ui.command.actions.Save;
import com.mygdx.ui.command.Invoker;
import com.mygdx.ui.command.actions.Redirect;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorldCreatorScreen extends CustomScreen {

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private OrthographicCamera camera;

    private GifHelper gifHelper;

    private Stage stage;

    private ScreenBodyHelper screenBodyHelper;

    private List<UiRoom> roomsToStore;

    private TiledMap tiledMap;

    private ClickableLabel cancel;

    private ClickableLabel create;

    private Invoker cancelInvoker;

    private Invoker saveInvoker;

    private ButtonAction saveMap;

    private ButtonAction redirectMainScreen;

    private TextField mapName;

    private ClickableLabel mapNameLabel;

    private MapScreenHelper mapScreenHelper;


    public WorldCreatorScreen(Boot boot){
        super(boot);
        this.cancelInvoker = new Invoker();
        this.saveInvoker = new Invoker();
        this.cancel = new ClickableLabel(100,35,850,500,new Texture("menuOptions/cancel_2.png"),new Texture("menuOptions/cancel.png"));
        this.create = new ClickableLabel(100,35,650,500,new Texture("menuOptions/create.png"),new Texture("menuOptions/create2.png"));
        this.mapNameLabel = new ClickableLabel(180,45,715,480,new Texture("menuOptions/mapName.png"),new Texture("menuOptions/mapName.png"));
        Gdx.graphics.setWindowedMode(1050,600);
        this.screenBodyHelper = new ScreenBodyHelper();
        this.tiledMap = new TmxMapLoader().load("map/mapaWorldCreator.tmx");
        this.orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        this.stage = new Stage();
        this.gifHelper = new GifHelper();
        this.camera = new OrthographicCamera();
        this.roomsToStore = new ArrayList<>();
        this.mapName = new TextField("", boot.getSkin());
        this.mapScreenHelper = new MapScreenHelper();
    }

    @Override
    public void show() {
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mapScreenHelper.getSelectableRooms(stage,roomsToStore, boot).forEach(roomActor -> stage.addActor(roomActor));

        mapName.setWidth(300);
        mapName.setHeight(40);

        cancel.setBoot(boot);
        cancel.setRedirectScreen("mainScreen");
        redirectMainScreen = new Redirect(cancel);
        cancelInvoker.takeAction(redirectMainScreen);

        create.setBoot(boot);
        create.setRedirectScreen("mainScreen");
        create.setStage(stage);
        saveMap = new Save(create);
        saveInvoker.takeAction(saveMap);
        mapName.setPosition(650, Gdx.graphics.getHeight() - (mapName.getHeight()+ 125));
        stage.addActor(mapName);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta){
        create.setCustomStringField(mapName.getText());
        if (mapScreenHelper.roomValidation(mapScreenHelper.getStorableRooms(roomsToStore), mapName.getText())) {
            saveInvoker.takeAction(redirectMainScreen);
        } else {
            saveInvoker.removeAction(redirectMainScreen);
        }
        create.setObjectToSave(director.buildObject(new MapBuilder().setCreator(boot.getLoggedUser()).setName(mapName.getText()).setRooms(mapScreenHelper.getStorableRooms(roomsToStore)).setCreationDate(LocalDate.now())));
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();

        orthogonalTiledMapRenderer.getBatch().begin();
        stage.act();
        stage.draw();
        orthogonalTiledMapRenderer.getBatch().draw(mapNameLabel.getTexture(),mapNameLabel.getX(), mapNameLabel.getY(), mapNameLabel.getWidth(),mapNameLabel.getHeight());
        screenBodyHelper.handleButton(cancel,cancelInvoker,orthogonalTiledMapRenderer.getBatch());
        screenBodyHelper.handleButton(create,saveInvoker,orthogonalTiledMapRenderer.getBatch());

        orthogonalTiledMapRenderer.getBatch().end();
    }
}
