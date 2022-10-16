package com.example.second_handshop.util;

import androidx.annotation.NonNull;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.second_handshop.service.AppidAndSecred_iml;
import com.google.gson.Gson;
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


public class addGoods<T> {

    private final Gson gson = new Gson();

    public int status_code = 0;
    public String status_msg = null;
    public T data;
    public String addr;
    public String content;
    public int id;
    public Long imageCode;
    public int price;
    public int typeId;
    public String typeName;
    public int userId;


    public addGoods(String addr,String content,Long imageCode,int price,int typeId,String typeName,int userId){

        this.addr = addr;
        this.content = content;
        this.imageCode = imageCode;
        this.price = price;
        this.typeId = typeId;
        this.typeName = typeName;
        this.userId = userId;

        Log.d("info","地址：" + addr);
        Log.d("info", "价格：" + price);
        Log.d("info","imageCode___________" + imageCode);

        post_public();
    }
    private void post_public(){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/tran/goods/add";

            // 请求头
            AppidAndSecred_iml app = new AppidAndSecred_iml();
            Headers headers = new Headers.Builder()
                    .add("appId", app.getAppId())
                    .add("appSecret", app.getAppSecret())
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("addr", addr);
            bodyMap.put("content", content);
//            bodyMap.put("id", id);
            bodyMap.put("imageCode", imageCode);
            bodyMap.put("price", price);
            bodyMap.put("typeId", typeId);
            bodyMap.put("typeName", typeName);
            bodyMap.put("userId", userId);
            // 将Map转换为字符串类型加入请求体中
            String body = gson.toJson(bodyMap);

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
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
        }
    };

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