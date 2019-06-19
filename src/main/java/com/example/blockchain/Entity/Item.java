package com.example.blockchain.Entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import com.example.blockchain.service.KeyUtil;

import java.util.Random;

public class Item {
    @JSONField(ordinal = 1)
    private String hash;
    @JSONField(ordinal = 2)
    private String name;
    @JSONField(ordinal = 3)
    private String source;
    @JSONField(ordinal = 4)
    private int volume;
    @JSONField(ordinal = 5)
    private long create_date;
    @JSONField(ordinal = 6)
    private boolean is_qualified;
    @JSONField(ordinal = 7)
    private boolean is_sold;

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
        this.is_sold = false;
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

    public boolean getIs_sold() {
        return is_sold;
    }

    public void setIs_sold(boolean is_sold) {
        this.is_sold = is_sold;
    }
}
