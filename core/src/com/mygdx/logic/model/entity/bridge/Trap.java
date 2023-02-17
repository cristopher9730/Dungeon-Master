package com.mygdx.logic.model.entity.bridge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.logic.helper.Constants;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.helper.Helper;
import com.mygdx.logic.model.entity.Entity;
import com.mygdx.logic.model.entity.enemy.Enemy;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Trap implements AttackType {
    private Animation<TextureRegion> trapSkin;

    private List<Body> traps;

    private List<Rectangle> trapDamageRectangles;

    public Trap() {
        trapSkin = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/peaks.gif").read());
        traps = new ArrayList<>();
        trapDamageRectangles = new ArrayList<>();
    }

    @Override
    public float getAttackRange() {
        return 6;
    }

    @Override
    public float getSpeed() {
        return 13;
    }

    @Override
    public Body createAttackBody(float playerPosX, float playerPosY, float width, float height, World world, String direction) {

        switch (direction) {
            case "U":
                playerPosY += 2;
                break;
            case "D":
                playerPosY -= 2;
                break;
            case "R":
                playerPosX += 2;
                break;
            case "L":
                playerPosX -= 2;
                break;
        }
        return Helper.createBody(
                playerPosX * Constants.PPM,
                playerPosY * Constants.PPM,
                width,
                height,
                false,
                world
        );
    }

    @Override
    public void contactBodyValidation(Entity opponent, Entity currentPlayingEntity, World world){
        for(int x = 0; x < trapDamageRectangles.size(); x++){
            if(Math.abs(trapDamageRectangles.get(x).y-opponent.getEntityRectangle().y) < 1.8f
                    && Math.abs(trapDamageRectangles.get(x).x - opponent.getEntityRectangle().x) < 1.8f){
                opponent.receiveDamage(currentPlayingEntity.getAttack());
                world.destroyBody(traps.get(x));
                traps.remove(traps.get(x));
                trapDamageRectangles.remove(trapDamageRectangles.get(x));
            }
        }
    }
}
