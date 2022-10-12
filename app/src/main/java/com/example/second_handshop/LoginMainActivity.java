package com.example.second_handshop;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import com.example.second_handshop.service.nomal_user;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class LoginMainActivity extends AppCompatActivity implements View.OnClickListener {


    nomal_user nomal_user = new nomal_user();
    private final Gson gson = new Gson();
    private Button btn_login;
    private EditText et_input_code;
    private EditText et_input_phone;
    private String code;
    private String phone;
    private Button toRegister;
    private Button btn_getCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这里写用户对应的布局
        setContentView(R.layout.activity_login_main);

        btn_login = findViewById(R.id.login);
        toRegister = findViewById(R.id.btn_toRegister);
        et_input_phone = findViewById(R.id.et_input_phone);
        et_input_code = findViewById(R.id.et_input_code);
        btn_getCode = findViewById(R.id.btn_getCode);

        btn_login.setOnClickListener(this);
        toRegister.setOnClickListener(this);


        btn_getCode.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        code = et_input_code.getText().toString();
        phone = et_input_phone.getText().toString();
        switch (v.getId()) {
            case R.id.login:
                post(code, phone);

                break;

            case R.id.btn_toRegister:
                Intent intent = new Intent(LoginMainActivity.this,RegisterMainActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_getCode:
                Log.d("info", "onClick: 获取验证码");
                get();

        }
    }





    //登录
    private void post(String code, String phone){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/tran/user/login";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "d9b1f1c026fa4b8c94423639085ddd22")
                    .add("appSecret", "53864593b0a674eb842ad86bc222e2d437138")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("code", code);
            bodyMap.put("phone", phone);
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
            Log.d("info", "登陆失败");
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
//            Log.d("info", dataResponseBody.toString());


            //509292
            //请求成功之后跳转到主页面
            int code =dataResponseBody.getCode();
            if(code==200)
            {
                //获取到用户的数据存进用户对象中
                String data = body.toString();
                ///////有个坑，这里一定要把得到的字符串除去   双引号，。不然后面的  id在转化成数字的时候会报错
                String id = data.split(",")[2].split(":")[2].replace("\"","");
                String appkey = data.split(",")[3].split(":")[1].replace("\"","");
                String username = data.split(",")[4].split(":")[1].replace("\"","");
                String money = data.split(",")[5].split(":")[1].replace("\"","");
                String avatar = data.split(",")[6].split(":")[1].replace("\"","");

                Log.d(TAG, "用户的数据获取："+id+appkey+username+money+avatar);

                nomal_user.setId(id);
                nomal_user.setAppkey(appkey);
                nomal_user.setUsername(username);
                nomal_user.setMoney(money);
                nomal_user.setAvatar(avatar);
                Log.d("info", nomal_user.toString());

                Intent intent = new Intent(LoginMainActivity.this, MainPageActivity.class);
                startActivity(intent);
            }

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




    //获取验证码
    private void get(){
        new Thread(() -> {

            phone = et_input_phone.getText().toString();
            Log.i("info",phone);

            // url路径
            String url = "http://47.107.52.7:88/member/tran/user/send?phone=";
            url = url + phone;
            Log.d("info",url);

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "d9b1f1c026fa4b8c94423639085ddd22")
                    .add("appSecret", "53864593b0a674eb842ad86bc222e2d437138")
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
                client.newCall(request).enqueue(callback3);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }


    /**
     * 回调
     */
    private final Callback callback3 = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Type jsonType = new TypeToken<ResponseBody3<Object>>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);

            // 解析json串到自己封装的状态
            ResponseBody3<Object> dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());



        }
    };

    /**
     * http响应体的封装协议
     * @param <T> 泛型
     */
    public static class ResponseBody3 <T> {

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

        public ResponseBody3(){}

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