package com.chinalwb.hereyouare.mvp2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinalwb.hereyouare.mvp2.presenter.MyListPresenter;
import com.chinalwb.hereyouare.mvp2.view.IListView;
import com.chinalwb.hereyouare.mvp2.view.MyListView;

public class MVP2ListFragment extends Fragment implements IListView.IListViewHandler, MyListPresenter.ListUpdaterListener {

    private IListView mListView;

    MyListPresenter mPresenter;

    public MVP2ListFragment() {
        super();
    }

    public static MVP2ListFragment newInstance() {
        return new MVP2ListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MyListPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListView = new MyListView(inflater, container);
        mListView.setViewHandler(this);
        return mListView.getRootView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.registerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unregisterListener(this);
    }

    @Override
    public void onRefreshList() {
        mListView.showLoading();
        mPresenter.loadList();
    }

    @Override
    public void updateList(String data) {
        mListView.hideLoading();
        mListView.updateList(data);
    }
}
