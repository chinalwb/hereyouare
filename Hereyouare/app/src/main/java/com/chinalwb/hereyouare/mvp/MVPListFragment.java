package com.chinalwb.hereyouare.mvp;

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
import com.chinalwb.hereyouare.mvc.ScrollChildSwipeRefreshLayout;
import com.chinalwb.hereyouare.mvp.model.ListModel;
import com.chinalwb.hereyouare.mvp.presenter.IListPresenter;
import com.chinalwb.hereyouare.mvp.presenter.ListPresenter;
import com.chinalwb.hereyouare.mvp.view.IListView;

public class MVPListFragment extends Fragment implements IListView {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private TextView mTextView;

    private ProgressBar mProgressBar;

    private IListPresenter mListPresenter;

    public MVPListFragment() {
        super();
    }

    public static MVPListFragment newInstance() {
        return new MVPListFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = getFragmentView(inflater, container);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        mListPresenter = new ListPresenter(new ListModel(), this);
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mTextView = rootView.findViewById(R.id.textview_list);
        mProgressBar = rootView.findViewById(R.id.progress);

        mRefreshLayout.setScrollUpChild(mTextView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListPresenter.loadList();
            }
        });
    }

    private View getFragmentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_list, container, false);
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
