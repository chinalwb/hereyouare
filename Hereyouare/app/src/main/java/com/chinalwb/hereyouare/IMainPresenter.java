package com.chinalwb.hereyouare;

import android.view.MenuItem;
import android.view.View;

/**
 * Created by wliu on 18/09/2018.
 */

public interface IMainPresenter {

    interface IFabListener {
        void onFabClicked();
    }

    void initView();

    void onFabClicked(View view);

    boolean onNavigationItemSelected(int menuId);

}
