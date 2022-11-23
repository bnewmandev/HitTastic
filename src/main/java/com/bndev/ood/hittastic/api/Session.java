package com.bndev.ood.hittastic.api;

import de.vandermeer.asciitable.AsciiTable;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;

public class Session {
    public User user;
    LocalDateTime ttl;

    public Session(User user) {

        this.user = user;
        this.ttl = LocalDateTime.now().plusHours(2);
    }

    public void checkLive() {
        if (this.ttl.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("This user has been inactive for too long. Logging out...");
        }
        this.ttl = LocalDateTime.now().plusHours(2);
    }

    public void checkAdmin() throws AuthenticationException {
        if (!this.user.isAdmin) {
            throw new AuthenticationException("You do not have permission to use this command");
        }
    }

    public List<Song> FindByTitle(String title) {
        checkLive();
        return Song.findByTitle(title);
    }

    public List<Song> FindByArtist(String artist) {
        checkLive();
        return Song.findByArtist(artist);
    }

    public Order BuySong(Song song, int qty) {
        checkLive();
        if (qty <= 0) {
            return null;
        }
        return this.user.createOrder(song, qty);
    }

    public AsciiTable ViewOrderHistory() {
        checkLive();
        return this.user.generateOrderTable();
    }

    public Song AddSong(String title, String artist) throws AuthenticationException {
        checkLive();
        checkAdmin();
        return Song.createSong(title, artist);
    }

    public List<User> GetAllUsers() throws AuthenticationException {
        checkLive();
        checkAdmin();
        return User.all.values().stream().toList();
    }

    public User UpdateUser(String username, String newUsername, String password, boolean isAdmin) throws AuthenticationException {
        checkLive();
        checkAdmin();
        return User.getAndUpdate(username, newUsername, password, isAdmin);
    }

    public User CreateUser(String username, String password, boolean isAdmin) throws AuthenticationException {
        checkLive();
        checkAdmin();
        if (isAdmin) {
            return User.CreateAdmin(username, password);
        } else {
            return User.CreateUser(username, password);
        }
    }

    public User GetUserByUsername(String username) throws AuthenticationException {
        checkLive();
        checkAdmin();
        return User.all.get(username);
    }

    public void DeleteUser(String username) throws AuthenticationException {
        checkLive();
        checkAdmin();
        User.all.remove(username);
        User.Save();
    }
}
