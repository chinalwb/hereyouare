package com.chinalwb.hereyouare.mvc.list.controller;

import android.os.Handler;
import android.os.Looper;

import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.mvc.list.ListFragment;

public class ListController {

    private ListFragment mListFragment;

    public ListController(ListFragment listFragment) {
        mListFragment = listFragment;
    }

    public void loadList() {
        mListFragment.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    updateUI();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUI() {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mListFragment.updateList(null);
            }
        });
    }
}
