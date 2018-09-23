package com.chinalwb.hereyouare.mvvvm.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by wliu on 17/09/2018.
 */

public class UserModelViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding bindings;

    public UserModelViewHolder(ViewDataBinding bindings) {
        super(bindings.getRoot());
        this.bindings = bindings;
    }

    public void bind(Object obj) {
        bindings.setVariable(BR.userModel,obj);
        bindings.executePendingBindings();
    }
}
