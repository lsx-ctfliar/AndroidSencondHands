package com.example.second_handshop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainPageActivity extends AppCompatActivity{

    private final Gson gson = new Gson();

    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private Button myself;
    private Button myshow;
    private Button changepwd;
    private Button about;
    private Button login;
    private TextView myId;
    protected Intent intent;
    private String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //这是底部栏的三个按钮事件
        button1=(RadioButton)findViewById(R.id.button_1);
        button2=(RadioButton)findViewById(R.id.button_2);
        button3=(RadioButton)findViewById(R.id.button_3);


        myself=(Button)findViewById(R.id.myself);
        myshow=(Button)findViewById(R.id.myShow);
        changepwd=(Button)findViewById(R.id.changepwd);
        about=(Button)findViewById(R.id.about);
        login=(Button)findViewById(R.id.login) ;
        myId=(TextView)findViewById(R.id.myId);



        //要在一进来的时候直接加载商品，，，调用分页获取所有已发布的商品信息列表
        get();
        //看看返回什么数据，之后进行处理

//

        //跳转到主页
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainPageActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });


        //跳转到增加到商品页面
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {if(a.equals("")||a==null){
                Toast.makeText(getApplicationContext(), "请先登录！", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainPageActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
                intent = new Intent(MainPageActivity.this,PublishGoodsActivity.class);
                startActivity(intent);
            }
        });

    }



    private void get(){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/tran/goods/type";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "d9b1f1c026fa4b8c94423639085ddd22")
                    .add("appSecret", "53864593b0a674eb842ad86bc222e2d437138")
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

