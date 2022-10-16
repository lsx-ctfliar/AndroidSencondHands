package com.example.second_handshop.util;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.reflect.TypeToken;


import com.example.second_handshop.service.AppidAndSecred_iml;
import com.example.second_handshop.service.good;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.List;

import java.util.ArrayList;

/**
 * **********step1**********
 * 网络安全配置
 * 在资源文件新建一个xml目录，在该目录下新建文件network_security_config.xml，配置如下
 * <?xml version="1.0" encoding="utf-8"?>
 * <network-security-config >
 *     <base-config cleartextTrafficPermitted="true" />
 * </network-security-config >
 *
 * **********step2**********
 * 在AndroidManifest.xml的<application></application>标签中引入上一步所添加的网络配置相关资源文件
 * <application android:networkSecurityConfig="@xml/network_security_config"></application>
 *
 * **********step3**********
 * 在AndroidManifest.xml中添加如下配置添加网络请求权限
 * <uses-permission android:name="android.permission.INTERNET" />
 *
 * **********step4**********
 * 添加以下依赖到build.gradle，用户可自主在[https://mvnrepository.com]仓库中选择合适的版本
 *   // 网络请求框架 okhttp3
 *   implementation 'com.squareup.okhttp3:okhttp:3.10.0'
 *   //用来解析json串
 *   // https://mvnrepository.com/artifact/com.google.code.gson/gson
 *   implementation 'com.google.code.gson:gson:2.9.1'
 *
 * **********step5**********
 * 用户对所请求的数据进行自主操作
 */
public class GetKindGoods{

    private final Gson gson = new Gson();
    public int typeId;
    public int userId;
    public int status_code;
    public int status = 0;





    public String status_msg,res;
    //定义一个货物对象列表
    public List<good> goodsList=new ArrayList<good>();


    public class get_list{
        public List<good> getlist()
        {
            return goodsList;
        }
    }




    public GetKindGoods(int typeId,int userId){
        this.typeId = typeId;
        this.userId = userId;
        get();
    }

    private void get(){
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/tran/goods/all?typeId="+typeId+"&userId="+userId;

            // 请求头
            AppidAndSecred_iml app = new AppidAndSecred_iml();
            Headers headers = new Headers.Builder()
                    .add("appId", app.getAppId())
                    .add("appSecret", app.getAppSecret())
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .get()
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Type jsonType = new TypeToken<ResponseBody<Object>>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody<Object> dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());
            status_code = dataResponseBody.getCode();
            System.out.println(status_code + "       99999999");

            res = body;
            JsonParser parser = new JsonParser();

            // 2.获得 根节点元素
            JsonElement element = parser.parse(res);
            // 3.根据 文档判断根节点属于 什么类型的 Gson节点对象
            JsonObject root = element.getAsJsonObject();
            String code = root.get("code").getAsString();
            String msg = root.get("msg").getAsString();
            System.out.println(code+"         777777777777");

//            Log.d("info", "msg: " + msg);
            JsonElement dataElement = root.get("data");
            JsonElement element2 = parser.parse(String.valueOf(dataElement));
            // 3.根据 文档判断根节点属于 什么类型的 Gson节点对象
            JsonObject root2 = element2.getAsJsonObject();

            JsonArray rec= root2.get("records").getAsJsonArray();
            int current = root2.get("current").getAsInt();
            int total = root2.get("total").getAsInt();
            System.out.println(current);
            System.out.println(total);
            //登陆成功之后之后 ，，在下面每循环一次就直接新建一个对象，储存数据
//            System.out.println(rec.get(1));


            for (int i = 0;i<total;i++){
                try {
                    JsonElement element3 = parser.parse(String.valueOf(rec.get(i)));
                    JsonObject root3 = element3.getAsJsonObject();
                    System.out.println(rec.get(i));
                    System.out.println(root3.get("id").getAsInt());
                    System.out.println(root3.get("content").getAsString());
                    //每次有个商品就直接新建一个新对象存储值
                    //之后将对象值加入一个队列
//                    good good_one = new good();
//
//                    int price = root3.get("price").getAsInt();
//                    String  content = root3.get("content").getAsString();
//
//                    //设置对象的值
//                    good_one.setPrice(price);
//                    good_one.setContent(content);
//
//                    goodsList.add(good_one);


                } catch (Exception e) {
                    e.printStackTrace();
                }




//                System.out.println(root3.get("imageUrlList").isJsonNull());
//                if (!root3.get("imageUrlList").isJsonNull()){
//
//                    String imagelist = root3.get("imageUrlList").getAsString();
//                    System.out.println(imagelist);
//                }

//                if (imagelist != null){
//
//                }
            }
            ///商品对象循环结束，打印一下
            Log.d("info", "商品列表"+goodsList);
            System.out.println(goodsList);
            status=1;

//            JsonElement element3 = parser.parse(String.valueOf(rec.get(5)));
//            JsonObject root3 = element3.getAsJsonObject();
//            System.out.println(root3.get("id").getAsInt());
//            JsonArray imageList = root3.get("imageUrlList").getAsJsonArray();
//            JsonElement element4 = parser.parse(String.valueOf(imageList));
//            JsonObject root4 = element4.getAsJsonObject();
//            System.out.println(root4.get("0").getAsString());
        }
    };
    /**
     * 去除首尾指定字符
     * @param str   字符串
     * @param element   指定字符
     * @return
     */
    public static String trimFirstAndLastChar(String str, String element){
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do{
            int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
            int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();
            str = str.substring(beginIndex, endIndex);
            beginIndexFlag = (str.indexOf(element) == 0);
            endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
        } while (beginIndexFlag || endIndexFlag);
        return str;
    }
    /**
     * http响应体的封装协议
     * @param <T> 泛型
     */
    public static class ResponseBody <T> {

        /**
         * 业务响应码
         */
        private int code;
        /**
         * 响应提示信息
         */
        private String msg;
        /**
         * 响应数据
         */
        private T data;

        public ResponseBody(){}

        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
        public T getData() {
            return data;
        }

        @NonNull
        @Override
        public String toString() {
            return "ResponseBody{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}