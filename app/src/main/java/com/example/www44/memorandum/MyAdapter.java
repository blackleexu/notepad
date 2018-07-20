package com.example.www44.memorandum;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by www44 on 2017/9/5.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout layout;
    float downX=0,downY=0;
    float upX=0,upY=0;
    public MyAdapter(Context context,Cursor cursor){
        this.context=context;
        this.cursor=cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        //避免重复解析布局
        if(view==null){
            view=LayoutInflater.from(context).inflate(R.layout.cell,viewGroup,false);
            holder=new ViewHolder();
            holder.contenttv = view.findViewById(R.id.list_content);
            holder.articletv = view.findViewById(R.id.list_article);
            holder.timetv = view.findViewById(R.id.list_time);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction())// 根据动作来执行代码
//                {
//                    case MotionEvent.ACTION_MOVE:// 滑动
////                        Toast.makeText(context, "move...", Toast.LENGTH_SHORT).show();
//                        return true;
////                        break;
//                    case MotionEvent.ACTION_DOWN:// 按下
//                        v.setPressed(true);//item的点击效果
////                        Toast.makeText(context, "down...", Toast.LENGTH_SHORT).show();
//                        downX = event.getX();
//                        downY=event.getY();
//                        return true;//不能改
////                        break;
//                    case MotionEvent.ACTION_UP:// 松开
//                        v.setPressed(false);//item的点击效果
//
//                        Button bt_delete = (Button) v.findViewById(R.id.btn_del);
//                        upX = event.getX();
//                        upY=event.getY();Toast.makeText(context, "up...X:" + Math.abs(upX-downX)+"up...Y:"+Math.abs(upY - downY), Toast.LENGTH_SHORT).show();
//                        if (Math.abs(upX - downX) > 50&&bt_delete.getVisibility()==View.GONE&&Math.abs(upY - downY) < 80) {
//
//                            bt_delete.setVisibility(View.VISIBLE);
//                            return false;
//                        }else{
//                            bt_delete.setVisibility(View.GONE);
//                            return true;
//                        }
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });

        cursor.moveToPosition(position);
        //通过游标内部对应列名来拿到各个列的数据并显示到对应的控件中
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String article = cursor.getString(cursor.getColumnIndex("article"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        holder.contenttv.setText(content);
        holder.articletv.setText(article);
        holder.timetv.setText(time);
        return view;
    }
}
