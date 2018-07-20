package com.example.www44.memorandum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.githang.statusbar.StatusBarTools.getStatusBarHeight;

/**
 * Created by www44 on 2017/9/8.
 */

public class Select extends Activity implements View.OnClickListener{

    private Button button_delt,button_bak;
    private EditText tv1,tv2;
    private TextView btn_update;
    private NoteDB noteDB;
    private SQLiteDatabase dbwriter;
    private int rowid;
    private int result;
    private String tvC,tvA;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        initView();
        initEvent();
        Log.e("info", "select==onCreate");
        if (Build.VERSION.SDK_INT <= 19) {
            setStatusBarColor4_4(this, ContextCompat.getColor(this, R.color.colorPrimaryDark) );
        } else {
            setStatusBarColor(this,ContextCompat.getColor(this, R.color.colorPrimaryDark) );
        }
    }

    private void initView(){
        button_delt=findViewById(R.id.button_delt);
        button_bak=findViewById(R.id.button_bak);
        tv1=findViewById(R.id.textView_select);
        tv2=findViewById(R.id.textView_select2);
        btn_update=findViewById(R.id.btn_update);
        noteDB = new NoteDB(this);
        dbwriter = noteDB.getWritableDatabase();

        rowid=getIntent().getIntExtra(NoteDB.ID,0);
        tvC=getIntent().getStringExtra(NoteDB.CONTENT);
        tvA=getIntent().getStringExtra(NoteDB.ARTICLE);
        tv1.setText(tvC);
        tv2.setText(tvA);
    }

    private void initEvent(){
        button_delt.setOnClickListener(this);
        button_bak.setOnClickListener(this);
        btn_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_delt:
                deleteDatebase();
                break;
            case R.id.button_bak:
                if(!tv1.getText().toString().equals(tvA.toString())||!tv2.getText().toString().equals(tvC.toString())){
                    new AlertDialog.Builder(this)
                            .setTitle("提示！")
                            .setMessage("内容有变化,是否放弃修改内容")
                            .setPositiveButton("返回保存", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create().show();
                }else{
                    finish();
                }

                break;
            case R.id.btn_update:
                updateDatabase();
                break;
        }
    }

    public void deleteDatebase(){
        result=dbwriter.delete(NoteDB.TABLE_NAME,"_id="+getIntent().getIntExtra(NoteDB.ID,0),null);
        if(result>0){
            Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"删除失败",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDatabase(){
        ContentValues values=new ContentValues();
        values.put(NoteDB.CONTENT,tv1.getText().toString());
        values.put(NoteDB.ARTICLE,tv2.getText().toString());
        values.put(NoteDB.TIME,getTime());
        result=dbwriter.update(NoteDB.TABLE_NAME,values,"_id="+rowid,null);
        if(result>0){
            Toast.makeText(this,"修改提交成功",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"修改提交失败",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.e("info", "select==onStart");
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.e("info", "select==onRestart");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e("info", "select==onResume");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.e("info", "select==onPause");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.e("info", "select==onStop");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e("info", "select==onDestroy");
    }
    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }
    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(statusColor);
        }
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
    @Override
    public void onBackPressed() {
//        Log.d(TAG, "onBackPressed()");
        if (!tv1.getText().toString().equals(""+tvA.trim()) || !tv2.getText().toString().equals(""+tvA.trim())) {
            new AlertDialog.Builder(this)
                    .setTitle("提示！")
                    .setMessage("内容有变化,是否放弃修改内容")
                    .setPositiveButton("返回保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
//            super.onBackPressed();
        }else{
            finish();
        }
//        Toast.makeText(this,tvA,Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,tv1.getText().toString(),Toast.LENGTH_SHORT).show();
    }
    static void setStatusBarColor4_4(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

//First translucent status bar.
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = getStatusBarHeight(activity);

        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
            //如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
            if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                //不预留系统空间
                ViewCompat.setFitsSystemWindows(mChildView, false);
                lp.topMargin += statusBarHeight;
                mChildView.setLayoutParams(lp);
            }
        }

        View statusBarView = mContentView.getChildAt(0);
        if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
            //避免重复调用时多次添加 View
            statusBarView.setBackgroundColor(statusColor);
            return;
        }
        statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        statusBarView.setBackgroundColor(statusColor);
//向 ContentView 中添加假 View
        mContentView.addView(statusBarView, 0, lp);
    }
}
