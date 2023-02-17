package com.mygdx.ui.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.logic.model.user.Item;
import com.mygdx.logic.model.user.Purchase;
import com.mygdx.logic.service.facade.ServiceStore;
import com.mygdx.logic.service.iterator.IteratorHelper;
import com.mygdx.ui.Boot;
import com.mygdx.ui.screens.MainScreen;

import java.util.ArrayList;
import java.util.List;

public class StoreScreenHelper extends IteratorHelper {
    public List<Actor> getSelectableItems(List<Item> ownedItems, List<Item> itemList, Boot boot, Stage stage) {
        try{
            List<Actor> arrayList = new ArrayList<>();
            int initialX = 100;
            int initialY = 400;

            this.iterator = getIterator(itemList);

            while(iterator.hasNext()){
                Item item = (Item)iterator.next();
                if (iterator.getIndex()%5==0){
                    initialX = 100;
                    initialY -= 150;
                }
                final Texture texture1 = new Texture(item.getStoreImage());
                com.badlogic.gdx.scenes.scene2d.ui.Image item1 = new com.badlogic.gdx.scenes.scene2d.ui.Image(texture1);
                item1.addListener(new ClickListener(){
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        try {
                            if (item.getPrice()<=boot.getLoggedUser().getCoins()){
                                Purchase purchase = new Purchase(boot.getLoggedUser().getUserId(),item.getItemId(), item.getPrice());
                                ServiceStore.getInstance().savePurchase(purchase);
                                boot.getLoggedUser().setCoins(boot.getLoggedUser().getCoins()-item.getPrice());
                                ServiceStore.getInstance().saveUser(boot.getLoggedUser());
                                boot.setScreen(new MainScreen(boot));
                                return true;
                            }else{
                                Dialog dialog =  new Dialog("Error", boot.getSkin());
                                Table table = new Table();
                                table.add(new Label("Not enough coins", boot.getSkin()));
                                dialog.getContentTable().add(table);
                                dialog.button("ok");
                                dialog.show(stage);
                                return false;
                            }
                        } catch (Exception e){
                            System.out.println(e.getMessage());
                            return false;
                        }
                    }
                });
                com.badlogic.gdx.scenes.scene2d.ui.Label label = new Label(item.getName(),boot.getSkin());
                com.badlogic.gdx.scenes.scene2d.ui.Label label2 = new Label(String.valueOf(item.getPrice()),boot.getSkin());
                item1.setBounds(initialX, initialY, 60, 60);
                label.setSize(60,35);
                boot.getSkin().getFont("default-font").getData().setScale(0.8f,0.8f);
                label.setPosition(initialX,initialY-40);
                label2.setPosition(initialX,initialY-60);
                arrayList.add(item1);
                arrayList.add(label);
                arrayList.add(label2);
                initialX += 100;
                if(ownedItems.contains(item)){
                    item1.setColor(Color.GRAY);
                    item1.setTouchable(Touchable.disabled);
                }
            }

            return arrayList;

        }catch (Exception e){
            return null;
        }
    }
}
