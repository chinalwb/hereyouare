package com.chinalwb.hereyouare.mvp2.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.adapter.UserListAdapter;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvc.ScrollChildSwipeRefreshLayout;

import java.util.List;

public class UserListView implements IUserListView {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private ListView mUserListView;

    private UserListAdapter mUserListAdapter;

    private IListViewHandler mListViewHandler;

    private View mEmptyView;

    private TextView mEmptyTextView;

    private View mRootView;

    private Context mContext;

    public UserListView(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView = layoutInflater.inflate(getLayoutId(), container, false);
        mContext = mRootView.getContext();
        init(mRootView);
    }

    private int getLayoutId() {
        return R.layout.fragment_list;
    }

    protected void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mUserListView = rootView.findViewById(R.id.user_list_view);
        mUserListAdapter = new UserListAdapter(null);
        mUserListView.setAdapter(mUserListAdapter);

        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListViewHandler != null) {
                    mListViewHandler.onRefreshList();
                }
            }
        });

        mEmptyView = rootView.findViewById(R.id.empty_list_layout);
        mEmptyTextView = mEmptyView.findViewById(R.id.empty_list_text_view);
    }


    @Override
    public void updateList(List<UserModel> userModels) {
        hideLoading();
        mUserListAdapter.setUserModelList(userModels);
        mUserListAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Update UI", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setViewHandler(IListViewHandler handler) {
        mListViewHandler = handler;
    }

    @Override
    public void setEmptyListText(String emptyListViewTextString) {
        String emptyText = mContext.getResources().getString(R.string.empty_text, emptyListViewTextString);
        mEmptyTextView.setText(emptyText);
        mUserListView.setEmptyView(mEmptyView);
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
