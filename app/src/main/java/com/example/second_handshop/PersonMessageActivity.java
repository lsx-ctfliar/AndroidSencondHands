package com.example.second_handshop;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class PersonMessageActivity extends AppCompatActivity implements View.OnClickListener{

    private Button myrecord;
    private RadioButton myhome;
    private RadioButton myadd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_personal_msg);


    myrecord = findViewById(R.id.person_myShow);
    myrecord.setOnClickListener(this);


    myadd=(RadioButton)findViewById(R.id.persion_add);


    myhome=(RadioButton)findViewById(R.id.persion_home);



    //设置  在case里面点击跳转会有问题
    myadd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PersonMessageActivity.this,PublishGoodsActivity.class);
                startActivity(intent);
        }
    });


    myhome.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PersonMessageActivity.this,MainPageActivity.class);
                startActivity(intent);
        }
    });




    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //跳转到  展示所有个人已经发布的商品的页面
            case R.id.person_myShow:
                Intent intent = new Intent(PersonMessageActivity.this,MyPublishActivity.class);
                startActivity(intent);


//           //还要设置在个人信息页面跳转回首页  和添加商品页面
//            在这里设置  下框的跳转，跳转回出问题，很奇怪
//            case R.id.persion_home:
//                Intent intent2 = new Intent(PersonMessageActivity.this,MainPageActivity.class);
//                startActivity(intent2);
//
//            case R.id.persion_add:
//                Intent intent3 = new Intent(PersonMessageActivity.this,PublishGoodsActivity.class);
//                startActivity(intent3);
        }

    }
}
