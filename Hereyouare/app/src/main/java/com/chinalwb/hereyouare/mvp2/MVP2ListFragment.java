package com.chinalwb.hereyouare.mvp2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.base.BaseFragment;
import com.chinalwb.hereyouare.mvc.ScrollChildSwipeRefreshLayout;
import com.chinalwb.hereyouare.mvp.model.ListModel;
import com.chinalwb.hereyouare.mvp.presenter.ListPresenter;
import com.chinalwb.hereyouare.mvp2.view.IListView;

public class MVP2ListFragment extends BaseFragment implements IListView {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private TextView mTextView;

    private ProgressBar mProgressBar;

    private IListView.IListViewHandler mListViewHandler;

    public MVP2ListFragment() {
        super();
    }

    public static MVP2ListFragment newInstance() {
        return new MVP2ListFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mTextView = rootView.findViewById(R.id.textview_list);
        mProgressBar = rootView.findViewById(R.id.progress);

        mRefreshLayout.setScrollUpChild(mTextView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListViewHandler != null) {
                    mListViewHandler.loadList();
                }
            }
        });
    }


    @Override
    public void updateList(String listData) {
        hideLoading();
        mTextView.setText(listData);
        Toast.makeText(getActivity(), "Update UI", Toast.LENGTH_LONG).show();
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
}
