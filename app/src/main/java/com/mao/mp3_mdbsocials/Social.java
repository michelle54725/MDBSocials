package com.mao.mp3_mdbsocials;

import java.io.Serializable;
import java.net.URI;

//Serializable allows object -> string (Firebase will do smth). need getter and setters
public class Social implements Serializable {
    String name;
    String desc;
    long date;

    String key; //key of the Social/name of the image thats in the Storage/Database

    public Social() { }

    public Social (String name, String desc, long date, String key) {
        this.name = name;
        this.desc = desc;
        this.date = date;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getkey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
