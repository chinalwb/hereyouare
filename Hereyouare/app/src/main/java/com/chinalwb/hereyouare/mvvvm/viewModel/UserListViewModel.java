package com.chinalwb.hereyouare.mvvvm.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.chinalwb.hereyouare.BR;
import com.chinalwb.hereyouare.common.BackgroundThreadPoster;
import com.chinalwb.hereyouare.common.dataManager.UserManager;
import com.chinalwb.hereyouare.common.model.UserModel;
import com.chinalwb.hereyouare.mvvvm.adapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wliu on 17/09/2018.
 */

public class UserListViewModel extends BaseObservable {

    private List<UserModel> userModels;

    public List<UserModel> getUserModels() {
        if (userModels == null) {
            userModels = UserManager.callAPIToGetUserList();
        }
        return userModels;
    }

//    public void loadList() {
//        BackgroundThreadPoster.post(new Runnable() {
//            @Override
//            public void run() {
//                userModels = UserManager.callAPIToGetUserList();
//                setUserModels(userModels);
//                Log.w("MVVM", "users size == " + userModels.size());
//            }
//        });
//    }

//    public SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
//        return new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.e("MVVM", "on refresh ");
//                loadList();
//
//            }
//        };
//    }
}
