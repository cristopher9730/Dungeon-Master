package com.mygdx.logic.model.builder.builderObjects;

import com.mygdx.logic.model.builder.Builder;
import com.mygdx.logic.model.user.Item;
import com.mygdx.logic.model.user.Match;
import com.mygdx.logic.model.user.User;
import lombok.Data;

import java.util.List;

@Data
public class UserBuilder implements Builder {

        private User user;

        private int userId;
        private String name;
        private int coins;
        private List<Item> items;

        public UserBuilder(){
            this.user = new User(this);
        }

    public UserBuilder setUserId(int userId) {
        this.user.setUserId(userId);
        return this;
    }

    public UserBuilder setCoins(int coins) {
        this.user.setCoins(coins);
        return this;
    }

    public UserBuilder setName(String name) {
        this.user.setName(name);
        return this;
    }

        public UserBuilder setItems(List<Item> items) {
            this.user.setItems(items);
            return this;
        }

        public User build(){
            return this.user;
        }
}
