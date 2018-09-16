package com.chinalwb.hereyouare.mvp2.view;

import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvp2.IBaseView;

import java.util.List;

public interface IUserListView extends IBaseView {

    interface IListViewHandler {
        void onRefreshList();
    }

    void updateList(List<UserModel> userModels);

    void showLoading();

    void hideLoading();

    void setViewHandler(IListViewHandler handler);
}
