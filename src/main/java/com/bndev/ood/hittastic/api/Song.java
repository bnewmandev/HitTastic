package com.bndev.ood.hittastic.api;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Song implements Serializable, Storable {
    public static StorableHashmap<Song> all = new StorableHashmap<>("songs");
    UUID id;
    public String title;
    public String artist;

    public int sales;

    public static void reset() {
        Song.all = new StorableHashmap<>("songs");
    }

    public String id() {
        return id.toString();
    }

    private Song(String title, String artist, UUID id, int sales) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.sales = sales;
    }

    public static Song createSong(String title, String artist) {
        UUID id = UUID.randomUUID();
        Song song = new Song(title, artist, id, 0);
        Song.all.put(id.toString(), song);
        Song.Save();
        return song;
    }

    public static List<Song> findByTitle(String title) {
        return filter(s -> matcher(s.title, title));
    }

    public static List<Song> findByArtist(String artist) {
        return filter(s -> matcher(s.artist, artist));
    }

    private static boolean matcher(String str, String searchStr) {
        return StringUtils.containsIgnoreCase(str, searchStr);
    }

    private static List<Song> filter(Predicate<Song> songPredicate) {
        return Song.all.values().stream().filter(songPredicate).collect(Collectors.toList());
    }

//    public static Song songCreator(String title, String artist) {
//
//        return Song.songCreator(title, artist, id, 0);
//    }

    public static void Save() {
        Song.all.save();
    }

    public static void Load() {
        Song.all.load();
    }
}