package com.example.www44.memorandum;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.www44.memorandum.MyAdapter;

import org.w3c.dom.Text;

/**
 * Created by www44 on 2017/9/2.implements AbsListView.OnScrollListener
 */

public class Fragment2 extends Fragment {
    private TextView tvinfo;
    private ImageButton imageButton;
    private View view;
    private Context context;
    private NoteDB noteDB;
    private SQLiteDatabase dbReader;
    private MyAdapter adapter;
    private ListView lv;
    private Cursor cursor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout layout;
    private TextView tv_noitem;
    float downX=0,downY=0;
    float upX=0,upY=0;

    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 1:if (swipeRefreshLayout.isRefreshing()){
                    adapter.notifyDataSetChanged();
                    selectDB();
//                    Toast.makeText(context,"调用scrollstatechange更新",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);//设置不刷新
                }
                    break;
                case 2:if (swipeRefreshLayout.isRefreshing()){
                    adapter.notifyDataSetChanged();
                    selectDB();
//                    Toast.makeText(context,"调用swipeRefreshLayout更新",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);//设置不刷新
                }
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        view = inflater.inflate(R.layout.fragment2,container,false);//解析布局文件
        initView();
        initEvent();
        selectDB();
        Log.e("info","Fragment2--onCreateView()");
        return view;
    }
    //初始化界面，绑定控件
    private void initView(){
        lv=view.findViewById(R.id.listview);
//        lv.setOnScrollListener(this);
        swipeRefreshLayout = view.findViewById(R.id.main_srl);
        imageButton=view.findViewById(R.id.imageButton2);
        tv_noitem=view.findViewById(R.id.tv_noitem);
        noteDB = new NoteDB(context);
        dbReader=noteDB.getReadableDatabase();
    }
    //监听事件以及点击事件
    private void initEvent(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context,"item点击事件",Toast.LENGTH_SHORT).show();
                cursor.moveToPosition(position);

                Button btn= (Button) view.findViewById(R.id.btn_del);
                if(btn.getVisibility() == View.VISIBLE){
                    btn.setVisibility(View.INVISIBLE);
                }
                Intent intent = new Intent(context,Select.class);
                intent.putExtra(NoteDB.ID,cursor.getInt(cursor.getColumnIndex(NoteDB.ID)));
                intent.putExtra(NoteDB.CONTENT,cursor.getString(cursor.getColumnIndex(NoteDB.CONTENT)));
                intent.putExtra(NoteDB.ARTICLE,cursor.getString(cursor.getColumnIndex(NoteDB.ARTICLE)));
                intent.putExtra(NoteDB.TIME,cursor.getString(cursor.getColumnIndex(NoteDB.TIME)));
                startActivity(intent);
            }
        });

//        lv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction())// 根据动作来执行代码
//                {
//
//                    case MotionEvent.ACTION_DOWN:// 按下
//                        v.setPressed(true);//item的点击效果
//                        downX = event.getX();
//                        downY=event.getY();
//                        return false;
////                        break;
//                    case MotionEvent.ACTION_MOVE:// 滑动
//                    break;
//                    case MotionEvent.ACTION_UP:// 松开
//                        v.setPressed(false);//item的点击效果
//                        Button bt_delete = (Button) v.findViewById(R.id.btn_del);
//                        upX = event.getX();
//                        upY=event.getY();
//                        if (Math.abs(upX - downX) > 50&&bt_delete.getVisibility()==View.GONE&&Math.abs(upY - downY) < 80) {
//                            bt_delete.setVisibility(View.VISIBLE);
////
//                        }else{
//                            bt_delete.setVisibility(View.GONE);
//                        }
//                        return false;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context,"item长按事件",Toast.LENGTH_SHORT).show();
                cursor.moveToPosition(position);
                Button btn_del=view.findViewById(R.id.btn_del);

                if(btn_del.getVisibility() == View.GONE){
                    btn_del.setVisibility(View.VISIBLE);
                    btn_del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbReader = noteDB.getWritableDatabase();
                            int rowid=dbReader.delete(NoteDB.TABLE_NAME,"_id="+cursor.getInt(cursor.getColumnIndex(NoteDB.ID)),null);
                            if(rowid!=0){
                                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                            }
                            selectDB();
                            dbReader.close();
                        }
                    });
                }
                else{
                    btn_del.setVisibility(View.GONE);
                }
                return true;
            }
        });

        // 设置刷新时进度动画变换的颜色，接收参数为可变长度数组。也可以使用setColorSchemeColors()方法。
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("infoswiperefreshlayout","newthread");
                new Thread(){
                    public void run(){
                        Message msg=new Message();
                        msg.what=2;
                        handler.sendMessage(msg);
                    };
                }.start();
            }
        });
    }
    //数据库查询
    public void selectDB(){
        /*
         * 通过query来实现查询
         * 查询返回的是一个游标（cursor）
         */
        dbReader=noteDB.getReadableDatabase();
        cursor = dbReader.query(noteDB.TABLE_NAME,null,null,null,null,null,null);
        adapter = new MyAdapter(context,cursor);

//        SimpleCursorAdapter adapter1=new SimpleCursorAdapter(context,R.layout.cell,cursor,new String[]{"CONTENT","ARTICLE","TIME"},
//                new int[]{R.id.list_content,R.id.list_article,R.id.list_time});

        lv.setAdapter(adapter);
        if(adapter.getCount()==0){
            tv_noitem.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
        }else {
            tv_noitem.setVisibility(View.INVISIBLE);
            tv_noitem.setHeight(0);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();

        dbReader.close();
    }

//    private int visibleLastIndex;//用来可显示的最后一条数据的索引
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if(adapter.getCount() == visibleLastIndex && scrollState == SCROLL_STATE_IDLE){
//            new Thread(){
//                public void run(){
//                    Log.e("scroll","newthread");
////                    Toast.makeText(context,"调用scrollstatechange更新",Toast.LENGTH_SHORT).show();
//                    Message msg=new Message();
//                    msg.what=1;
//                    handler.sendMessage(msg);
//                };
//            }.start();
//        }
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;//减去最后一个加载中那条
//    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("info","Fragment2--onCreate()");
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        selectDB();
        Log.e("info","Fragment2--onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("info","Fragment2--onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("info","Fragment2--onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("info","Fragment2--onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("info","Fragment2--onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("info","Fragment2--onDestroy()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("info","Fragment2--onActivityCreated()");
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e("info","Fragment2--onAttach()");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("info","Fragment2--onDetach()");
    }
}
