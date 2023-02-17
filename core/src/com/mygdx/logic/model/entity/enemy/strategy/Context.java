package com.mygdx.logic.model.entity.enemy.strategy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.logic.model.entity.enemy.strategy.actions.Attack;
import com.mygdx.logic.model.entity.enemy.strategy.actions.Run;
import com.mygdx.logic.model.entity.enemy.strategy.actions.Strategy;
import com.mygdx.logic.model.entity.Entity;
import com.mygdx.logic.model.entity.bridge.AttackType;
import lombok.Data;

@Data
public class Context {
    private Strategy strategy;
    private Entity entity;

    public Context( Entity entity) {
        this.entity = entity;
    }

    public void chooseStrategy(Vector2 enemyPos, Vector2 playerPos, AttackType entityMode) {

        float resPosX = Math.abs(playerPos.x - enemyPos.x);
        float resPosY = Math.abs(playerPos.y - enemyPos.y);

        if(resPosX>entityMode.getAttackRange() || resPosY>entityMode.getAttackRange()){
            this.strategy = new Run(this.entity);
            executeStrategy();
        }else{
            this.strategy = new Attack(this.entity);
            executeStrategy();
        }
    }
    private void executeStrategy(){
        strategy.execute();
    }
}
