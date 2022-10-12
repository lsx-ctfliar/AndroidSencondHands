package com.example.second_handshop.dao;



//主要用来封装  用户的数据

public class user {


    //为了方便处理，所有的属性都是使用字符串的形式进行保存
    private static String id;
    private static String username;
    private static String appkey;
    private static String money;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        user.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        user.username = username;
    }

    public static String getAppkey() {
        return appkey;
    }

    public static void setAppkey(String appkey) {
        user.appkey = appkey;
    }

    public static String getMoney() {
        return money;
    }

    public static void setMoney(String money) {
        user.money = money;
    }

    public static String getAvatar() {
        return avatar;
    }

    public static void setAvatar(String avatar) {
        user.avatar = avatar;
    }

    private static String avatar;









}
