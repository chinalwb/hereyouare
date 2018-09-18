package com.chinalwb.hereyouare.mvp.presenter;

public interface IUserListPresenter {
    String getEmptyListText();
    void loadList();
    void addUser();
}
