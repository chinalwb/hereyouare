package com.chinalwb.hereyouare.mvvvm.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wliu on 17/09/2018.
 */

public class UserModelViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding bindings;

    public UserModelViewHolder(View itemView) {
        super(itemView);
        bindings = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getBindings() {
        return bindings;
    }
}
