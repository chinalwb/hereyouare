package com.chinalwb.hereyouare.mvc.controller;

import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.common.dataManager.UserManager;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvc.MVCUserListFragment;

import java.util.List;

public class UserListController {

    private MVCUserListFragment mMVCUserListFragment;

    private List<UserModel> mUserModels;

    public UserListController(MVCUserListFragment MVCUserListFragment) {
        mMVCUserListFragment = MVCUserListFragment;
    }

    public void loadList() {
        mMVCUserListFragment.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }

    public void addUser() {
        mMVCUserListFragment.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = UserManager.addUser();
                mUserModels.add(userModel);
                updateUI(mUserModels);
            }
        });
    }



    private void updateUI(final List<UserModel> userModels) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mMVCUserListFragment.hideLoading();
                mMVCUserListFragment.updateListView(userModels);
            }
        });
    }
}
