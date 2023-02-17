package com.mygdx.logic.model.user;

import lombok.Data;

@Data
public class Purchase {
    private int itemUserId;
    private int userId;
    private int itemId;

    public Purchase(int userId, int itemId, int price) {
        this.userId = userId;
        this.itemId = itemId;
    }
}