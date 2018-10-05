package com.mao.mp3_mdbsocials;

import java.io.Serializable;

//Serializable allows object -> string (Firebase will do smth). need getter and setters
public class Social implements Serializable {
    String name;
    String desc;
    String date;
    String key; //key of the Social/name of the image thats in the Storage/Database
    String email;
    public int interested;

    public Social() { }

    public Social (String name, String desc, String date, String key, String email) {
        this.name = name;
        this.desc = desc;
        this.date = date;
        this.key = key;
        this.email = email;
        this.interested = 0;
    }

    public int getInterested() {
        return interested;
    }

    public void setInterested(int interested) {
        this.interested = interested;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
