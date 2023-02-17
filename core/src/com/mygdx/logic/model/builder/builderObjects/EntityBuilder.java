package com.mygdx.logic.model.builder.builderObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.logic.model.builder.Builder;
import com.mygdx.logic.model.entity.Entity;
import com.mygdx.logic.model.entity.enemy.Enemy;
import com.mygdx.logic.model.entity.enemy.possibleEnemies.Boss;
import com.mygdx.logic.model.entity.enemy.possibleEnemies.FinalBoss;
import com.mygdx.logic.model.entity.enemy.possibleEnemies.Spawn;
import com.mygdx.logic.model.entity.player.playableCharacters.Mercenary;
import com.mygdx.logic.model.entity.player.playableCharacters.Warrior;
import com.mygdx.logic.model.entity.player.playableCharacters.Wizard;
import lombok.Data;

@Data
public class EntityBuilder implements Builder {
    private Entity entity;

    private float width;

    private float height;

    private Body body;

    private Rectangle entityRectangle;

    private Entity opponent;

    private World world;

    public EntityBuilder(String subClass){
        switch (subClass) {
            case "spawn":
                this.entity = new Spawn(this);
                break;
            case "boss":
                this.entity = new Boss(this);
                break;
            case "finalBoss":
                this.entity = new FinalBoss(this);
                break;
            case "playerWarrior":
                this.entity = new Warrior(this);
                break;
            case "playerWizard":
                this.entity = new Wizard(this);
                break;
            case "playerMercenary":
                this.entity = new Mercenary(this);
                break;
        }
    }

    public EntityBuilder setWidth(float width) {
        this.entity.setWidth(width);
        return this;
    }

    public EntityBuilder setHeight(float height) {
        this.entity.setHeight(height);
        return this;
    }
    public EntityBuilder setBody(Body body) {
        this.entity.setBody(body);
        return this;
    }
    public EntityBuilder setEntityRectangle(Rectangle rectangle) {
        this.entity.setEntityRectangle(rectangle);
        return this;
    }

    public EntityBuilder setOpponent(Entity opponent) {
        this.entity.setOpponent(opponent);
        return this;
    }

    public EntityBuilder setWorld(World world) {
        this.entity.setWorld(world);
        return this;
    }

    @Override
    public Entity build() {
          return this.entity;
        }
}

