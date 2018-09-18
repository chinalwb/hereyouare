package com.chinalwb.hereyouare.common.model;

/**
 *
 */
public class UserModel {

    public int picId;
    public String userName;
    public String userDesc;

    public UserModel(int picId, String userName, String userDesc) {
        this.picId = picId;
        this.userName = userName;
        this.userDesc = userDesc;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }
}
