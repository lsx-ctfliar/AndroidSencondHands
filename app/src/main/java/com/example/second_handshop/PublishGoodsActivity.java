package com.example.second_handshop;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.second_handshop.service.AppidAndSecred_iml;
import com.example.second_handshop.service.nomal_user;
import com.example.second_handshop.util.addGoods;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class PublishGoodsActivity extends AppCompatActivity implements View.OnClickListener {
    private final Gson gson = new Gson();
    //上传图片参数
    private ImageView iv_image;
    private ActivityResultLauncher<Intent> mResultLauncher;
    private String picPath;
    private ArrayList<File> files;
    private Spinner sp;

    private int typeId;
    private String typeName;
    private byte[] image;

    private Button btn_publish;
    private EditText et_price;
    private EditText et_addr;
    private EditText et_content;


    private String res;
    private String url;
    private Long imageCode;
    private String addr;
    private int price;
    private String content;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_goods);

        et_price = findViewById(R.id.m1_price);
        et_addr = findViewById(R.id.m1_address);
        et_content = findViewById(R.id.m1_content);

        String[] ctype = new String[]{"选项","生活用品", "学习用品", "电子产品", "体育用品"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        Spinner spinner = (Spinner) super.findViewById(R.id.m1_style);
        spinner.setAdapter(adapter);
        sp = (Spinner) findViewById(R.id.m1_style);
        final String kind = (String) sp.getSelectedItem();
        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String option = ctype[position];
                Log.d("info", "选项: " + option);
                setTypeId(option);
                Log.d("info","typeID: " + typeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        files = new ArrayList<>();

        iv_image = findViewById(R.id.m1_image);
        iv_image.setOnClickListener(this);

        btn_publish = findViewById(R.id.btn_publish);
        btn_publish.setOnClickListener(this);

        //调用  发布商品的接口


    }
    

    private void setTypeId(String option) {
        switch (option) {
            case "生活用品":
                typeId = 14;
                typeName = "其他";
                break;

            case "学习用品":
                typeId = 8;
                typeName = "图书";
                break;

            case "体育用品":
                typeId = 10;
                typeName = "文玩";
                break;
            case "电子产品":
                typeId = 5;
                typeName = "数码";
                break;
            case "选项":
                typeId = 0;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m1_image:
                if (ContextCompat.checkSelfPermission(PublishGoodsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PublishGoodsActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
                break;

            case R.id.btn_publish:
                post_image_url();

                String str = et_price.getText().toString();
                price = Integer.valueOf(str);
                addr = et_addr.getText().toString();
                content = et_content.getText().toString();

                Log.d("info", "1价格: "+ price);
                Log.d("info", "1描述: "+ content);
                Log.d("info", "1地址: "+ addr);
                while (imageCode == null)
                {
                    Log.d(TAG, "de: ");
                }
                //之前这里  ，， 用户的id是写死的写成了别人的ID，导致了，查找用户发布记录的时候，总是空
                int id = Integer.parseInt(nomal_user.getId());
                System.out.println("登陆用户的nomal_user.getId()="+nomal_user.getId());
                System.out.println("发布商品的id---------------------："+id);
                addGoods addGoods = new addGoods(addr,content,imageCode,price,typeId,typeName,id);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            picPath = imagePath;
            Log.i("info", imagePath);
            showImage(imagePath);
            c.close();
        }
    }

    //加载图片
    private void showImage(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        image = baos.toByteArray();
        iv_image.setImageBitmap(bm);

    }


    private void post_image_url() {
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/tran/image/upload";

            // 请求头
            AppidAndSecred_iml app = new AppidAndSecred_iml();
            Headers headers = new Headers.Builder()
                    .add("appId", app.getAppId())
                    .add("appSecret", app.getAppSecret())
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
            MediaType MEDIA_TYPE_PNG = MediaType.parse("application/image/png; charset=utf-8");
            MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);

            System.out.println(files);
            files.add(new File(picPath));
            System.out.println(files);

            for (File file : files) {
                if (file.exists()) {
                    Log.i("imageName:", file.getPath());//经过测试，此处的名称不能相同，如果相同，只能保存最后一个图片，不知道那些同名的大神是怎么成功保存图片的。
                    mbody.addFormDataPart("fileList", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

                } else {
                    System.out.println("路径不存在");
                }
            }
            RequestBody requestBody = mbody.build();


            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(requestBody)
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException ex) {
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
            Type jsonType = new TypeToken<ResponseBody<Object>>() {
            }.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody<Object> dataResponseBody = gson.fromJson(body, jsonType);
            Log.d("info", dataResponseBody.toString());

            res = body;

            JsonParser parser = new JsonParser();
            // 2.获得 根节点元素
            JsonElement element = parser.parse(res);
            // 3.根据 文档判断根节点属于 什么类型的 Gson节点对象
            JsonObject root = element.getAsJsonObject();
            String code = root.get("code").getAsString();
            String msg = root.get("msg").getAsString();


            JsonElement dataElement = root.get("data");
            JsonElement element2 = parser.parse(String.valueOf(dataElement));
            // 3.根据 文档判断根节点属于 什么类型的 Gson节点对象
            JsonObject root2 = element2.getAsJsonObject();
            imageCode = root2.get("imageCode").getAsLong();
            url = root2.get("imageUrlList").getAsString();


            Log.d("info", "imgCode: " + imageCode);
            Log.d("info", "imgUrl: " + url);

            Intent intent = new Intent(PublishGoodsActivity.this,MainPageActivity.class);
            startActivity(intent);

        }
    };

    /**
     * http响应体的封装协议
     *
     * @param <T> 泛型
     */
    public static class ResponseBody<T> {

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

        public ResponseBody() {
        }

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