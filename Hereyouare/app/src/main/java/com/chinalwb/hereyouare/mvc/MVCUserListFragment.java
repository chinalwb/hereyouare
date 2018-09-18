package com.chinalwb.hereyouare.mvc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.hereyouare.IMainPresenter;
import com.chinalwb.hereyouare.MainActivity;
import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.adapter.UserListAdapter;
import com.chinalwb.hereyouare.mvc.controller.UserListController;
import com.chinalwb.hereyouare.common.model.UserModel;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MVCUserListFragment extends Fragment implements IMainPresenter.IFabListener {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private ListView mUserListView;

    private UserListAdapter mUserListAdapter;

    private UserListController mUserListController;

    public MVCUserListFragment() {
        // Required empty public constructor
    }

    public static MVCUserListFragment newInstance() {
        return new MVCUserListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mUserListView = rootView.findViewById(R.id.user_list_view);
        mUserListAdapter = new UserListAdapter(null);
        mUserListView.setAdapter(mUserListAdapter);
        mUserListController = new UserListController(this);

        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserListController.loadList();
            }
        });

        View emptyView = rootView.findViewById(R.id.empty_list_layout);
        TextView emptyTextView = emptyView.findViewById(R.id.empty_list_text_view);
        String emptyListViewTextString = mUserListController.getEmptyListText();
        String emptyText = getResources().getString(R.string.empty_text, emptyListViewTextString);
        emptyTextView.setText(emptyText);
        mUserListView.setEmptyView(emptyView);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateListView(List<UserModel> userModels) {
        mUserListAdapter.setUserModelList(userModels);
        mUserListAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Update UI from MVC " + userModels.size(), Toast.LENGTH_LONG).show();
    }

    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFabClicked() {
        mUserListController.addUser();
    }
}
