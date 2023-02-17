package com.mygdx.logic.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GifHelper {
    float elapsed;

    public Animation<TextureRegion> getGif(String gifName){
        switch(gifName){
            case "torch":
             return GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("img/torchGif.gif").read());
            default:
                return null;
        }
    }

    public void setScreenBorderGifs(Batch batch, int screenHeight, int screenWidth){
        elapsed += Gdx.graphics.getDeltaTime();
        for (int x = 0; x <= Gdx.graphics.getWidth(); x += 150){
            batch.draw(getGif("torch").getKeyFrame(elapsed), x, screenHeight-50, Constants.tileSize,Constants.tileSize);
        }
        for (int x = 0; x <= Gdx.graphics.getWidth(); x += 150){
            batch.draw(getGif("torch").getKeyFrame(elapsed), x, 0, Constants.tileSize,Constants.tileSize);
        }
        for (int x = 0; x <= Gdx.graphics.getHeight(); x += 150){
            batch.draw(getGif("torch").getKeyFrame(elapsed), 0, x, Constants.tileSize,Constants.tileSize);
        }
        for (int x = 0; x <= Gdx.graphics.getHeight(); x += 150){
            batch.draw(getGif("torch").getKeyFrame(elapsed), screenWidth - 50, x, Constants.tileSize,Constants.tileSize);
        }
    }
}
