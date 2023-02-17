package com.mygdx.ui.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.logic.model.builder.Director;
import com.mygdx.ui.Boot;

public class CustomScreen extends ScreenAdapter {
    protected Boot boot;

    protected Director director;

    public CustomScreen(Boot boot){
        this.boot = boot;
        this.director = new Director();
    }
}
