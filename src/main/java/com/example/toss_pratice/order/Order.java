package com.example.toss_pratice.order;

import java.util.UUID;

public class Order {
    private final UUID id;
    private final UUID userId;
    private final ItemName orderItemName;
    private final Price orderPrice;

    public Order(UUID id, UUID userId, ItemName orderItemName, Price orderPrice) {
        this.id = id;
        this.userId = userId;
        this.orderItemName = orderItemName;
        this.orderPrice = orderPrice;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public ItemName getOrderItemName() {
        return orderItemName;
    }

    public Price getOrderPrice() {
        return orderPrice;
    }
}
