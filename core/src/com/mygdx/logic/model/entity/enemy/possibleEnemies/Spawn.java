package com.mygdx.logic.model.entity.enemy.possibleEnemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.bridge.Melee;
import com.mygdx.logic.model.entity.bridge.Ranged;
import com.mygdx.logic.model.entity.enemy.Enemy;

public class Spawn extends Enemy {
    public Spawn(EntityBuilder entityBuilder) {
        super(entityBuilder);
        this.texture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/6ypvth-unscreen.gif").read());
        this.attackTexture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/AttackAnimations/enemyAttacking.gif").read());
        entityMode = new Melee();
        this.health = 30;
        this.attack = 5;

    }
}
