package com.mygdx.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.player.Player;
import com.mygdx.logic.service.facade.ServiceStore;
import com.mygdx.ui.Boot;
import com.mygdx.ui.helper.ScreenBodyHelper;
import com.mygdx.ui.command.ButtonAction;
import com.mygdx.ui.command.Invoker;
import com.mygdx.ui.command.actions.Redirect;
import com.mygdx.ui.command.clickables.ClickableLabel;

public class MapPickerScreen extends CustomScreen{

    private ClickableLabel pickAMap;

    private ClickableLabel challenges;

    private ClickableLabel pickAPlayer;

    private Table mapTable;

    private Table challengeTable;
    private Stage stage;

    private ScrollPane scrollPane;

    private Table outerTable;

    private SelectBox<String> playerDropdown;

    private ClickableLabel back;

    private Invoker backInvoker;

    private ButtonAction redirectMainScreen;

    private ScreenBodyHelper screenBodyHelper;

    private boolean isDropdownOpen;

    public MapPickerScreen(Boot boot) {
        super(boot);
        Gdx.graphics.setWindowedMode(500,650);
        this.stage = new Stage();
        this.isDropdownOpen = false;
        this.screenBodyHelper = new ScreenBodyHelper();
        this.backInvoker = new Invoker();
        this.mapTable = new Table(boot.getSkin());
        this.outerTable = new Table(boot.getSkin());
        this.challengeTable = new Table(boot.getSkin());
        this.playerDropdown = new SelectBox<>(boot.getSkin());

        this.back = new ClickableLabel(100,45,Gdx.graphics.getWidth()/2-50,520,new Texture("menuOptions/back.png"),new Texture("menuOptions/backI.png"));
        pickAMap = new ClickableLabel(310,60,Gdx.graphics.getWidth()/2-200,535,new Texture("menuOptions/pickAMap.png"),new Texture("menuOptions/pickAMap.png"));
        challenges = new ClickableLabel(220,50, Gdx.graphics.getWidth()/2-200, 345, new Texture("menuOptions/challenge.png"),new Texture("menuOptions/challenge.png"));
        pickAPlayer = new ClickableLabel(220,27, Gdx.graphics.getWidth()/2-200, 195, new Texture("menuOptions/pickAPlayer.png"),new Texture("menuOptions/pickAPlayer.png"));
    }

    @Override
    public void show(){
        try {
            Gdx.input.setInputProcessor(stage);

            this.playerDropdown.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    isDropdownOpen = !isDropdownOpen;
                }
            });

            this.playerDropdown.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    isDropdownOpen = false;
                }
            });

            boot.getSkin().getFont("default-font").getData().setScale(1.5f,1.5f);
            playerDropdown.setItems("Warrior","Wizard","Mercenary");
            playerDropdown.setWidth(300);
            playerDropdown.setHeight(40);
            playerDropdown.setPosition(Gdx.graphics.getWidth()/2-playerDropdown.getWidth()/2,150);
            stage.addActor(playerDropdown);
            setMaps();
            setChallenges();
            back.setBoot(boot);
            back.setRedirectScreen("mainScreen");
            redirectMainScreen = new Redirect(back);
            backInvoker.takeAction(redirectMainScreen);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        boot.getBatch().begin();
        boot.getSprite().draw(boot.getBatch());
        boot.getBatch().draw(pickAMap.getTexture(),Gdx.graphics.getWidth()/2 - pickAMap.getWidth()/2, pickAMap.getY(), pickAMap.getWidth(),pickAMap.getHeight());
        boot.getBatch().draw(challenges.getTexture(),Gdx.graphics.getWidth()/2 - challenges.getWidth()/2, challenges.getY(), challenges.getWidth(),challenges.getHeight());
        boot.getBatch().draw(pickAPlayer.getTexture(),Gdx.graphics.getWidth()/2 - pickAPlayer.getWidth()/2, pickAPlayer.getY(), pickAPlayer.getWidth(),pickAPlayer.getHeight());
        if(!isDropdownOpen){
            screenBodyHelper.handleButton(back,backInvoker,boot.getBatch());
        }
        stage.act();
        stage.draw();
        boot.getBatch().end();
    }

    public void setMaps() throws Exception {
        ServiceStore.getInstance().getMaps().forEach(map -> {
            Label label = new Label(String.format("%s: %s",map.getCreator().getName(),map.getName()),boot.getSkin());

            label.addListener(new ClickListener(){
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        setPlayer();
                        boot.setSelectedMap(map);
                        boot.setChallenge(false);
                        boot.setScreen(new GameScreen(boot));
                        return true;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            label.setAlignment(Align.center);
            mapTable.add(label).width(300).fill();
            mapTable.row();
        });
        scrollPane = new ScrollPane(mapTable, boot.getSkin());
        outerTable.setWidth(600);
        outerTable.setHeight(115);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        outerTable.add(scrollPane);
        outerTable.row();
        stage.addActor(outerTable);
        outerTable.setPosition(Gdx.graphics.getWidth()/2-outerTable.getWidth()/2,410);
    }

    public void setChallenges() throws Exception {
        challengeTable.setPosition(Gdx.graphics.getWidth()/2-challengeTable.getWidth()/2,290);
        ServiceStore.getInstance().getChallenges().forEach(map -> {
            Label label = new Label(map.getName(),boot.getSkin());
            label.addListener(new ClickListener(){
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    try{
                        setPlayer();
                        boot.setSelectedMap(map);
                        boot.setChallenge(true);
                        boot.setScreen(new GameScreen(boot));
                        return true;
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }
            });
            challengeTable.add(label);
            challengeTable.row();

        });
        stage.addActor(challengeTable);
    }

    public void setPlayer() {
        switch (playerDropdown.getSelected()){
            case "Warrior":
                boot.setSelectedPlayer((Player)director.buildObject(new EntityBuilder("playerWarrior")));
                break;
            case "Wizard":
                boot.setSelectedPlayer((Player)director.buildObject(new EntityBuilder("playerWizard")));
                break;
            case "Mercenary":
                boot.setSelectedPlayer((Player)director.buildObject(new EntityBuilder("playerMercenary")));
                break;
        };
    }

    @Override
    public void dispose(){
        stage.dispose();
        challengeTable = null;
        mapTable = null;
        playerDropdown = null;
    }
}
