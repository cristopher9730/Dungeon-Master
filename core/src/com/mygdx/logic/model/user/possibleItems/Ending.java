package com.mygdx.logic.model.user.possibleItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.builder.builderObjects.ItemBuilder;
import com.mygdx.logic.model.user.Item;

public class Ending extends Item {
    public Ending(ItemBuilder itemBuilder) {
        super(itemBuilder);
        setItemId(3);
        this.texture = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("map/dungeonEscape.gif").read());
    }


}
