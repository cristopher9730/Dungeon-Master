package com.mygdx.logic.model.user.possibleItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.builder.builderObjects.ItemBuilder;
import com.mygdx.logic.model.user.Item;

public class Healing extends Item {
    public Healing(ItemBuilder itemBuilder) {
        super(itemBuilder);
        setItemId(2);
        this.texture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/items and trap_animation/flasks/flasks_1_1.gif").read());

    }
}
