package com.mygdx.ui.command.actions;

import com.mygdx.ui.command.ButtonAction;
import com.mygdx.ui.command.clickables.Clickable;

public class Save implements ButtonAction {
    private Clickable clickable;
    public Save(Clickable clickable){
        this.clickable = clickable;
    }

    @Override
    public void execute() throws Exception {
        clickable.saveObject();
    }
}
