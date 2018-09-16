package com.chinalwb.hereyouare.mvc.controller;

import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.mvc.MVCUserListFragment;
import com.chinalwb.hereyouare.mvp.model.UserModel;

public class UserListController {

    private UserModel mUserModel;
    private MVCUserListFragment mMVCUserListFragment;

    public UserListController(UserModel userModel, MVCUserListFragment MVCUserListFragment) {
        mUserModel = userModel;
        mMVCUserListFragment = MVCUserListFragment;
    }

    public void loadList() {
        mMVCUserListFragment.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                String data = mUserModel.loadList("Controller");
                updateUI(data);
            }
        });
    }

    private void updateUI(final String data) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mMVCUserListFragment.updateList(data);
            }
        });
    }
}
