package com.bndev.ood.hittastic.api;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StorableHashmap<V extends Storable> extends HashMap<String, V> implements Serializable, Map<String, V> {

    public String storeName;
    public String storeDir = "data";
    public String storeExt = "ser";

    public StorableHashmap(Map<String, ? extends V> m, String storeName) {
        super(m);
        this.storeName = storeName;
    }

    public StorableHashmap(String storeName) {
        this.storeName = storeName;
    }

    public Path getStoreFile() {
        return Paths.get(this.storeDir, this.storeName + "." + this.storeExt);
    }

    void save() {
        try {
            FileOutputStream fos = new FileOutputStream(this.getStoreFile().toString());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(this.values()));
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            FileInputStream fis = new FileInputStream(this.getStoreFile().toString());
            ObjectInputStream ois = new ObjectInputStream(fis);
            Collection<V> data = (ArrayList<V>) ois.readObject();
            this.putAll(collectionToMap(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <V extends Storable> Map<String, V> collectionToMap(Collection<V> data) {
        return data.stream().collect(Collectors.toMap(V::id, v -> v));
    }
}
