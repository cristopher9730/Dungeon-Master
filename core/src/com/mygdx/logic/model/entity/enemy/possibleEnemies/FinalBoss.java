package com.mygdx.logic.model.entity.enemy.possibleEnemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.bridge.Melee;
import com.mygdx.logic.model.entity.bridge.Ranged;
import com.mygdx.logic.model.entity.enemy.Enemy;

public class FinalBoss extends Enemy {
    public FinalBoss(EntityBuilder entityBuilder) {
        super(entityBuilder);
        this.texture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/6ypw2q-unscreen.gif").read());
        this.attackTexture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/6ypw2q-unscreen.gif").read());
        this.entityMode = new Ranged();
        this.health = 100;
        this.attack = 25;
        ((Ranged)entityMode).setProyectileSkin(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/flame.gif").read()));
    }
}
