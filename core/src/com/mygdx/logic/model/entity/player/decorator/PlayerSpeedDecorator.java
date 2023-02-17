package com.mygdx.logic.model.entity.player.decorator;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.logic.model.entity.Entity;
import com.mygdx.logic.model.entity.bridge.AttackType;

public class PlayerSpeedDecorator extends PlayerDecorator {
    public PlayerSpeedDecorator(Entity entity) {
        this.entity = entity;
    }

    @Override
    public float getSpeed(){
        return this.entity.getEntityMode().getSpeed()+10;
    }

    @Override
    public void updateEntityComponents() throws Exception {
        this.entity.updateEntityComponents();
    }

    @Override
    public void setEntityAttackFront() {
        this.entity.setEntityAttackFront();
    }

    @Override
    public float getAttack(){
        return this.entity.getAttack();
    }

    @Override
    public void attack() {
        this.entity.attack();
    }

    @Override
    public Body getBody(){
        return this.entity.getBody();
    }

    @Override
    public AttackType getEntityMode(){
        return this.entity.getEntityMode();
    }

    @Override
    public void run() {
        this.entity.run();
    }

    @Override
    public void update() {
        this.entity.update();
    }
}
