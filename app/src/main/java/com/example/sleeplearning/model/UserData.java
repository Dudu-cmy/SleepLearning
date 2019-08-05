package com.example.sleeplearning.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("language")
    @Expose
    private String language;

    public UserData(String ID, String language) {
        this.ID = ID;
        this.language = language;
    }

    public UserData() {
        ID = "";
        language="";
    }

    @Override
    public String toString() {
        return "UserData{" +
                "ID='" + ID + '\'' +
                ", language='" + language + '\'' +
                '}';
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
