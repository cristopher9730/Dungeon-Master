package com.mygdx.logic.model.entity.player.decorator;

import com.mygdx.logic.model.entity.Entity;
import lombok.Data;

@Data
public abstract class PlayerDecorator extends Entity {
    protected Entity entity;
}
