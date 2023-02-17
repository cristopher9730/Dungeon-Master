package com.mygdx.logic.model.user;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.logic.model.builder.builderObjects.ItemBuilder;
import lombok.Data;

@Data
public class Item{

    protected Animation<TextureRegion> texture;
    private int itemId;
    private String name;
    private String storeImage;
    private int price;
    private Body body;
    private float width;
    private float height;
    private Rectangle rectangle;

    public Item(ItemBuilder itemBuilder) {
        this.itemId = itemBuilder.getItemId();
        this.name = itemBuilder.getName();
        this.storeImage = itemBuilder.getStoreImage();
        this.price = itemBuilder.getPrice();
        this.body = itemBuilder.getBody();
        this.height = itemBuilder.getHeight();
        this.width = itemBuilder.getWidth();
        this.rectangle = itemBuilder.getRectangle();
    }
    public Item(){}

    public void update() {
        Vector2 ItemPos = new Vector2(body.getPosition());
        rectangle.setPosition(ItemPos);
        body.setLinearVelocity(0,0);

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return getItemId() == item.getItemId();
    }
}