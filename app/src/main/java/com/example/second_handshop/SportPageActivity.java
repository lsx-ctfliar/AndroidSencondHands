package com.example.second_handshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.second_handshop.R;
import com.example.second_handshop.service.good;
import com.example.second_handshop.service.nomal_user;
import com.example.second_handshop.util.GetKindGoods;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportPageActivity extends AppCompatActivity {
    public static final String NEWS_TITLE = "new_title";
    public static final String NEWS_AUTHOR = "news_author";
    private String[] titles = new String[100];
    private String[] authors = new String[100];
    private TypedArray images;

    private List<News> newsList = new ArrayList<>();
    private ListView lvNewsList;
    private NewsAdapter newsAdapter = null;

    public List<good> goodsList = new ArrayList<good>();
    public String[] price_list=new String[100];
    public String[] connent_list=new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_sport);


        System.out.println("加载列表布局没有问题");

//        不能写死这里，放开来，，然后这个就不只是运动类的获取商品的了
        //然后运动类型的布局文件也只是这个而已了，，
        String type = "文玩";

        //访问已经上传的商品的时候，只用传参数 typeid 和用户Id
        Intent intent = getIntent();
        String typeid0 = intent.getStringExtra("typeid");
        System.out.println("主页面绑定获取到的商品类型id"+typeid0);
        int typeid=Integer.parseInt(typeid0); // 强转成int类型

//563782

        int userId;
        userId = Integer.parseInt(nomal_user.getId());
        Log.d("info", "sdf" + userId);
        GetKindGoods getKindGoods = new GetKindGoods(typeid, userId);




        while (getKindGoods.status!=1)
        {
            System.out.println("请求数据等待中--------------");
        }

        String target = getKindGoods.res;
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
        int total = root2.get("total").getAsInt();
        System.out.println(current);
        System.out.println(total);
        //登陆成功之后之后 ，，在下面每循环一次就直接新建一个对象，储存数据
//            System.out.println(rec.get(1));
            //435962


        for (int i = 0; i < total; i++) {
            try {
                JsonElement element3 = parser.parse(String.valueOf(rec.get(i)));
                JsonObject root3 = element3.getAsJsonObject();
                System.out.println(rec.get(i));
                System.out.println(root3.get("id").getAsInt());
                System.out.println(root3.get("content").getAsString());
                //每次有个商品就直接新建一个新对象存储值
                //之后将对象值加入一个队列
                good good_one = new good();

                int price = root3.get("price").getAsInt();
                String content = root3.get("content").getAsString();

                //设置对象的值
                good_one.setPrice(price);
                good_one.setContent(content);

                goodsList.add(good_one);


            } catch (Exception e) {
                e.printStackTrace();
            }


//                GetKindGoods getKindGoods = new GetKindGoods(10, Integer.parseInt(nomal_user.getId()));
////        listView.setAdapter(adapter);
            while (getKindGoods.status != 1) {
                System.out.println("获取商品，等待中------------");
            }


//                System.out.println("一个商品的id和content"+getKindGoods.goodsList[0]);
            //获取列表里面的对象



            initData();

            System.out.println("商品列表" + getKindGoods.goodsList);
//        在 MainActivity 类中定义了 titles、authors 两个字符串数组，并使⽤getResources()得
//到 Resources 对象，并通过该对象的getStringArray的⽅法获取 arrays.xml ⽂
//件中定义的字符串数组资源。
            newsAdapter = new NewsAdapter(
                    SportPageActivity.this,
                    R.layout.list_item,
                    newsList);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
//                android.R.layout.simple_list_item_1,titles);
            ListView lvNewsList = findViewById(R.id.lv_news_list);
            lvNewsList.setAdapter(newsAdapter);


        }
    }
        private void initData () {
            int length = goodsList.size();

            for(int i=0;i<goodsList.size();i++)
            {
                //将拿到的数据对象列表，转成两个分别类型相同的数据列表

                titles[i] = goodsList.get(i).getContent();
                authors[i]=String.valueOf(goodsList.get(i).getPrice());    //将获取到的数字转换成字符串
                System.out.println("价格-------"+goodsList.get(i).getPrice());
//                connent_list.add(goodsList.get(i).getContent());
//                price_list.add(goodsList.get(i).getPrice());
         }



//            titles = getResources().getStringArray(R.array.titles);
//            authors = getResources().getStringArray(R.array.authors);

//            titles = connent_list;
//            authors=price_list;
            System.out.println("!!!!!!!!!!"+ titles[1]);



            images = getResources().obtainTypedArray(R.array.images);

              String str = Arrays.toString(titles);




            for (int i = 0; i < length; i++) {
                News news = new News();
                news.setmTitle(titles[i]);
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
            }


            public View getView(int position, View convertView, ViewGroup parent) {
                News news = getItem(position);
                View view;


                ViewHolder viewHolder;

                if (convertView == null) {
                    view = LayoutInflater.from(getContext())
                            .inflate(resourceId, parent, false);

                    viewHolder = new ViewHolder();
                    viewHolder.tvTitle = view.findViewById(R.id.tv_title);
                    viewHolder.tvAuthor = view.findViewById(R.id.tv_subtitle);
                    viewHolder.ivImage = view.findViewById(R.id.iv_image);

                    view.setTag(viewHolder);
                } else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag();
                }

                viewHolder.tvTitle.setText(news.getTitle());
                viewHolder.tvAuthor.setText(news.getAuthor());
                viewHolder.ivImage.setImageResource(news.getImageId());

                return view;

            }


        }

    }
