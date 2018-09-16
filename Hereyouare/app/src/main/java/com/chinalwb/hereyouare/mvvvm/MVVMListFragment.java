package com.chinalwb.hereyouare.mvvvm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinalwb.hereyouare.R;

public class MVVMListFragment extends Fragment {

    public MVVMListFragment() {

    }

    public static MVVMListFragment newInstance() {
        return new MVVMListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        return view;
    }
}
