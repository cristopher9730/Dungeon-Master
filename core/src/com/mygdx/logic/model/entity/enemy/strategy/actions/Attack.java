package com.mygdx.logic.model.entity.enemy.strategy.actions;

import com.mygdx.logic.model.entity.Entity;

public class Attack implements Strategy{
    private Entity entity;
    @Override
    public void execute() {
        entity.attack();
    }

    public Attack(Entity entity) {
        this.entity = entity;
    }
}
