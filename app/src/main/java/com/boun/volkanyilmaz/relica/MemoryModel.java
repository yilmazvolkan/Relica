package com.boun.volkanyilmaz.relica;

/**
 * Created by volkanyilmaz on 13/03/18.
 */

public class MemoryModel {
    private String fullname, username, profilePath, imagePath, memoryText;
    private String date;

    public MemoryModel() {
        //boş yapıcı metod
    }


    public MemoryModel(String fullname, String username, String profilePath, String imagePath, String memoryText, String date) {
        this.fullname = fullname;
        this.username = username;
        this.profilePath = profilePath;
        this.imagePath = imagePath;
        this.memoryText = memoryText;
        this.date = date;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMemoryText() {
        return memoryText;
    }

    public void setMemoryText(String memoryText) {
        this.memoryText = memoryText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
