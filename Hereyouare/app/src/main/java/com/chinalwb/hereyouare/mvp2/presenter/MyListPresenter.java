package com.chinalwb.hereyouare.mvp2.presenter;

import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.mvp.model.ListModel;
import com.chinalwb.hereyouare.mvp2.BasePresenter;

public class MyListPresenter extends BasePresenter<MyListPresenter.ListUpdaterListener> {

    private ListModel mListModel;

    public MyListPresenter() {
        mListModel = new ListModel();
    }

    public void loadList() {
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                String data = mListModel.loadList("MVP2");
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
