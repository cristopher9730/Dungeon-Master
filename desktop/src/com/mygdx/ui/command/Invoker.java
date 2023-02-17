package com.mygdx.ui.command;

import com.mygdx.logic.service.iterator.ObjectIterator;
import com.mygdx.logic.service.iterator.IteratorHelper;

import java.util.ArrayList;
import java.util.List;

public class Invoker extends IteratorHelper {

    List<ButtonAction> actions;

    public Invoker() {
        this.actions = new ArrayList<>();
    }

    public void takeAction(ButtonAction buttonAction){
        if(!actions.contains(buttonAction)) actions.add(buttonAction);
    }

    public void removeAction(ButtonAction buttonAction){
        actions.remove(buttonAction);
    }

    public void placeActions() throws Exception{
        this.iterator = new ObjectIterator(actions);
        while (iterator.hasNext()){
            ButtonAction action = (ButtonAction)iterator.next();
            action.execute();
        }
    }
}
