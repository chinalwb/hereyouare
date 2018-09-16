package com.chinalwb.hereyouare.mvp.view;

import com.chinalwb.hereyouare.common.model.UserModel;

import java.util.List;

public interface IUserListView {

    void updateList(List<UserModel> userModels);

    void showLoading();

    void hideLoading();
}
