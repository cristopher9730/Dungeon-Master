package com.mygdx.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.model.builder.builderObjects.UserBuilder;
import com.mygdx.logic.service.facade.ServiceStore;
import com.mygdx.ui.Boot;
import com.mygdx.logic.helper.GifHelper;
import com.mygdx.ui.helper.ScreenBodyHelper;
import com.mygdx.ui.command.ButtonAction;
import com.mygdx.ui.command.actions.Exit;
import com.mygdx.ui.command.clickables.ClickableLabel;
import com.mygdx.ui.command.clickables.CustomButton;
import com.mygdx.ui.command.actions.Log;
import com.mygdx.ui.command.actions.Save;
import com.mygdx.ui.command.Invoker;
import com.mygdx.ui.command.actions.Redirect;

import java.util.ArrayList;


public class UserListScreen extends CustomScreen {

    private GifHelper gifHelper;

    private ClickableLabel title;
    private ClickableLabel pickUser;
    private ClickableLabel registerT;

    private SelectBox<String> selectBox;

    private Array<String> userNames;

    private Stage stage;

    private TextField userName;

    private CustomButton login;
    private CustomButton register;

    private ButtonAction redirectMainScreen;

    private Invoker logInInvoker;

    private Invoker registerInvoker;

    private ButtonAction logUser;

    private ButtonAction saveUser;

    private ButtonAction exitGame;

    private ClickableLabel exit;

    private Invoker exitInvoker;

    private ScreenBodyHelper screenBodyHelper;

    //Screem de inicio
    public UserListScreen(Boot boot){
        super(boot);
        logInInvoker = new Invoker();
        registerInvoker = new Invoker();
        this.exitInvoker = new Invoker();
        this.stage = new Stage();
        this.login = new CustomButton("Log In",boot.getSkin());
        this.selectBox = new SelectBox<>(boot.getSkin());
        this.userNames = new Array<>();
        this.gifHelper = new GifHelper();
        this.boot = boot;
        this.title = new ClickableLabel(400,100,Gdx.graphics.getWidth()/2-200,35,new Texture("menuTitle.png"), new Texture("menuTitle.png"));
        this.pickUser = new ClickableLabel(250,45,Gdx.graphics.getWidth()/2-125,125,new Texture("menuOptions/pickUser.png"), new Texture("menuOptions/pickUser.png"));
        this.registerT = new ClickableLabel(159,52,Gdx.graphics.getWidth()/2-80,300,new Texture("menuOptions/Register2.png"),new Texture("menuOptions/Register2.png"));
        this.userName = new TextField("",boot.getSkin());
        this.register = new CustomButton("Register",boot.getSkin());
        this.exit = new ClickableLabel(100,50,Gdx.graphics.getWidth()/2-50,500,new Texture("menuOptions/Exit.png"),new Texture("menuOptions/Exit_2.png"));
        this.screenBodyHelper = new ScreenBodyHelper();
    }

    @Override
    public void show() {
        try {
            ServiceStore.getInstance().getUsers().forEach(user -> userNames.add(user.getName()));
        selectBox.setItems(userNames);
        selectBox.setWidth(300);
        selectBox.setHeight(40);
        login.setWidth(80);
        login.setHeight(35);
        userName.setWidth(300);
        userName.setHeight(40);
        register.setWidth(80);
        register.setHeight(35);

        logUser = new Log(login);
        login.setRedirectScreen("mainScreen");
        login.setBoot(boot);
        redirectMainScreen = new Redirect(login);

        logInInvoker.takeAction(logUser);
        logInInvoker.takeAction(redirectMainScreen);


        register.setRedirectScreen("mainScreen");
        register.setBoot(boot);
        logUser = new Log(register);
        saveUser = new Save(register);

        registerInvoker.takeAction(saveUser);
        registerInvoker.takeAction(logUser);
        registerInvoker.takeAction(redirectMainScreen);

            exit.setBoot(boot);
            exit.setButtonAction("exit");
            this.exitGame = new Exit(exit);
            exitInvoker.takeAction(exitGame);


        login.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    login.setObjectToSave(ServiceStore.getInstance().getUsers().stream().filter(user -> user.getName().equals(selectBox.getSelected())).findFirst().orElse(null));
                    logInInvoker.placeActions();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        selectBox.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectBox.showScrollPane();
            }
        });

        register.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if(!userName.getText().isEmpty()) {
                        register.setObjectToSave(director.buildObject(new UserBuilder().setName(userName.getText()).setItems(new ArrayList<>())));
                        registerInvoker.placeActions();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        login.setPosition(Gdx.graphics.getWidth()/2-login.getWidth()/2, Gdx.graphics.getHeight() - (login.getHeight()+ 240));
        selectBox.setPosition(Gdx.graphics.getWidth()/2-selectBox.getWidth()/2,Gdx.graphics.getHeight() - (selectBox.getHeight()+ 185));
        userName.setPosition(Gdx.graphics.getWidth()/2-userName.getWidth()/2, Gdx.graphics.getHeight() - (userName.getHeight()+ 375));
        register.setPosition(Gdx.graphics.getWidth()/2-register.getWidth()/2, Gdx.graphics.getHeight() - (register.getHeight()+ 430));

        stage.addActor(userName);
        stage.addActor(register);
        stage.addActor(login);
        stage.addActor(selectBox);
        Gdx.input.setInputProcessor(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(float delta) {
        try {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            boot.getBatch().begin();
            boot.getSprite().draw(boot.getBatch());
            boot.getBatch().draw(title.getTexture(),Gdx.graphics.getWidth()/2 - title.getWidth()/2, Gdx.graphics.getHeight() - (title.getHeight()+ title.getY()), title.getWidth(),title.getHeight());
            boot.getBatch().draw(pickUser.getTexture(),Gdx.graphics.getWidth()/2 - pickUser.getWidth()/2, Gdx.graphics.getHeight() - (pickUser.getHeight()+ pickUser.getY()), pickUser.getWidth(),pickUser.getHeight());
            boot.getBatch().draw(registerT.getTexture(),Gdx.graphics.getWidth()/2 - registerT.getWidth()/2, Gdx.graphics.getHeight() - (registerT.getHeight()+ registerT.getY()), registerT.getWidth(),registerT.getHeight());
            gifHelper.setScreenBorderGifs(boot.getBatch(), Gdx.graphics.getHeight(),Gdx.graphics.getWidth());
            screenBodyHelper.handleButton(exit,exitInvoker,boot.getBatch());
            stage.act();
            stage.draw();
            boot.getBatch().end();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
