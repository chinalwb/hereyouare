package com.chinalwb.hereyouare.mvvvm;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinalwb.hereyouare.BR;
import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.databinding.FragmentRecyclerViewElementBinding;
import com.chinalwb.hereyouare.databinding.FragmentRecyclerviewBinding;
import com.chinalwb.hereyouare.mvc.ScrollChildSwipeRefreshLayout;
import com.chinalwb.hereyouare.mvvvm.adapter.UserAdapter;

import java.util.List;

/**
 * 尚未完成，待续，谢谢。
 */
public class MVVMListFragment extends Fragment {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;

    public MVVMListFragment() {

    }

    public static MVVMListFragment newInstance() {
        return new MVVMListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);


        FragmentRecyclerviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false);
        View view = binding.getRoot();

        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.user_recycler_view);
        mRefreshLayout.setScrollUpChild(mRecyclerView);
        mUserAdapter = new UserAdapter(getContext(), null);
        mRecyclerView.setAdapter(mUserAdapter);

        return view;
    }


    @BindingAdapter({"userModels"})
    public static void runMe(View view, List<UserModel> userModelList) {
//        mUserAdapter.setUserModels(userModelList);
//        mUserAdapter.notifyDataSetChanged();

        Log.w("size", "user model list size == " + (userModelList == null ? 0 : userModelList.size()));
    }

}
