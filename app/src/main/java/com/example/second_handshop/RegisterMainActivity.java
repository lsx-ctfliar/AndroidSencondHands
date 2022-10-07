package com.example.second_handshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.second_handshop.util.ViewUtil;
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
import com.example.second_handshop.util.ViewUtil;

public class RegisterMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_get_code;
    private Button toReturn;
    private EditText et_phone;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这里写用户对应的布局
        setContentView(R.layout.activity_register_main);
        btn_get_code = findViewById(R.id.btn_get_code);
        toReturn = findViewById(R.id.toReturn);
        et_phone = findViewById(R.id.et_phone);

        toReturn.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
    }

    private void get(){
        new Thread(() -> {

            phone = et_phone.getText().toString();
            Log.i("info",phone);

            // url路径
            String url = "http://47.107.52.7:88/member/tran/user/send?phone=";
            url = url + phone;
            Log.d("info",url);
            System.out.println("开始请求");
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
            System.out.println("开始请求");
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
            Gson gson = new Gson();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody<Object> dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_code:
                get();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("发送成功");
                AlertDialog dialog = builder.create();
                dialog.show();
                break;


            case R.id.toReturn:
                Intent intent = new Intent(RegisterMainActivity.this,LoginMainActivity.class);
                startActivity(intent);
                break;
        }
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


