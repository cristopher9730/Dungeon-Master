package com.mygdx.ui.command.actions;

import com.mygdx.ui.command.clickables.Clickable;
import com.mygdx.ui.command.ButtonAction;

public class Redirect implements ButtonAction {
    private Clickable clickable;
    public Redirect(Clickable clickable){
        this.clickable = clickable;
    }

    @Override
    public void execute() throws Exception {
        clickable.redirect();
    }
}
