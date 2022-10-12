package com.example.second_handshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class AddPictureActivity extends AppCompatActivity {
    private final Gson gson = new Gson();
    private Activity myActivity;
    private TextView tvTitle;//标题
    private EditText etImage;//图片
    private EditText etContext;//内容
    private EditText imtitle; // 图片标题
    private ImageView ivImg;//图片
    private Button btnSelectImage;
    private String imagePath ="";
    private Picture picture;
    private String account;
    private Integer likes;
    private ArrayList<File> files;
    private Long imageCode;
    private String Content;
    private String Title;
    private Long userID;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        userID = user.getId();
        files = new ArrayList<>();
        setContentView(R.layout.activity_picture_add);
        imtitle = findViewById(R.id.imagetitle);
        tvTitle = findViewById(R.id.title);
        etContext = findViewById(R.id.context);
        ivImg = findViewById(R.id.iv_img);
        btnSelectImage = findViewById(R.id.btn_select_image);
        //选择图片上传
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClick();
            }
        });
    }
    /**
     * 选择图片
     */
    private void selectClick() {
        //取自：
        //https://blog.csdn.net/lian123456780/article/details/126122799?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522166460749016782414988049%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=166460749016782414988049&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~baidu_landing_v2~default-1-126122799-null-null.142^v51^new_blog_pos_by_title,201^v3^control&utm_term=%E5%AE%89%E5%8D%93%E5%BC%80%E5%8F%91%20%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87&spm=1018.2226.3001.4187
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        for (int i = 0; i < result.size(); i++) {
                            // onResult Callback
                            LocalMedia media = result.get(i);
                            String path;
                            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                            boolean compressPath = media.isCompressed() || (media.isCut() && media.isCompressed());
                            // 裁剪过
                            boolean isCutPath = media.isCut() && !media.isCompressed();

                            if (isCutPath) {
                                path = media.getCutPath();
                            } else if (compressPath) {
                                path = media.getCompressPath();
                            } else if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
                                // AndroidQ特有path
                                path = media.getAndroidQToPath();
                            } else if (!TextUtils.isEmpty(media.getRealPath())) {
                                // 原图
                                path = media.getRealPath();
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    path = PictureFileUtils.getPath(AddPictureActivity.this, Uri.parse(media.getPath()));
                                } else {
                                    path = media.getPath();
                                }
                            }
                            imagePath = path;
                        }
                        ivImg.setVisibility(View.VISIBLE);
                        System.out.println("检查加载图片路劲" + imagePath);
                        files.add(new File(imagePath));
                        Glide.with(AddPictureActivity.this).load(imagePath).into(ivImg);
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(AddPictureActivity.this, "error", Toast.LENGTH_SHORT).show();
                        // onCancel Callback
                    }
                });
    }

    public void save(View view) {
        if(files.size() == 0){
            Toast.makeText(AddPictureActivity.this, "图片不能为空！" , Toast.LENGTH_LONG).show();
            return;
        }
        Title = imtitle.getText().toString();
        Content = etContext.getText().toString();
        if(Title.equals("")){
            Toast.makeText(AddPictureActivity.this, "标题不能为空！" , Toast.LENGTH_LONG).show();
            return;
        }
        if(Content.equals("")){
            Toast.makeText(AddPictureActivity.this, "内容不能为空！" , Toast.LENGTH_LONG).show();
            return;
        }
        post();

    }

    private void post() {
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/photo/image/upload";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "5bb78418d8d9489a8702cbecfaa07cc9")
                    .add("appSecret", "184736cf550e4c5fd48f18ccbcb6b4f580f2a")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            MediaType MEDIA_TYPE_PNG = MediaType.parse("application/image/png; charset=utf-8");
            MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (File file : files) {
                if (file.exists()) {
                    Log.i("imageName:", file.getName());//经过测试，此处的名称不能相同，如果相同，只能保存最后一个图片，不知道那些同名的大神是怎么成功保存图片的。
                    mbody.addFormDataPart("fileList", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                }
            }
            RequestBody requestBody =mbody.build();

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
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }

        }).start();

    }
    private void post1(){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/photo/share/add";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "5bb78418d8d9489a8702cbecfaa07cc9")
                    .add("appSecret", "184736cf550e4c5fd48f18ccbcb6b4f580f2a")
                    .add("Content-Type", "application/json")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("imageCode", imageCode);
            bodyMap.put("pUserId", userID);
            bodyMap.put("title", Title);
            bodyMap.put("content", Content);
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
                client.newCall(request).enqueue(callback1);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }

            try {
                System.out.println("（1）线程停滞，测试一下");
                Thread.sleep(1000)  ;
                System.out.println("（2）线程停滞，测试一下");
                finish();
            } catch (InterruptedException e) {
                System.out.println("线程出错");
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback callback1 = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Type jsonType = new TypeToken<ResponseBody1<Object>>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody1<Object> dataResponseBody = gson.fromJson(body,jsonType);
            System.out.println("检测一下是否成功上传？" + dataResponseBody.getCode());
            Log.d("info", dataResponseBody.toString());

        }
    };
    public static class ResponseBody1 <T> {

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

        public ResponseBody1(){}

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

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Type jsonType = new TypeToken<ResponseBody<Data>>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
            System.out.println("现在处于post的callback中");
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody<Data> dataResponseBody = gson.fromJson(body,jsonType);
            imageCode = dataResponseBody.data.getImageCode();
            Log.d("info", dataResponseBody.toString());
            post1();

        }
    };

    public static class Data{
        Long imageCode;
        List<String>imageUrlList;

        public Long getImageCode() {
            return imageCode;
        }

        public List<String> getImageUrlList() {
            return imageUrlList;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "imageCode=" + imageCode +
                    ", imageUrlList=" + imageUrlList +
                    '}';
        }
    }
    /**
     * http响应体的封装协议
     * @param <Data> 泛型
     */
    public static class ResponseBody <Data> {

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
        private Data data;

        public ResponseBody(){}

        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
        public Data getData() {
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
    public void back(View view){
        finish();
    }

}
