package com.chinalwb.hereyouare.mvp2.view;

public interface IListView {

    interface IListViewHandler {
        void loadList();
    }

    void updateList(String listData);

    void showLoading();

    void hideLoading();
}
