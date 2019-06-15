package com.example.blockchain.Entity;


import com.example.blockchain.service.KeyUtil;

import java.util.Random;

public class Item {
    private String hash;
    private String name;
    private String source;
    private int volume;
    private long create_date;
    private boolean is_qualified;

    public Item() {
        this.create_date = System.currentTimeMillis();
        this.hash = KeyUtil.getSHA256Str(name + source + String.valueOf(volume)
                + String.valueOf(create_date) + new Random());
    }

    public Item (String name, int volume, String source) {
        this.name = name;
        this.source = source;
        this.create_date = System.currentTimeMillis();
        this.is_qualified = false;
        this.volume = volume;
        this.hash = KeyUtil.getSHA256Str(name + source + String.valueOf(volume)
                + String.valueOf(create_date) + new Random());
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public boolean isIs_qualified() {
        return is_qualified;
    }

    public void setIs_qualified(boolean is_qualified) {
        this.is_qualified = is_qualified;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
