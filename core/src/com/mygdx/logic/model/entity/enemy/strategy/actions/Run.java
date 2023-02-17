package com.mygdx.logic.model.entity.enemy.strategy.actions;

import com.mygdx.logic.model.entity.Entity;

public class Run implements Strategy{
    private Entity entity;
    @Override
    public void execute() {
        entity.run();
    }

    public Run(Entity entity) {
        this.entity = entity;
    }
}
