package com.chinalwb.hereyouare.mvp.presenter;


import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.common.dataManager.UserManager;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvp.view.IUserListView;

import java.util.List;

public class UserUserListPresenter implements IUserListPresenter {

    private IUserListView mListView;

    private List<UserModel> mUserModels;

    public UserUserListPresenter(IUserListView iUserListView) {
        mListView = iUserListView;
    }

    @Override
    public void loadList() {
        mListView.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }

    @Override
    public void addUser() {
        mListView.showLoading();
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
                mListView.updateList(userModels);
            }
        });
    }
}
