package com.mygdx.logic.model.entity.enemy.possibleEnemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.bridge.Melee;
import com.mygdx.logic.model.entity.enemy.Enemy;

public class Boss extends Enemy {
    public Boss(EntityBuilder entityBuilder) {
        super(entityBuilder);
        this.texture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/6ypvy8-unscreen.gif").read());
        this.attackTexture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/bossEnemyAttacking.gif").read());
        this.entityMode = new Melee();
        this.health = 50;
        this.attack = 15;
    }
}
