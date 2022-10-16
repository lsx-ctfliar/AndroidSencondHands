package com.example.second_handshop;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.second_handshop.service.AppidAndSecred_iml;
import com.example.second_handshop.service.nomal_user;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyPublishActivity extends AppCompatActivity {

    private final Gson gson = new Gson();
    public String resource;
    private TypedArray images;
    private String[] titles = new String[100];
    private String[] authors = new String[100];
    private String[] list_addr= new String[100];

    private List<News> newsList = new ArrayList<>();
    String[] myrecord_content=new String[20];
    String[] myrecord_price=new String[20];
    private int total;
    private NewsAdapter newsAdapter = null;
    //控制进程结束之后再加载
    private int status  = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("info", "!!!!!!!!!!!!!!!!!!!!!");
        setContentView(R.layout.acitvity_sport);
        Log.d("info", "!!!!!!!!!!!!!!!!--------------");
        //先请求数据，结束后返回处理
        get();

        while(status!=1)
        {
            System.out.println("加载个人用户已发布的商品还未完成-------------");
        }
        //加载完数据之后进行处理
        String target = resource;
        System.out.println("------------"+target);
        JsonParser parser = new JsonParser();
        // 2.获得 根节点元素
        JsonElement element = parser.parse(target);
        // 3.根据 文档判断根节点属于 什么类型的 Gson节点对象
        JsonObject root = element.getAsJsonObject();
        String code = root.get("code").getAsString();
        String msg = root.get("msg").getAsString();
        System.out.println(code + "         777777777777");

//            Log.d("info", "msg: " + msg);
        JsonElement dataElement = root.get("data");
        JsonElement element2 = parser.parse(String.valueOf(dataElement));
        // 3.根据 文档判断根节点属于 什么类型的 Gson节点对象
        JsonObject root2 = element2.getAsJsonObject();
        JsonArray rec = root2.get("records").getAsJsonArray();

        int current = root2.get("current").getAsInt();
        total = root2.get("total").getAsInt();
        System.out.println(current);
        System.out.println(total);

        System.out.println("拿到的记录：------------"+rec);
//
//        JsonObject object = rec.getAsJsonObject();
//        System.out.println("拿到的记录：！！！！！！"+object);

        String data=rec.toString();
        data.replace("[","");
        data.replace("]","");

        //第一个分割之后，一共有16×total个 键值对
        String[] data2=data.split(",");
        for(int i =0;i<total;i++)
        {
            System.out.println("字符串"+data2[i]);
            System.out.println("\n");

        }
//
//        data.split(",")[4].split(":")[1].replace("\"","");
//        data.split(",")[5].split(":")[1].replace("\"","");

        for(int i=0;i<total;i++)
        {
            int flag = 4;
            int tar=i*16+flag;
            String str = data2[tar];
            myrecord_content[i]=str.split(":")[1].replace("\"","");
        }
        for(int i=0;i<total;i++)
        {
            int flag = 5;
            int tar=i*16+flag;
            String str = data2[tar];
            myrecord_price[i]=str.split(":")[1];
        }
        for(int i=0;i<total;i++)
        {
            int flag=6;
            int tar =i*16+flag;
            String str = data2[tar];
            list_addr[i]="地址："+str.split(":")[1].replace("\"","");
        }

        for(int i=0;i<total;i++)
        {
            System.out.println("  +++++++获取到的两个字符串数组"+"---第"+i+"组");
            System.out.println(myrecord_content[i]);
            System.out.println(myrecord_price[i]);
            System.out.println(list_addr[i]);
        }


        initData();

        newsAdapter = new NewsAdapter(
                MyPublishActivity.this,
                R.layout.list_item,
                newsList);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
//                android.R.layout.simple_list_item_1,titles);
        ListView lvNewsList = findViewById(R.id.lv_news_list);
        lvNewsList.setAdapter(newsAdapter);

    }



    private void initData () {


        for(int i=0;i<total;i++)
        {
            //将拿到的数据对象列表，转成两个分别类型相同的数据列表

            titles[i] = "商品描述："+myrecord_content[i];
            authors[i]="价格："+String.valueOf(myrecord_price[i]);    //将获取到的数字转换成字符串

//                connent_list.add(goodsList.get(i).getContent());
//                price_list.add(goodsList.get(i).getPrice());
        }

        for(int i=0;i<total;i++)
        {
            System.out.println("   获取到的全部数据            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(titles[i]);
            System.out.println(authors[i]);
        }



//            titles = getResources().getStringArray(R.array.titles);
//            authors = getResources().getStringArray(R.array.authors);

//            titles = connent_list;
//            authors=price_list;




        images = getResources().obtainTypedArray(R.array.images);






        for (int i = 0; i < total; i++) {
            News news = new News();
            news.setmTitle(titles[i]);
            news.setmAddr(list_addr[i]);
            news.setAuthor(authors[i]);
            news.setmImageId(images.getResourceId(i, 0));
            newsList.add(news);
        }



    }

    public class News {
        private String mTitle;
        private String mAuthor;
        private String mContent;
        private int mImageId;

        public String getmAddr() {
            return mAddr;
        }

        public void setmAddr(String mAddr) {
            this.mAddr = mAddr;
        }

        private String mAddr;

        public String getTitle() {
            return mTitle;

        }

        public void setmTitle(String title) {
            this.mTitle = title;
        }

        public String getAuthor() {
            return mAuthor;

        }

        public void setAuthor(String author) {
            this.mAuthor = author;
        }

        public String getContent() {
            return mContent;

        }

        public void setmContent(String content) {
            this.mContent = content;
        }

        public int getImageId() {
            return mImageId;

        }

        public void setmImageId(int imageid) {
            this.mImageId = imageid;
        }



    }


    public class NewsAdapter extends ArrayAdapter<News> {
        private List<News> mNewsData;
        private Context mContext;
        private int resourceId;


        public NewsAdapter(Context context, int resourceId, List<News> data) {
            super(context, resourceId, data);
            this.mContext = context;
            this.mNewsData = data;
            this.resourceId = resourceId;

        }

        class ViewHolder {
            TextView tvTitle;
            TextView tvAuthor;
            ImageView ivImage;
            TextView tvAddr;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            News news = getItem(position);
            View view;


            ViewHolder viewHolder;

            if (convertView == null) {
                view = LayoutInflater.from(getContext())
                        .inflate(resourceId, parent, false);

                viewHolder = new MyPublishActivity.NewsAdapter.ViewHolder();
                viewHolder.tvTitle = view.findViewById(R.id.tv_title);
                viewHolder.tvAuthor = view.findViewById(R.id.tv_subtitle);
                viewHolder.ivImage = view.findViewById(R.id.iv_image);
                viewHolder.tvAddr=view.findViewById(R.id.tv_adrr);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.tvTitle.setText(news.getTitle());
            viewHolder.tvAuthor.setText(news.getAuthor());
            viewHolder.ivImage.setImageResource(news.getImageId());
            viewHolder.tvAddr.setText(news.getmAddr());
            return view;

        }


    }













    private void get(){
        new Thread(() -> {

            // url路径
            //构造进去用户id
            String url = "http://47.107.52.7:88/member/tran/goods/myself?userId="+ nomal_user.getId();

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
            Log.d("info", body);
            resource=body;
            //已经获取数据之后，将状态改为完成态，，然后在oncreate方法里面进行处理
            status=1;
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
