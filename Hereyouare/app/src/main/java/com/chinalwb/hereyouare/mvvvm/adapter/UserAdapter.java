package com.chinalwb.hereyouare.mvvvm.adapter;

import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.model.UserModel;

import java.util.List;

public class UserAdapter extends MyBaseAdapter {

        List<UserModel> data;

        // Provide a suitable constructor (depends on the kind of dataset)
        public UserAdapter(List<UserModel> myDataset) {
                data = myDataset;
        }
        @Override
        public Object getDataAtPosition(int position) {
                return data.get(position);
        }

        @Override
        public int getLayoutIdForType(int viewType) {
                return R.layout.fragment_recycler_view_element;
        }

        @Override
        public int getItemCount() {
                return data.size();
        }
}