package com.chinalwb.hereyouare.common.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 *
 */
public class UserModel extends BaseObservable {

    private int picId;
    private String userName;
    private String userDesc;

    public UserModel(int picId, String userName, String userDesc) {
        this.picId = picId;
        this.userName = userName;
        this.userDesc = userDesc;
    }

    @Bindable
    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Bindable
    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }
}
