package com.moutimid.musicapp.Model;

public class MusicFiles {
    private String path;
    private String title;
    private String artist;
    private String album;
    private String duration;
private String id;

    public MusicFiles(String path, String title, String artist) {
        this.path = path;
        this.title = title;
        this.artist = artist;
    }

    public MusicFiles(String path, String title, String artist, String album, String duration, String ID) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.id=ID;
    }

    public MusicFiles() {

    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
