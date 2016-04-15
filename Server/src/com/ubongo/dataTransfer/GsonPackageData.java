package com.ubongo.dataTransfer;


import java.io.Serializable;

public class GsonPackageData implements Serializable {
    private int id;
    private String name;
    private String pin;
    private String difficulty;

    public GsonPackageData(int id, String name, String pin, String difficulty) {
        this.id = id;
        this.name = name;
        this.pin = pin;
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPin() {
        return pin;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setName(String value) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
