package com.bndev.ood.hittastic.api;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order implements Serializable {
    UUID id;
    User user;
    public LocalDateTime timeOfOrder;
    public Song song;

    public int qty;

    public Order(UUID id, User user, LocalDateTime timeOfOrder, Song song, int qty) {
        this.id = id;
        this.user = user;
        this.timeOfOrder = timeOfOrder;
        this.song = song;
        this.qty = qty;
    }

    public static Order createOrder(User user, Song song, int qty) {
        return new Order(UUID.randomUUID(), user, LocalDateTime.now(), song, qty);
    }


}
