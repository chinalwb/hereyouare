package com.chinalwb.hereyouare.mvvvm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinalwb.hereyouare.BR;
import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.model.UserModel;

import java.util.List;

/**
 * Created by wliu on 17/09/2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserModelViewHolder> {

    private Context context;
    private List<UserModel> userModels;

    public UserAdapter(Context context, List<UserModel> userModels) {
        this.context = context;
        this.userModels = userModels;
    }

    public void setUserModels(List<UserModel> userModels) {
        this.userModels = userModels;
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(
                R.layout.fragment_recycler_view_element, parent, false);
        return new UserModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserModelViewHolder holder, int position) {
        if (userModels == null || userModels.size() <= position) {
            return;
        }
        UserModel userModel = userModels.get(position);
        holder.getBindings().setVariable(BR.userModel, userModel);
        holder.getBindings().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (userModels == null) {
            return 0;
        }
        return userModels.size();
    }
}
