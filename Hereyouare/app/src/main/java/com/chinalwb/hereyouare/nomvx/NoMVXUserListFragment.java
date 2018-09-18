package com.chinalwb.hereyouare.nomvx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.hereyouare.IMainPresenter;
import com.chinalwb.hereyouare.MainActivity;
import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.MainThreadPoster;
import com.chinalwb.hereyouare.common.adapter.UserListAdapter;
import com.chinalwb.hereyouare.common.dataManager.UserManager;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvc.ScrollChildSwipeRefreshLayout;
import com.chinalwb.hereyouare.mvc.controller.UserListController;

import java.util.List;

/**
 * Created by wliu on 18/09/2018.
 */

public class NoMVXUserListFragment extends Fragment implements IMainPresenter.IFabListener {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private ListView mUserListView;

    private UserListAdapter mUserListAdapter;

    private List<UserModel> mUserModels;


    public NoMVXUserListFragment() {

    }

    public static NoMVXUserListFragment newInstance() {
        return new NoMVXUserListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        init(view);
        return view;
    }

    private void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mUserListView = rootView.findViewById(R.id.user_list_view);
        mUserListAdapter = new UserListAdapter(null);
        mUserListView.setAdapter(mUserListAdapter);

        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });

        setListEmptyView(rootView);
    }

    private void setListEmptyView(View rootView) {
        View emptyView = rootView.findViewById(R.id.empty_list_layout);
        TextView emptyTextView = emptyView.findViewById(R.id.empty_list_text_view);
        String emptyText = getResources().getString(R.string.empty_text, "No MVX");
        emptyTextView.setText(emptyText);
        mUserListView.setEmptyView(emptyView);
    }

    private void loadList() {
        showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }

    private void updateUI(final List<UserModel> userModels) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                updateListView(userModels);
            }
        });
    }

    public void updateListView(List<UserModel> userModels) {
        mUserListAdapter.setUserModelList(userModels);
        mUserListAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Update UI from Fragment: " + userModels.size(), Toast.LENGTH_LONG).show();
    }

    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onFabClicked() {
        addUser();
    }

    private void addUser() {
        showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = UserManager.addUser();
                mUserModels.add(userModel);
                updateUI(mUserModels);
            }
        });
    }
}
