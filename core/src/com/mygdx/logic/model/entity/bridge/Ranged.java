package com.mygdx.logic.model.entity.bridge;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.logic.helper.Constants;
import com.mygdx.logic.helper.Helper;
import com.mygdx.logic.model.entity.Entity;
import com.mygdx.logic.model.entity.enemy.Enemy;
import com.mygdx.logic.model.entity.player.Player;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class Ranged implements AttackType {

    private Animation<TextureRegion> proyectileSkin;
    private List<Rectangle> proyectileRectangles;
    private List<Body> proyectiles;

    private String proyectileDirection;

    public Ranged() {
        proyectiles = new ArrayList<>();
        proyectileRectangles = new ArrayList<>();
    }

    @Override
    public float getAttackRange() {
        return 7f;
    }

    @Override
    public float getSpeed() {
        return 7f;
    }

    public Body createAttackBody(float playerPosX, float playerPosY, float width, float height, World world, String direction){

        switch (direction){
            case "U":
                playerPosY += 1;
                break;
            case "D":
                playerPosY -= 1;
                break;
            case "R":
                playerPosX += 1;
                break;
            case "L":
                playerPosX -= 1;
                break;
        }

        return Helper.createBody(
                playerPosX*Constants.PPM,
                playerPosY*Constants.PPM,
                 width,
                 height,
                false,
                world
        );
    }
    @Override
    public void contactBodyValidation(Entity opponent, Entity currentPlayingEntity, World world){
        for(int x = 0; x < proyectileRectangles.size(); x++){
            if(opponent!=null&&Math.abs(proyectileRectangles.get(x).y-opponent.getEntityRectangle().y) < 1.8f
                    && Math.abs(proyectileRectangles.get(x).x-opponent.getEntityRectangle().x) < 1.8f){
                opponent.receiveDamage(currentPlayingEntity.getAttack());
                world.destroyBody(proyectiles.get(x));
                proyectiles.remove(proyectiles.get(x));
                proyectileRectangles.remove(proyectileRectangles.get(x));
            }
        }
    }
}
