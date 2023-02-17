package com.mygdx.logic.model.builder.builderObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.logic.model.builder.Builder;
import com.mygdx.logic.model.user.Item;
import com.mygdx.logic.model.user.possibleItems.Chest;
import com.mygdx.logic.model.user.possibleItems.Ending;
import com.mygdx.logic.model.user.possibleItems.Healing;
import lombok.Data;

@Data
public class ItemBuilder implements Builder {
    private Item item;

    private int itemId;

    private String name;

    private String storeImage;

    private int price;

    private Body body;
    private float width;

    private float height;

    private Rectangle rectangle;

    public ItemBuilder() {
        this.item = new Item(this);
    }

    public ItemBuilder setMapItemSubclass(String mapItemSubclass){
        switch (mapItemSubclass) {
            case "Healing":
                this.item = new Healing(this);
                break;
            case "Chest":
                this.item = new Chest(this);
                break;
            case "Ending":
                this.item = new Ending(this);
                break;
        }
        return this;
    }

    public ItemBuilder setItemId(int itemId) {
        this.item.setItemId(itemId);
        return this;
    }
    public ItemBuilder setName(String name) {
        this.item.setName(name);
        return this;
    }

    public ItemBuilder setStoreImage(String storeImage){
        this.item.setStoreImage(storeImage);
        return this;
    }

    public ItemBuilder setPrice(int price){
        this.item.setPrice(price);
        return this;
    }
    public ItemBuilder setBody(Body body) {
        this.item.setBody(body);
        return this;
    }

    public ItemBuilder setWidth(float width) {
        this.item.setWidth(width);
        return this;
    }
    public ItemBuilder setHeight(float height) {
        this.item.setWidth(width);
        return this;
    }

    public ItemBuilder setRectangle(Rectangle rectangle) {
        this.item.setRectangle(rectangle);
        return this;
    }
    
    @Override
    public Item build() {
        return this.item;
    }
}
