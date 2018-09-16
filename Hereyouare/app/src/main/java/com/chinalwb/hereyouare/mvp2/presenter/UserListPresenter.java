package com.chinalwb.hereyouare.mvp2.presenter;

import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.mvp.model.UserModel;
import com.chinalwb.hereyouare.mvp2.BasePresenter;

public class UserListPresenter extends BasePresenter<UserListPresenter.ListUpdaterListener> {

    private UserModel mUserModel;

    public UserListPresenter() {
        mUserModel = new UserModel();
    }

    public void loadList() {
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                String data = mUserModel.loadList("MVP2");
                updateUI(data);
            }
        });
    }

    private void updateUI(final String data) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                for (ListUpdaterListener listener : getListeners()) {
                    listener.updateList(data);
                }
            }
        });
    }

    public interface ListUpdaterListener {
        void updateList(String data);
    }

}
