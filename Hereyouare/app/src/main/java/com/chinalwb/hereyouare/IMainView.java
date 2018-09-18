package com.chinalwb.hereyouare;


import android.support.v4.app.Fragment;

/**
 * Created by wliu on 18/09/2018.
 */

public interface IMainView {

    void setTitle(String title);

    void showFragment(Fragment fragment);

    void toast(String msg);
}
