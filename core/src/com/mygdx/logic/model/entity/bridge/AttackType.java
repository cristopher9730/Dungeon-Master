package com.mygdx.logic.model.entity.bridge;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.logic.model.entity.Entity;

import java.util.List;

public interface AttackType {
    float getAttackRange();
    float getSpeed();
    Body createAttackBody(float playerPosX, float playerPosY, float width, float height, World world, String direction) throws Exception;
    void contactBodyValidation(Entity opponent, Entity currentPlayingEntity, World world) throws Exception;
}
