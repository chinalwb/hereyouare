package com.chinalwb.hereyouare.common.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.model.UserModel;

import java.util.List;

/**
 * ListView adapter.
 */
public class UserListAdapter extends BaseAdapter {

    private List<UserModel> userModelList;

    public UserListAdapter(List<UserModel> userModels) {
        userModelList = userModels;
    }

    public void setUserModelList(List<UserModel> userModels) {
        userModelList = userModels;
    }

    @Override
    public UserModel getItem(int position) {
        if (userModelList == null) {
            return null;
        }
        return userModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (userModelList == null) {
            return 0;
        }
        return userModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.user_item, parent, false);
            ImageView picView = convertView.findViewById(R.id.user_pic);
            TextView nameView = convertView.findViewById(R.id.user_name);
            TextView descView = convertView.findViewById(R.id.user_desc);

            viewHolder = new ViewHolder();
            viewHolder.picImageView = picView;
            viewHolder.nameTextView = nameView;
            viewHolder.descTextView = descView;

            convertView.setTag(viewHolder);

            Log.e("MVX", "convert view is null, set view holder " + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            Log.e("MVX", "convert view not null, use view holder " + position);
        }

        UserModel userModel = getItem(position);
        viewHolder.picImageView.setImageResource(userModel.picId);
        viewHolder.nameTextView.setText(userModel.userName);
        viewHolder.descTextView.setText(userModel.userDesc);

        return convertView;
    }

    class ViewHolder {
        ImageView picImageView;
        TextView nameTextView;
        TextView descTextView;
    }
} // #End of UserListAdapter