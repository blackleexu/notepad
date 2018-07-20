package com.example.www44.memorandum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by www44 on 2017/10/9.
 */

public class LogInActivity extends AppCompatActivity {
    private Button btn_login;
    private TextView tv_register;
    EventHandler eventHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login);
        MobSDK.init(this, "218423de33b70", "f5352147728d3b6d775b4aa306be2962");
        init();
    }

    private void init(){
        btn_login = (Button)findViewById(R.id.button_login);
        btn_login.setOnClickListener(onClickListener);
        tv_register = (TextView)findViewById(R.id.regist_mobile);
        tv_register.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button_login:
                    Intent intent =  new Intent();
                    intent.setClass(LogInActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.regist_mobile:
                    RegisterPage registerPage = new RegisterPage();
                    registerPage.setRegisterCallback(new EventHandler() {
                        public void afterEvent(int event, int result, Object data) {
                            // 解析注册结果
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                @SuppressWarnings("unchecked")
                                HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//                                String country = (String) phoneMap.get("country");
//                                String phone = (String) phoneMap.get("phone");
//                                Log.e("PhoneNumber", phone);
                                //如果验证成功就跳转到MainActivity
//                                Intent intent1 =  new Intent();
//                                intent1.setClass(LogInActivity.this,MainActivity.class);
//                                startActivity(intent1);
//                                finish();
                                // 提交用户信息（此方法可以不调用）
//                                 submitInfo(country, phone);
                            }
                            else {//验证失败
                                Toast.makeText(LogInActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    registerPage.show(LogInActivity.this);
                    break;
            }

        }
    };
}
