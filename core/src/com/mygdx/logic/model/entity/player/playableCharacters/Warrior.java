package com.mygdx.logic.model.entity.player.playableCharacters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.player.Player;
import com.mygdx.logic.model.entity.bridge.Melee;

public class Warrior extends Player {
    public Warrior(EntityBuilder entityBuilder) {
        super(entityBuilder);
        texture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/6ypwda-unscreen.gif").read());
        attackTexture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/meleeAttackSkin.gif").read());
        entityMode = new Melee();
    }
}
