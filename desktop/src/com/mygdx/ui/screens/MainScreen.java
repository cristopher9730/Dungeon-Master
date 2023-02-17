package com.mygdx.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.logic.helper.GifHelper;
import com.mygdx.logic.service.facade.ServiceStore;
import com.mygdx.ui.helper.ScreenBodyHelper;
import com.mygdx.ui.command.clickables.ClickableLabel;
import com.mygdx.ui.Boot;
import com.mygdx.ui.command.actions.Exit;
import com.mygdx.ui.command.Invoker;
import com.mygdx.ui.command.actions.Redirect;


public class MainScreen extends CustomScreen {

    private ScreenBodyHelper screenBodyHelper;

    private ClickableLabel title;

    private ClickableLabel createWorldRedirect;

    private ClickableLabel play;
    private ClickableLabel store;

    private ClickableLabel exit;

    private GifHelper gifHelper;

    private Invoker createInvoker;
    private Invoker playInvoker;
    private Invoker storeInvoker;
    private Invoker exitInvoker;

    public MainScreen(Boot boot){
        super(boot);
        Gdx.graphics.setWindowedMode(500,650);
        this.gifHelper = new GifHelper();
        this.createInvoker = new Invoker();
        this.playInvoker = new Invoker();
        this.storeInvoker = new Invoker();
        this.exitInvoker = new Invoker();
        this.title = new ClickableLabel(400,100,Gdx.graphics.getWidth()/2-200,490,new Texture("menuTitle.png"), new Texture("menuTitle.png"));
        this.createWorldRedirect = new ClickableLabel(280,60,Gdx.graphics.getWidth()/2-140,175,new Texture("menuOptions/Create-a-Map.png"),new Texture("menuOptions/Create-a-Map_2.png"));
        this.play = new ClickableLabel(100,50,Gdx.graphics.getWidth()/2-50,270,new Texture("menuOptions/Play.png"), new Texture("menuOptions/Play2.png"));
        this.store = new ClickableLabel(150,50,Gdx.graphics.getWidth()/2-70,370,new Texture("menuOptions/Store.png"), new Texture("menuOptions/Store2.png"));
        this.exit = new ClickableLabel(100,50,Gdx.graphics.getWidth()/2-50,500,new Texture("menuOptions/Exit.png"),new Texture("menuOptions/Exit_2.png"));
        this.screenBodyHelper = new ScreenBodyHelper();
    }

    @Override
    public void show() {
        try{
            boot.setLoggedUser(ServiceStore.getInstance().getUsers().stream().filter(user -> user.getName().equals(boot.getLoggedUser().getName())).findFirst().orElse(null));

            createWorldRedirect.setBoot(boot);
            createWorldRedirect.setRedirectScreen("worldCreator");
            Redirect redirectWorldScreen = new Redirect(createWorldRedirect);
            createInvoker.takeAction(redirectWorldScreen);

            play.setBoot(boot);
            play.setRedirectScreen("pickScreen");
            Redirect redirectPlayScreen = new Redirect(play);
            playInvoker.takeAction(redirectPlayScreen);

            store.setBoot(boot);
            store.setRedirectScreen("storeScreen");
            Redirect redirectStoreScreen = new Redirect(store);
            storeInvoker.takeAction(redirectStoreScreen);

            exit.setBoot(boot);
            exit.setButtonAction("exit");
            Exit exitAction = new Exit(exit);
            exitInvoker.takeAction(exitAction);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        boot.getBatch().begin();

        boot.getSprite().draw(boot.getBatch());
        boot.getBatch().draw(title.getTexture(),Gdx.graphics.getWidth()/2 - title.getWidth()/2, title.getY(), title.getWidth(),title.getHeight());
        gifHelper.setScreenBorderGifs(boot.getBatch(), Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
        screenBodyHelper.handleButton(createWorldRedirect,createInvoker,boot.getBatch());
        screenBodyHelper.handleButton(play,playInvoker,boot.getBatch());
        screenBodyHelper.handleButton(store,storeInvoker,boot.getBatch());
        screenBodyHelper.handleButton(exit,exitInvoker,boot.getBatch());
        boot.getBatch().end();

    }

}
