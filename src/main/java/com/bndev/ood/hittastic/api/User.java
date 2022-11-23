package com.bndev.ood.hittastic.api;

import de.vandermeer.asciitable.AsciiTable;

import javax.security.auth.login.FailedLoginException;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable, Storable {
    public static StorableHashmap<User> all = new StorableHashmap<>("users");

    public String username;
    public String password;
    public boolean isAdmin;
    public List<Order> orders;

    public static void Reset() {
        User.all = new StorableHashmap<User>("users");
    }

    public String id() {
        return username;
    }

    private User(String username, String password, boolean isAdmin, List<Order> orders) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.orders = orders;
    }

    public User Update(String username, String password, boolean isAdmin) {
        if (User.all.containsKey(username) && !Objects.equals(this.username, username)) {
            throw new RuntimeException("The new username already exists");
        }
        User.all.remove(this.username, this);

        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;

        User.all.put(username, this);
        User.Save();
        return this;
    }

    public Order createOrder(Song song, int qty) {
        Order order = Order.createOrder(this, song, qty);
        this.orders.add(order);
        song.sales += qty;
        User.Save();
        Song.Save();
        return order;
    }

    public AsciiTable generateOrderTable() {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Date", "Time", "Artist", "Title", "Qty");
        this.orders.forEach(order -> {
            at.addRule();
            at.addRow(order.timeOfOrder.toLocalDate(), order.timeOfOrder.format(DateTimeFormatter.ofPattern("HH:mm:ss")), order.song.artist, order.song.title, order.qty);
        });
        at.addRule();
        return at;
    }

    public static User getAndUpdate(String username, String newUsername, String password, boolean isAdmin) {
        if (!User.all.containsKey(username)) {
            throw new RuntimeException("User not found");
        }
        return User.all.get(username).Update(newUsername, password, isAdmin);
    }

    public static User CreateUser(String username, String password) {
        if (User.all.containsKey(username)) {
            throw new RuntimeException("This username already exists");
        }

        User user = new User(username, password, false, new ArrayList<>());
        User.all.put(username, user);
        User.Save();
        return user;
    }

    public static User CreateAdmin(String username, String password) {
        if (User.all.containsKey(username)) {
            throw new RuntimeException("This username already exists");
        }
        User user = new User(username, password, true, new ArrayList<>());
        User.all.put(username, user);
        User.Save();
        return user;
    }

    public static Session Login(String username, String password) throws FailedLoginException {
        if (!User.all.containsKey(username) || !Objects.equals(User.all.get(username).password, password)) {
            throw new FailedLoginException();
        } else {
            return new Session(User.all.get(username));
        }
    }

    public static void Save() {
        User.all.save();
    }

    public static void Load() {
        User.all.load();
    }

}
