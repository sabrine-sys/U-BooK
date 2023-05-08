package com.fsb.e_commerceapplication.models;

public class UserModel {

    String name;
    String mail;
    String pass;
    String profileImg;

    public UserModel() {
    }

    public UserModel(String name, String mail, String pass, String profileImg) {
        this.name = name;
        this.mail = mail;
        this.pass = pass;
        this.profileImg = profileImg;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
