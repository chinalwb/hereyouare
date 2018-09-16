package com.chinalwb.hereyouare.mvp2.view;

import com.chinalwb.hereyouare.mvp2.IBaseView;

public interface IListView extends IBaseView {

    interface IListViewHandler {
        void onRefreshList();
    }

    void updateList(String listData);

    void showLoading();

    void hideLoading();

    void setViewHandler(IListViewHandler handler);
}
