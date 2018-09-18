package com.chinalwb.hereyouare.mvp2.presenter;

import com.chinalwb.hereyouare.mvp2.IBasePresenter;

/**
 * Created by wliu on 18/09/2018.
 */

public interface IUserListPresenter<T> extends IBasePresenter<T> {

    void loadList();

    void addUser();

    String getEmptyListText();
}
