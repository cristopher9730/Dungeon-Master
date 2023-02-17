package com.mygdx.logic.model.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.logic.helper.Constants;
import com.mygdx.logic.helper.GifDecoder;
import com.mygdx.logic.model.entity.Entity;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import com.mygdx.logic.model.entity.bridge.Melee;
import com.mygdx.logic.model.entity.bridge.Ranged;
import com.mygdx.logic.model.entity.bridge.Trap;
import com.mygdx.logic.model.entity.enemy.Enemy;
import com.mygdx.logic.model.entity.enemy.possibleEnemies.FinalBoss;
import com.mygdx.logic.model.entity.player.decorator.PlayerAttackDecorator;
import com.mygdx.logic.model.entity.player.decorator.PlayerSpeedDecorator;
import com.mygdx.logic.model.entity.player.playableCharacters.Mercenary;
import com.mygdx.logic.model.entity.player.playableCharacters.Warrior;
import com.mygdx.logic.model.entity.player.playableCharacters.Wizard;
import com.mygdx.logic.model.user.Item;
import com.mygdx.logic.model.builder.builderObjects.ItemBuilder;
import com.mygdx.logic.model.user.possibleItems.Healing;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player extends Entity {
    private List<Item> itemList;
    private List<Item> mapUsables;
    private Entity currentPlayingEntity;
    private int pickedCoins;
    private boolean hasKey;

    private boolean openedFinalDoor;
    private Rectangle interactionRectangle;

    public Player(EntityBuilder entityBuilder) {
        super(entityBuilder);
        health = 100;
        attack = 10f;
        mapUsables = new ArrayList<>();
        pickedCoins = 0;
        itemList = new ArrayList<>();
    }
    @Override
    public void attack() {
        try{
            updateEntityComponents();
            playerAttackInput();
            handleOpponentKill();
        }
        catch (Exception e){
           throw new RuntimeException();
        }
    }

    private void handleOpponentKill() {
        if(opponent != null && opponent.getHealth()<=0){
                ((Enemy)opponent).setDead(true);
                opponent.getBody().setType(BodyDef.BodyType.StaticBody);
                if(opponent instanceof FinalBoss){
                    opponent.setTexture( GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/" +
                            "key.gif").read()));
                    opponent.setAttackTexture( GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/" +
                            "key.gif").read()));
                    return;
                }
                opponent.setAttacking(false);
                opponent.setTexture(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/dead.gif").read()));
            }
    }

    @Override
    public void updateEntityComponents() throws Exception {
        if(this.entityMode instanceof Ranged && ((Ranged) this.entityMode).getProyectiles().size()>0){
            for (int x = 0; x < ((Ranged) this.entityMode).getProyectileRectangles().size(); x++){
                ((Ranged) this.entityMode).getProyectileRectangles().get(x).set(((Ranged) this.entityMode).getProyectiles().get(x).getPosition().x,((Ranged) this.entityMode).getProyectiles().get(x).getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE);
            }
            entityMode.contactBodyValidation(opponent,currentPlayingEntity,world);
        }
        if(this.entityMode instanceof Trap &&  ((Trap) this.entityMode).getTraps().size()>0 && opponent!=null){
            for (int x = 0; x < ((Trap) this.entityMode).getTrapDamageRectangles().size(); x++){
                ((Trap) this.entityMode).getTrapDamageRectangles().get(x).set(((Trap) this.entityMode).getTraps().get(x).getPosition().x,((Trap) this.entityMode).getTraps().get(x).getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE);
            }
            entityMode.contactBodyValidation(opponent,currentPlayingEntity,world);
        }
    }

    private void playerAttackInput() {
        interactionRectangle = new Rectangle(body.getPosition().x, body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE);
        switch (entityAttackFront){
            case"U":
                if(this.entityMode instanceof Melee) {
                    setAttacking(true);
                    setAttackSkinTimer(0);
                    if(opponent!=null){
                        if(Math.abs((opponent.getEntityRectangle().getY()+0.1)-interactionRectangle.y)<entityMode.getAttackRange() && (interactionRectangle.x>opponent.getEntityRectangle().x-1.5 && opponent.getEntityRectangle().x>interactionRectangle.x-1.4)){
                            opponent.receiveDamage(currentPlayingEntity.getAttack());
                        }
                    }
                }
                else if (this.entityMode instanceof Ranged){
                    ((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).setLinearVelocity(0, ((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).getPosition().y+4);
                    ((Ranged) this.entityMode).getProyectileRectangles().add(new Rectangle(body.getPosition().x,body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE));
                }
                else if(this.entityMode instanceof Trap){
                    ((Trap) this.entityMode).getTraps().add(((Trap) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, entityAttackFront));
                    ((Trap) this.entityMode).getTrapDamageRectangles().add(new Rectangle(body.getPosition().x,body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE));
                }
                pickUpKey();
                handleUsableItems();
                break;
            case"D":
                if(this.entityMode instanceof Melee) {
                    setAttacking(true);
                    setAttackSkinTimer(0);
                    if (opponent != null) {
                        if (Math.abs((opponent.getEntityRectangle().getY() - 0.2) - interactionRectangle.y) < entityMode.getAttackRange() && (interactionRectangle.x > opponent.getEntityRectangle().x - 1.5 && opponent.getEntityRectangle().x > interactionRectangle.x - 1.4)) {
                            opponent.receiveDamage(currentPlayingEntity.getAttack());
                        }
                    }
                }
                else if (this.entityMode instanceof Ranged){
                    ((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).setLinearVelocity(0,-1*(((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).getPosition().y-4));
                    ((Ranged) this.entityMode).getProyectileRectangles().add(new Rectangle(body.getPosition().x,body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE));
                }
                else if(this.entityMode instanceof Trap){
                    ((Trap) this.entityMode).getTraps().add(((Trap) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, entityAttackFront));
                    ((Trap) this.entityMode).getTrapDamageRectangles().add(new Rectangle(body.getPosition().x,body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE));
                }
                pickUpKey();
                handleUsableItems();
                break;
            case"L":
                if(this.entityMode instanceof Melee){
                    setAttacking(true);
                    setAttackSkinTimer(0);
                    if(opponent != null){
                        if(Math.abs((opponent.getEntityRectangle().getX()+ 0.2)-interactionRectangle.x)<entityMode.getAttackRange() && (interactionRectangle.y>opponent.getEntityRectangle().y-1.5 && opponent.getEntityRectangle().y>interactionRectangle.y-1.4)){
                            opponent.receiveDamage(currentPlayingEntity.getAttack());
                        }
                    }
                }
                else if (this.entityMode instanceof Ranged){
                    ((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).setLinearVelocity(-1*(((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).getPosition().x-4),0);
                    ((Ranged) this.entityMode).getProyectileRectangles().add(new Rectangle(body.getPosition().x,body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE));
                }
                else if(this.entityMode instanceof Trap){
                    ((Trap) this.entityMode).getTraps().add(((Trap) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, entityAttackFront));
                    ((Trap) this.entityMode).getTrapDamageRectangles().add(new Rectangle(body.getPosition().x,body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE));
                }
                pickUpKey();
                handleUsableItems();
                break;
            case"R":
                if(this.entityMode instanceof Melee){
                    setAttacking(true);
                    setAttackSkinTimer(0);
                    if(opponent!=null){
                        if(Math.abs((opponent.getEntityRectangle().getX()- 0.2)-interactionRectangle.x)<entityMode.getAttackRange() && (interactionRectangle.y>opponent.getEntityRectangle().y-1.5 && opponent.getEntityRectangle().y>interactionRectangle.y-1.4)){
                            opponent.receiveDamage(currentPlayingEntity.getAttack());
                        }
                    }
                }else if (this.entityMode instanceof Ranged){
                    ((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).setLinearVelocity(((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).getPosition().x+4,0);
                    ((Ranged) this.entityMode).getProyectileRectangles().add(new Rectangle(body.getPosition().x,body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE));
                }
                else if(this.entityMode instanceof Trap){
                    ((Trap) this.entityMode).getTraps().add(((Trap) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, entityAttackFront));
                    ((Trap) this.entityMode).getTrapDamageRectangles().add(new Rectangle(body.getPosition().x,body.getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE));
                }
                pickUpKey();
                handleUsableItems();
                break;
        }
    }

    private void pickUpKey() {
        if(opponent != null && ((Enemy)opponent).isDead()
                && opponent instanceof FinalBoss
                && (interactionRectangle.y-opponent.getEntityRectangle().y) < 1.8f
                && Math.abs(interactionRectangle.x - opponent.getEntityRectangle().x) < 1.8f){
            world.destroyBody(opponent.getBody());
            setHasKey(true);
        }
    }

    @Override
    public void run() {
        try{
            checkUserMovementInput();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDecorators() {
        if (itemList.contains(new ItemBuilder().setItemId(6).build())) {
            currentPlayingEntity = new PlayerSpeedDecorator(currentPlayingEntity);
        }
        if((itemList.contains(new ItemBuilder().setItemId(5).build()) && this instanceof Warrior) || (itemList.contains(new ItemBuilder().setItemId(4).build()) && this instanceof Wizard) || (itemList.contains(new ItemBuilder().setItemId(7).build()) && this instanceof Mercenary)){
            currentPlayingEntity = new PlayerAttackDecorator(currentPlayingEntity);
        }
    }

    @Override
    public void update() {
        posX = body.getPosition().x * Constants.PPM;
        posY = body.getPosition().y * Constants.PPM;
        currentPlayingEntity = this;
        setDecorators();
        run();
        attackSkinTimer += Gdx.graphics.getRawDeltaTime();
        if(attackSkinTimer > ATTACK_PERIOD){
            setAttacking(false);
        }
    }

    private void checkUserMovementInput() throws Exception {
        currentPlayingEntity.setVelX(0);
        currentPlayingEntity.setVelY(0);
        entityAttackFront = "";
        if(this.health>0){
            setEntityAttackFront();
            if (Gdx.input.isKeyPressed(Input.Keys.D))
                currentPlayingEntity.setVelX(1);
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                currentPlayingEntity.setVelX(-1);
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                currentPlayingEntity.setVelY(1);
            if (Gdx.input.isKeyPressed(Input.Keys.S)){
                currentPlayingEntity.setVelY(-1);
            }
            currentPlayingEntity.getBody().setLinearVelocity(currentPlayingEntity.getVelX() * currentPlayingEntity.getSpeed(), currentPlayingEntity.getVelY() * currentPlayingEntity.getSpeed());
        }else{
            setTexture(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/enemyAssets/dead.gif").read()));
        }
        entityRectangle.setPosition(body.getPosition());
    }

    @Override
    public void setEntityAttackFront(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            entityAttackFront = "R";
            if(this.entityMode instanceof Ranged){
                ((Ranged)entityMode).setProyectileDirection("R");
                ((Ranged) this.entityMode).getProyectiles().add(((Ranged) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, ((Ranged)entityMode).getProyectileDirection()));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            entityAttackFront = "L";
            if(this.entityMode instanceof Ranged){
                ((Ranged)entityMode).setProyectileDirection("L");
                ((Ranged) this.entityMode).getProyectiles().add(((Ranged) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, ((Ranged)entityMode).getProyectileDirection()));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            entityAttackFront = "U";
            if(this.entityMode instanceof Ranged){
                ((Ranged)entityMode).setProyectileDirection("U");
                ((Ranged) this.entityMode).getProyectiles().add(((Ranged) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, ((Ranged)entityMode).getProyectileDirection()));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            entityAttackFront = "D";
            if(this.entityMode instanceof Ranged){
                ((Ranged)entityMode).setProyectileDirection("D");
                ((Ranged) this.entityMode).getProyectiles().add(((Ranged) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, ((Ranged)entityMode).getProyectileDirection()));
            }
        }
        currentPlayingEntity.attack();
        if(opponent!=null && this.entityMode instanceof Ranged && ((Ranged) this.entityMode).getProyectiles().size()>0 && currentPlayingEntity.getEntityMode() instanceof Ranged){
            ((Ranged)currentPlayingEntity.getEntityMode()).contactBodyValidation(opponent,currentPlayingEntity,world);
        }
    }
    private void handleUsableItems() {
        currentPlayingEntity = this;
        for (int x = 0; x < mapUsables.size(); x++){
            if (Math.abs((mapUsables.get(x).getRectangle().getX() - 0.2) - interactionRectangle.x) < entityMode.getAttackRange() && (interactionRectangle.y > mapUsables.get(x).getRectangle().y - 1.5 && mapUsables.get(x).getRectangle().y > interactionRectangle.y - 1.4)) {
                if(interactionRectangle.overlaps(mapUsables.get(x).getRectangle()) && mapUsables.get(x).getItemId() == 1 ){
                    mapUsables.get(x).setTexture( GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets2d/items and trap_animation/chest/chest_open_3.gif").read()));
                    mapUsables.get(x).getBody().setType(BodyDef.BodyType.StaticBody);
                    pickedCoins += 5;
                } else if (interactionRectangle.overlaps(mapUsables.get(x).getRectangle()) && mapUsables.get(x).getItemId() == 2 ) {
                    currentPlayingEntity.receiveHealth(15);

                }
             else if (interactionRectangle.overlaps(mapUsables.get(x).getRectangle()) && mapUsables.get(x).getItemId() == 3 && hasKey ) {
                 setOpenedFinalDoor(true);
            }
                if(mapUsables.get(x) instanceof Healing){
                    world.destroyBody(mapUsables.get(x).getBody());
                    mapUsables.remove(mapUsables.get(x));
                }
            }
        }
    }
}