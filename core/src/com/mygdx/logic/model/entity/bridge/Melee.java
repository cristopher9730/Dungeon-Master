package com.mygdx.logic.model.entity.bridge;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.logic.model.entity.Entity;


public class Melee implements AttackType {
    @Override
    public float getAttackRange() {
        return 1.8f;
    }

    @Override
    public float getSpeed() {
        return 10f;
    }

    @Override
    public Body createAttackBody(float playerPosX, float playerPosY, float width, float height, World world, String direction) throws Exception {
        throw new Exception("Method Not Allowed");
    }

    @Override
    public void contactBodyValidation(Entity opponent, Entity currentPlayingEntity, World world) throws Exception {
        throw new Exception("Method Not Allowed");
    }

}
