package com.example.second_handshop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.second_handshop.service.AppidAndSecred_iml;
import com.example.second_handshop.service.good;
import com.example.second_handshop.service.nomal_user;
import com.example.second_handshop.util.GetKindGoods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{

    private final Gson gson = new Gson();

    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;



    private ImageView kindone;
    private ImageView kindtwo;
    private ImageView kindthree;
    private ImageView kindfour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //这是底部栏的三个按钮事件
        button1=(RadioButton)findViewById(R.id.button_1);
        button2=(RadioButton)findViewById(R.id.button_2);
        button3=(RadioButton)findViewById(R.id.button_3);







        //接口调试
        //要在一进来的时候直接加载商品，，，获取全部商品分类列表接口
//        Log.d("info", "调用获取全部已经发布的商品的列表");
//        get();
        //看看返回什么数据，之后进行处理

//
//        Log.d("info", "下面是调用获取全部的商品的信息的返回值");
        //测试接口知道，这个平台的商品类型是已经确定好了的，不能修改了，新增发布商品的时候需要指定已经有了的类型，分页获取已经发布的商品信息的时候 ，也需要指定引进存在的商品类型
//        get2();




        //跳转到主页

        //主界面种类绑定事件，调用接口，获取返回的数据
        kindone=findViewById(R.id.kind1);
        kindtwo=findViewById(R.id.kind2);
        kindthree=findViewById(R.id.kind3);
        kindfour=findViewById(R.id.kind4);

        kindone.setOnClickListener(this);
        kindtwo.setOnClickListener(this);
        kindthree.setOnClickListener(this);
        kindfour.setOnClickListener(this);


        //主页面无效操作跳转到主页面
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });


        //跳转到增加到商品页面
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, PublishGoodsActivity.class);
                startActivity(intent);
            }

        });
//895689

        //跳转到个人信息页面
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, PersonMessageActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kind1:

                Intent intent = new Intent(MainPageActivity.this, SportPageActivity.class);


                intent.putExtra("typeid","10");
                startActivity(intent);
                break;

            case R.id.kind2:


                /*intent = new Intent(MainPageActivity.this, KindPage1Activity.class);
                startActivity(intent);*/
                Intent intent2 = new Intent(MainPageActivity.this, SportPageActivity.class);

//生活用品的id是14
                intent2.putExtra("typeid","14");
                startActivity(intent2);
                break;


            case R.id.kind3:
                Intent intent3 = new Intent(MainPageActivity.this, SportPageActivity.class);

//生活用品的id是14
                intent3.putExtra("typeid","5");
                startActivity(intent3);

                break;

            case R.id.kind4:

                Intent intent4 = new Intent(MainPageActivity.this, SportPageActivity.class);

//生活用品的id是14
                intent4.putExtra("typeid","8");
                startActivity(intent4);
                break;

        }

    }


//894909
    ////到这里都是请求所有已经发布的所有商品类型列表，，返回值
    //ResponseBody{code=200, msg='成功', data=[{id=1.0, type=手机}, {id=2.0, type=奢品}, {id=3.0, type=潮品}, {id=4.0, type=美妆},
    // {id=5.0, type=数码}, {id=6.0, type=潮玩}, {id=7.0, type=游戏}, {id=8.0, type=图书}, {id=9.0, type=美食},
    // {id=10.0, type=文玩}, {id=11.0, type=母婴}, {id=12.0, type=家居}, {id=13.0, type=乐器}, {id=14.0, type=其他}]}
    private void get(){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/tran/goods/type";

            // 请求头
            AppidAndSecred_iml app = new AppidAndSecred_iml();
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", app.getAppId())
                    .add("appSecret", app.getAppSecret())
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
            Log.d("info", "请求已经发布的所有商品的类型列表："+body);
            // 解析json串到自己封装的状态
            ResponseBody<Object> dataResponseBody = gson.fromJson(body,jsonType);
//            Log.d("info", dataResponseBody.toString());
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


    //这里是调用了获取全部商品信息的接口，，，返回值是：


    /**
     * 回调
     */
    private final Callback callback5 = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Type jsonType = new TypeToken<ResponseBody5<Object>>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info","全部的商品发布的商品信息根据分析获取响应："+ body);
            // 解析json串到自己封装的状态
            ResponseBody5<Object> dataResponseBody = gson.fromJson(body,jsonType);
//            Log.d("info", dataResponseBody.toString());







        }
    };

    /**
     * http响应体的封装协议
     * @param <T> 泛型
     */
    public static class ResponseBody5 <T> {

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

        public ResponseBody5(){}

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

