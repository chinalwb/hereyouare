package com.chinalwb.hereyouare.mvc.model;

/**
 * 读取本地数据  或  读取网络数据
 */
public class UserModel {

    public UserModel() {

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
