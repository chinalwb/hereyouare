package com.chinalwb.hereyouare.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.hereyouare.IMainPresenter;
import com.chinalwb.hereyouare.MainActivity;
import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.adapter.UserListAdapter;
import com.chinalwb.hereyouare.mvc.ScrollChildSwipeRefreshLayout;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvp.presenter.IUserListPresenter;
import com.chinalwb.hereyouare.mvp.presenter.UserUserListPresenter;
import com.chinalwb.hereyouare.mvp.view.IUserListView;

import java.util.List;

public class MVPUserListFragment extends Fragment implements IUserListView, IMainPresenter.IFabListener {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private IUserListPresenter mListPresenter;

    private ListView mUserListView;

    private UserListAdapter mUserListAdapter;

    public MVPUserListFragment() {
        super();
    }

    public static MVPUserListFragment newInstance() {
        return new MVPUserListFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = getFragmentView(inflater, container);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        mListPresenter = new UserUserListPresenter(this);
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mUserListView = rootView.findViewById(R.id.user_list_view);
        mUserListAdapter = new UserListAdapter(null);
        mUserListView.setAdapter(mUserListAdapter);

        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListPresenter.loadList();
            }
        });

        View emptyView = rootView.findViewById(R.id.empty_list_layout);
        TextView emptyTextView = emptyView.findViewById(R.id.empty_list_text_view);
        String emptyListViewTextString = mListPresenter.getEmptyListText();
        String emptyText = getResources().getString(R.string.empty_text, emptyListViewTextString);
        emptyTextView.setText(emptyText);
        mUserListView.setEmptyView(emptyView);
    }

    private View getFragmentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void updateList(List<UserModel> userModels) {
        hideLoading();
        mUserListAdapter.setUserModelList(userModels);
        mUserListAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Update UI from MVP", Toast.LENGTH_LONG).show();
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
    public void onFabClicked() {
        mListPresenter.addUser();
    }
}
