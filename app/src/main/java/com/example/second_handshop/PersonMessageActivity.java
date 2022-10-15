package com.example.second_handshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PersonMessageActivity extends AppCompatActivity implements View.OnClickListener{

    private Button myrecord;
    private Button myhome;
    private Button myadd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_personal_msg);


    myrecord = findViewById(R.id.myShow);
    myrecord.setOnClickListener(this);


    myadd=findViewById(R.id.rb_add);
    myadd.setOnClickListener(this);

    myhome=findViewById(R.id.rb_home);
    myhome.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //跳转到  展示所有个人已经发布的商品的页面
            case R.id.myShow:
                Intent intent = new Intent(PersonMessageActivity.this,MyPublishActivity.class);
                startActivity(intent);


                //还要设置在个人信息页面跳转回首页  和添加商品页面
            case R.id.rb_home:
                Intent intent2 = new Intent(PersonMessageActivity.this,PublishGoodsActivity.class);
                startActivity(intent2);

            case R.id.rb_add:
                Intent intent3 = new Intent(PersonMessageActivity.this,MainPageActivity.class);
                startActivity(intent3);
        }

    }
}
