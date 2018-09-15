package com.chinalwb.hereyouare.mvc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.mvc.controller.ListController;
import com.chinalwb.hereyouare.mvp.model.ListModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private TextView mTextView;

    private ProgressBar mProgressBar;

    private ListController mListController;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ListFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ListFragment newInstance(String param1, String param2) {
//        ListFragment fragment = new ListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        mTextView = rootView.findViewById(R.id.textview_list);
        mProgressBar = rootView.findViewById(R.id.progress);
        mListController = new ListController(new ListModel(), this);

        mRefreshLayout.setScrollUpChild(mTextView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListController.loadList();
            }
        });
    }

    public void updateList(String list) {
        hideLoading();
        mTextView.setText(list);
        Toast.makeText(getActivity(), "Update UI", Toast.LENGTH_LONG).show();
    }

    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }
}
