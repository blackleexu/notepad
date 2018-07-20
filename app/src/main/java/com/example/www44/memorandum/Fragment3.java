package com.example.www44.memorandum;

import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by www44 on 2017/9/2.
 */

public class Fragment3 extends Fragment {
    private TextView tvinfo;
    private View view;
    private Context context;
    private Button button_copy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment3,container,false);//解析布局文件

        button_copy=(Button)view.findViewById(R.id.button_copy);
        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText("446634431");
                Toast.makeText(context,"已复制作者QQ到粘贴板",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
