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
    //??????????????????
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

        String[] ctype = new String[]{"??????","????????????", "????????????", "????????????", "????????????"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);  //???????????????????????????
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //??????????????????????????????????????????
        Spinner spinner = (Spinner) super.findViewById(R.id.m1_style);
        spinner.setAdapter(adapter);
        sp = (Spinner) findViewById(R.id.m1_style);
        final String kind = (String) sp.getSelectedItem();
        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String option = ctype[position];
                Log.d("info", "??????: " + option);
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

        //??????  ?????????????????????


    }
    

    private void setTypeId(String option) {
        switch (option) {
            case "????????????":
                typeId = 14;
                typeName = "??????";
                break;

            case "????????????":
                typeId = 8;
                typeName = "??????";
                break;

            case "????????????":
                typeId = 10;
                typeName = "??????";
                break;
            case "????????????":
                typeId = 5;
                typeName = "??????";
                break;
            case "??????":
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
                    //??????????????????
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

                Log.d("info", "1??????: "+ price);
                Log.d("info", "1??????: "+ content);
                Log.d("info", "1??????: "+ addr);
                while (imageCode == null)
                {
                    Log.d(TAG, "de: ");
                }
                //????????????  ?????? ?????????id??????????????????????????????ID????????????????????????????????????????????????????????????
                int id = Integer.parseInt(nomal_user.getId());
                System.out.println("???????????????nomal_user.getId()="+nomal_user.getId());
                System.out.println("???????????????id---------------------???"+id);
                addGoods addGoods = new addGoods(addr,content,imageCode,price,typeId,typeName,id);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //??????????????????
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

    //????????????
    private void showImage(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        image = baos.toByteArray();
        iv_image.setImageBitmap(bm);

    }


    private void post_image_url() {
        new Thread(() -> {

            // url??????
            String url = "http://47.107.52.7:88/member/tran/image/upload";

            // ?????????
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
                    Log.i("imageName:", file.getPath());//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    mbody.addFormDataPart("fileList", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

                } else {
                    System.out.println("???????????????");
                }
            }
            RequestBody requestBody = mbody.build();


            //??????????????????
            Request request = new Request.Builder()
                    .url(url)
                    // ???????????????????????????
                    .headers(headers)
                    .post(requestBody)
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //?????????????????????callback????????????
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }


    /**
     * ??????
     */
    private final Callback callback = new Callback() {


        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO ??????????????????
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO ??????????????????
            Type jsonType = new TypeToken<ResponseBody<Object>>() {
            }.getType();
            // ??????????????????json???
            String body = response.body().string();
            Log.d("info", body);
            // ??????json???????????????????????????
            ResponseBody<Object> dataResponseBody = gson.fromJson(body, jsonType);
            Log.d("info", dataResponseBody.toString());

            res = body;

            JsonParser parser = new JsonParser();
            // 2.?????? ???????????????
            JsonElement element = parser.parse(res);
            // 3.?????? ??????????????????????????? ??????????????? Gson????????????
            JsonObject root = element.getAsJsonObject();
            String code = root.get("code").getAsString();
            String msg = root.get("msg").getAsString();


            JsonElement dataElement = root.get("data");
            JsonElement element2 = parser.parse(String.valueOf(dataElement));
            // 3.?????? ??????????????????????????? ??????????????? Gson????????????
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
     * http????????????????????????
     *
     * @param <T> ??????
     */
    public static class ResponseBody<T> {

        /**
         * ???????????????
         */
        private int code;
        /**
         * ??????????????????
         */
        private String msg;
        /**
         * ????????????
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