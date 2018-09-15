package com.chinalwb.hereyouare.mvc.controller;

import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.mvc.ListFragment;
import com.chinalwb.hereyouare.mvp.model.ListModel;

public class ListController {

    private ListModel mListModel;
    private ListFragment mListFragment;

    public ListController(ListModel listModel, ListFragment listFragment) {
        mListModel = listModel;
        mListFragment = listFragment;
    }

    public void loadList() {
        mListFragment.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                String data = mListModel.loadList("Controller");
                updateUI(data);
            }
        });
    }

    private void updateUI(final String data) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mListFragment.updateList(data);
            }
        });
    }
}
