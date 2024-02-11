package com.moutimid.musicapp;
public class Song {
    private String name;
    private String description;
    private int imageResourceId;
    private int musicResourceId;

    public Song(String name, String description, int imageResourceId, int musicResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.musicResourceId = musicResourceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getMusicResourceId() {
        return musicResourceId;
    }
}
