package com.chinalwb.hereyouare.mvp.presenter;


import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.mvp.model.ListModel;
import com.chinalwb.hereyouare.mvp.view.IListView;

public class ListPresenter implements IListPresenter {

    private IListView mListView;

    private ListModel mListModel;

    public ListPresenter(ListModel listModel, IListView iListView) {
        mListModel = listModel;
        mListView = iListView;
    }

    @Override
    public void loadList() {
        mListView.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                String data = mListModel.loadList("Presenter");
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
