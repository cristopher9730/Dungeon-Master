package com.mygdx.logic.model.user.possibleItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.builder.builderObjects.ItemBuilder;
import com.mygdx.logic.model.user.Item;

public class Key extends Item {
    public Key(ItemBuilder itemBuilder) {
        super(itemBuilder);
        setItemId(3);
        this.texture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/items and trap_animation/flasks/key.gif").read());

    }
}
