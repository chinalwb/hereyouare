package com.chinalwb.hereyouare.mvp.model;

/**
 * 读取本地数据  或  读取网络数据
 */
public class ListModel {

    public ListModel() {

    }

    public String loadList(String from) {
        String list = "";
        try {
            Thread.sleep(2000);
            list = "This is a List<Object> from " + from;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

}
