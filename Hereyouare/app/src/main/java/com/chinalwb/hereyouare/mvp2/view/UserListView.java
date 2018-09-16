package com.chinalwb.hereyouare.mvp2.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.mvc.ScrollChildSwipeRefreshLayout;

public class UserListView implements IUserListView {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private TextView mTextView;

    private ProgressBar mProgressBar;

    private IListViewHandler mListViewHandler;

    private View mRootView;

    public UserListView(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView = layoutInflater.inflate(getLayoutId(), container, false);
        init(mRootView);
    }

    private int getLayoutId() {
        return R.layout.fragment_list;
    }

    protected void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mTextView = rootView.findViewById(R.id.textview_list);
        mProgressBar = rootView.findViewById(R.id.progress);

        mRefreshLayout.setScrollUpChild(mTextView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListViewHandler != null) {
                    mListViewHandler.onRefreshList();
                }
            }
        });
    }


    @Override
    public void updateList(String listData) {
        hideLoading();
        mTextView.setText(listData);
        Toast.makeText(getContext(), "Update UI", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setViewHandler(IListViewHandler handler) {
        mListViewHandler = handler;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Context getContext() {
        if (mRootView == null) {
            return null;
        }
        return mRootView.getContext();
    }
}
