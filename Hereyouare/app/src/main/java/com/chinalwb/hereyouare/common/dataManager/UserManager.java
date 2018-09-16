package com.chinalwb.hereyouare.common.dataManager;

import com.chinalwb.hereyouare.R;
import com.chinalwb.hereyouare.common.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    /**
     * 读取本地数据  或  读取网络数据
     * @return
     */
    public static List<UserModel> callAPIToGetUserList() {
        List<UserModel> list = new ArrayList<>();
        try {
            Thread.sleep(2000);

            int[] picIds = {
                    R.drawable.at_1,
                    R.drawable.at_2,
                    R.drawable.at_3,
                    R.drawable.at_4,
                    R.drawable.at_5,
                    R.drawable.at_6,
                    R.drawable.at_7,
                    R.drawable.at_8,
                    R.drawable.at_9
            };

            String[] userNames = {
                    "User 1",
                    "User 2",
                    "User 3",
                    "User 4",
                    "User 5",
                    "User 6",
                    "User 7",
                    "User 8",
                    "User 9",
            };

            String[] userDescs = {
                    "User Desc 1",
                    "User Desc 2",
                    "User Desc 3",
                    "User Desc 4",
                    "User Desc 5",
                    "User Desc 6",
                    "User Desc 7",
                    "User Desc 8",
                    "User Desc 9",
            };

            for (int i = 0; i < 9; i++) {
                UserModel userModel = new UserModel(picIds[i], userNames[i], userDescs[i]);
                list.add(userModel);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static UserModel addUser() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserModel(R.drawable.rainliu, "Rain Liu", "I am a little little little bird..");
    }
}
