package com.example.horizontalscrollview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout;
    private String[] names;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = getWindow().getDecorView();
        init();
        bindData(v);

    }


    private void init() {
        // 初始化布局中的控件
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        linearLayout = findViewById(R.id.horizontalScrollViewItemContainer);
        // item的值
        names = new String[]{"专业", "录像", "拍照", "人像", "夜景", "短视频", "全景", "文档模式", "更多"};
        data = new ArrayList<>();
    }

    private void bindData(View view) {
        //为textView设置属性
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(80, 10, 80, 10);
        //将字符串数组和集合绑定起来
        Collections.addAll(data, names);

        for (int i = 0; i < data.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(data.get(i));
            textView.setTextColor(Color.WHITE);
            textView.setLayoutParams(layoutParams);

            textView.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //手指离开当前item时，居中
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        getCenterItem(v);
                    }
                    return true;
                }
            });
            linearLayout.addView(textView);
            linearLayout.invalidate();
        }
    }

    private void getCenterItem(View view) {
        //获取HorzontalScrollView的宽度
        int hsvWidth = horizontalScrollView.getWidth();
        Log.e("TAG","hsvWidth :" +hsvWidth);
        //获取textView左边缘位置
        int textViewLeft = view.getLeft();
        Log.e("TAG","textViewLeft :" + textViewLeft);
        //获取textView item的宽度
        int textViewWidth = view.getWidth();
        Log.e("TAG","textViewWidth :" + textViewWidth);
        //计算偏移量
        int offset = textViewLeft + textViewWidth/2 - hsvWidth/2;
        Log.e("TAG","offset :" + offset);

        //横向平滑滚动偏移
        horizontalScrollView.smoothScrollTo(offset,0);

        String s = "CenterLocked Item: " + ((TextView)view).getText();
        Log.e("TAG","s :" + s);

    }
}