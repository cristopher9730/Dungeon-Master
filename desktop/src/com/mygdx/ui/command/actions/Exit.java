package com.mygdx.ui.command.actions;

import com.mygdx.ui.command.clickables.Clickable;
import com.mygdx.ui.command.ButtonAction;

public class Exit implements ButtonAction {

    private Clickable clickable;

    public Exit(Clickable clickable) {
        this.clickable = clickable;
    }

    @Override
    public void execute() throws Exception {
        clickable.exit();
    }
}
