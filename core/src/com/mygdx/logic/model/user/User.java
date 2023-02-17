package com.mygdx.logic.model.user;


import com.mygdx.logic.model.builder.builderObjects.UserBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {
    private int userId;
    private String name;
    private int coins;
    private List<Item> items;

    public User(UserBuilder builder) {
        this.userId = builder.getUserId();
        this.name= builder.getName();
        this.coins=builder.getCoins();
        this.items=builder.getItems();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getName().equals(user.getName());
    }

}
