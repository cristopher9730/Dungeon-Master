package com.mygdx.logic.model.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.logic.helper.Constants;
import com.mygdx.logic.model.entity.bridge.Ranged;
import com.mygdx.logic.model.entity.enemy.strategy.Context;
import com.mygdx.logic.model.entity.Entity;
import com.mygdx.logic.model.builder.builderObjects.EntityBuilder;
import lombok.Data;

@Data
public class Enemy extends Entity {
    private Context context;
    private boolean isDead;
    private int attackControlTimer;

    public Enemy(EntityBuilder entityBuilder) {
        super(entityBuilder);
    }

    @Override
    public void attack() {
        try {
            updateEntityComponents();
            int Attack_Counter_Limit = this.entityMode instanceof Ranged ? 100:50;
            if (opponent.getHealth()>0){
                attackControlTimer++;
                if(attackControlTimer == Attack_Counter_Limit){
                    setAttackSkinTimer(0);
                    setAttacking(true);
                    body.setLinearVelocity(0, 0);

                    if(this.entityMode instanceof Ranged ){
                        ((Ranged) this.entityMode).getProyectiles().add(((Ranged) this.entityMode).createAttackBody( body.getPosition().x,  body.getPosition().y, 40, 40, world, entityAttackFront));
                        ((Ranged) this.entityMode).getProyectileRectangles().add(new Rectangle(((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).getPosition().x, ((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).getPosition().y, 1.5f, 1.5f));
                        Vector2 proyectilePos = new Vector2(((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).getPosition());
                        Vector2 playerPos = new Vector2(opponent.getBody().getPosition());
                        Vector2 direction = new Vector2();
                        direction.x = (playerPos.x + 40) - (proyectilePos.x + 40);
                        direction.y = (playerPos.y + 40) - (proyectilePos.y + 40);
                        direction.nor();
                        ((Ranged) this.entityMode).getProyectiles().get(((Ranged) this.entityMode).getProyectiles().size()-1).setLinearVelocity(direction.x * 4, direction.y * 4);
                        this.entityMode.contactBodyValidation(opponent,this,world);

                    }else {
                        opponent.receiveDamage(this.attack);
                    }

                    attackControlTimer = 0;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateEntityComponents() throws Exception{
        if(this.entityMode instanceof Ranged && ((Ranged) this.entityMode).getProyectiles().size()>0){
            for (int x = 0; x < ((Ranged) this.entityMode).getProyectileRectangles().size(); x++){
                ((Ranged) this.entityMode).getProyectileRectangles().get(x).set(((Ranged) this.entityMode).getProyectiles().get(x).getPosition().x,((Ranged) this.entityMode).getProyectiles().get(x).getPosition().y, ATTACK_RECTANGLE_SIZE, ATTACK_RECTANGLE_SIZE);
            }
            this.entityMode.contactBodyValidation(opponent,this,world);
        }
    }

    @Override
    public void run() {
        Vector2 enemyPos = new Vector2(body.getPosition());
        Vector2 playerPos = new Vector2(opponent.getBody().getPosition());
        Vector2 direction = new Vector2();
        direction.x = (playerPos.x + 40) - (enemyPos.x + 40);
        direction.y = (playerPos.y + 40) - (enemyPos.y + 40);
        direction.nor();
        body.setLinearVelocity(direction.x * (entityMode.getSpeed() - 3), direction.y * (entityMode.getSpeed() - 3));
        posX = body.getPosition().x * Constants.PPM;
        posY = body.getPosition().y * Constants.PPM;
    }


    @Override
    public void update() {
        setEntityAttackFront();
        if(!this.isDead){
            context = new Context(this);
            Vector2 enemyPos = new Vector2(body.getPosition());
            Vector2 playerPos = new Vector2(opponent.getBody().getPosition());
            context.chooseStrategy(enemyPos,playerPos,this.entityMode);

            attackSkinTimer += Gdx.graphics.getRawDeltaTime();
            if(attackSkinTimer > ATTACK_PERIOD){
                setAttacking(false);
            }
        }
        entityRectangle.set(body.getPosition().x,body.getPosition().y,0.1f,0.1f);
    }

    @Override
    public void setEntityAttackFront(){
        if(opponent.getBody().getPosition().x<=this.getBody().getPosition().x){
            entityAttackFront = "L";
        }else{
            entityAttackFront = "R";
        }
    }
}
