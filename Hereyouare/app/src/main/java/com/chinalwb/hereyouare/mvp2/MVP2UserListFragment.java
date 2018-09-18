package com.chinalwb.hereyouare.mvp2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinalwb.hereyouare.IMainPresenter;
import com.chinalwb.hereyouare.MainActivity;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvp2.presenter.IUserListPresenter;
import com.chinalwb.hereyouare.mvp2.presenter.UserListPresenter;
import com.chinalwb.hereyouare.mvp2.view.IUserListView;
import com.chinalwb.hereyouare.mvp2.view.UserListView;

import java.util.List;

public class MVP2UserListFragment extends Fragment implements IUserListView.IListViewHandler,
        UserListPresenter.ListUpdaterListener, IMainPresenter.IFabListener {

    private IUserListView mListView;

    IUserListPresenter mPresenter;

    public MVP2UserListFragment() {
        super();
    }

    public static MVP2UserListFragment newInstance() {
        return new MVP2UserListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserListPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListView = new UserListView(inflater, container);
        mListView.setViewHandler(this);
        mListView.setEmptyListText(mPresenter.getEmptyListText());
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
    public void updateList(List<UserModel> userModels) {
        mListView.hideLoading();
        mListView.updateList(userModels);
    }

    @Override
    public void onFabClicked() {
        mListView.showLoading();
        mPresenter.addUser();
    }
}
