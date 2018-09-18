package com.chinalwb.hereyouare.mvp2;

/**
 * Created by wliu on 18/09/2018.
 */

public interface IBasePresenter<T> {

    void registerListener(T t);

    void unregisterListener(T t);
}
