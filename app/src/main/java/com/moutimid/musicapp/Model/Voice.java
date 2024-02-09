package com.moutimid.musicapp.Model;

public class Voice {
    String name;
    int song_image;

    public Voice(String name, int song_image) {
        this.name = name;
        this.song_image = song_image;
    }

    public int getSong_image() {
        return song_image;
    }

    public void setSong_image(int song_image) {
        this.song_image = song_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
