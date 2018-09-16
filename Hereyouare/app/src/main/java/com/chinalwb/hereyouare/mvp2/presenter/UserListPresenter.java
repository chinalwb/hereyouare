package com.chinalwb.hereyouare.mvp2.presenter;

import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.common.dataManager.UserManager;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvp2.BasePresenter;

import java.util.List;

public class UserListPresenter extends BasePresenter<UserListPresenter.ListUpdaterListener> {

    private List<UserModel> mUserModels;

    public UserListPresenter() {
    }

    public void loadList() {
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }

    public void addUser() {
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
                for (ListUpdaterListener listener : getListeners()) {
                    listener.updateList(userModels);
                }
            }
        });
    }

    public interface ListUpdaterListener {
        void updateList(List<UserModel> userModels);
    }
}
