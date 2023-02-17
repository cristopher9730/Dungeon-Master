package com.mygdx.logic.model.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.bridge.AttackType;
import lombok.Data;

import java.util.List;

@Data
public abstract class Entity {

    protected AttackType entityMode;

    protected Animation<TextureRegion> texture;
    protected Animation<TextureRegion> attackTexture;

    protected boolean isAttacking;
    protected float attackSkinTimer;
    protected float posX, posY, velX, velY, attack, width,height;
    protected int health;

    protected Body body;
    protected Entity opponent;
    protected Rectangle entityRectangle;

    protected World world;
    protected String entityAttackFront;

    protected final float ATTACK_PERIOD = 0.5f;
    protected final float ATTACK_RECTANGLE_SIZE = 2f;

    public Entity(EntityBuilder entityBuilder){
        this.width = entityBuilder.getWidth();
        this.height = entityBuilder.getHeight();
        this.body = entityBuilder.getBody();
        this.entityRectangle = entityBuilder.getEntityRectangle();
        this.opponent = entityBuilder.getOpponent();
        this.world = entityBuilder.getWorld();
    }
    public Entity(){}

    public abstract void attack();

    public abstract void run();

    public abstract void update();

    public void receiveDamage(float dmgTaken){
        this.health -= dmgTaken;
        if(this.health < 0){
            this.health = 0;
        }
    }
    public void receiveHealth(float health){
        this.health += health;
        if(this.health > 100){
            this.health = 100;
        }
    }
    public float getSpeed(){
        return this.entityMode.getSpeed();
    }
    public abstract void updateEntityComponents() throws Exception;

    public abstract void setEntityAttackFront();
}
