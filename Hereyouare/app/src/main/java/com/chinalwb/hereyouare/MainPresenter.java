package com.chinalwb.hereyouare;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.chinalwb.hereyouare.mvc.MVCUserListFragment;
import com.chinalwb.hereyouare.mvp.MVPUserListFragment;
import com.chinalwb.hereyouare.mvp2.MVP2UserListFragment;
import com.chinalwb.hereyouare.mvvvm.MVVMListFragment;
import com.chinalwb.hereyouare.nomvx.NoMVXUserListFragment;

/**
 * Created by wliu on 18/09/2018.
 */

public class MainPresenter implements IMainPresenter {

    private IMainView mMainView;

    private IMainPresenter.IFabListener mFabListener;

    public MainPresenter(IMainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void initView() {
        MainFragment fragment = MainFragment.newInstance();
        mMainView.showFragment(fragment);
        setFabListener(null);
        mMainView.setTitle("MVX");
    }

    @Override
    public void onFabClicked(View view) {
        if (mFabListener == null) {
            Snackbar.make(view, "欢迎页面没有注册事件处理器，试试点击Navigation Drawer里面的菜单吧！", Snackbar.LENGTH_SHORT).show();
        } else {
            mFabListener.onFabClicked();
        }
    }

    @Override
    public boolean onNavigationItemSelected(int id) {
        if (id == R.id.nav_no_mvx) {
            NoMVXUserListFragment fragment = NoMVXUserListFragment.newInstance();
            mMainView.showFragment(fragment);
            setFabListener(fragment);
            mMainView.setTitle("No MVX");
        } else if (id == R.id.nav_mvc) {
            MVCUserListFragment fragment = MVCUserListFragment.newInstance();
            mMainView.showFragment(fragment);
            setFabListener(fragment);
            mMainView.setTitle("MVC");
        } else if (id == R.id.nav_mvp) {
            MVPUserListFragment fragment = MVPUserListFragment.newInstance();
            mMainView.showFragment(fragment);
            setFabListener(fragment);
            mMainView.setTitle("MVP");
        } else if (id == R.id.nav_mvp2) {
            MVP2UserListFragment fragment = MVP2UserListFragment.newInstance();
            mMainView.showFragment(fragment);
            setFabListener(fragment);
            mMainView.setTitle("MVP2");
        } else if (id == R.id.nav_mvvm) {
            MVVMListFragment fragment = MVVMListFragment.newInstance();
            mMainView.showFragment(fragment);
            setFabListener(fragment);
            mMainView.setTitle("MVVM");
        } else if (id == R.id.nav_component_share) {
            mMainView.toast("尚未完成，待续。");
        } else if (id == R.id.nav_component_send) {
            mMainView.toast("尚未完成，待续。");
        }

        return true;
    }

    private void setFabListener(IFabListener fabListener) {
        mFabListener = fabListener;
    }
}
