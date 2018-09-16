package com.chinalwb.hereyouare.mvp.presenter;


import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.mvp.model.UserModel;
import com.chinalwb.hereyouare.mvp.view.IUserListView;

public class UserUserListPresenter implements IUserListPresenter {

    private IUserListView mListView;

    private UserModel mUserModel;

    public UserUserListPresenter(UserModel userModel, IUserListView iUserListView) {
        mUserModel = userModel;
        mListView = iUserListView;
    }

    @Override
    public void loadList() {
        mListView.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                String data = mUserModel.loadList("Presenter");
                updateUI(data);
            }
        });
    }

    private void updateUI(final String data) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mListView.updateList(data);
            }
        });
    }
}
