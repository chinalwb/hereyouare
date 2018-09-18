package com.chinalwb.hereyouare.mvvvm.custom;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

/**
 * Created by wliu on 17/09/2018.
 */

public class CustomImageBinding {

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
