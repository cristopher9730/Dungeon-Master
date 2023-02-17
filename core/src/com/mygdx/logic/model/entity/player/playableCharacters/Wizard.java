package com.mygdx.logic.model.entity.player.playableCharacters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.bridge.Ranged;
import com.mygdx.logic.model.entity.player.Player;

public class Wizard extends Player {
    public Wizard(EntityBuilder entityBuilder) {
        super(entityBuilder);
        texture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/6ypw6a-unscreen.gif").read());
        attackTexture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/6ypw6a-unscreen.gif").read());
        entityMode = new Ranged();
        ((Ranged)entityMode).setProyectileSkin(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/redFlame.gif").read()));
    }
}
