package com.example.horizontalscrollview;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.horizontalscrollview.R;

import java.util.ArrayList;
import java.util.List;

public class HorActivity extends AppCompatActivity {

    private LinearLayout mGallery;
    private String[] mModelName;
    private LayoutInflater mInflater;
    private List<TextView> mTextViewList;
    private HorizontalScrollView horizontalScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hor);
        mInflater = LayoutInflater.from(this);
        mModelName = new String[]{"拍照", "录像","慢动作","夜景","全景","人像","专业","更多"};
        mGallery = findViewById(R.id.gallery);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextViewList = new ArrayList<>();
        for (int i = 0; i < mModelName.length; i++) {
            View inflate = mInflater.inflate(R.layout.activity_hor, mGallery, false);
            TextView textView = new TextView(this);
            textView.setText(mModelName[i]);
            textView.setOnClickListener(view->{

                for (TextView tv : mTextViewList) {
                    tv.setTextColor(Color.BLACK);
                }
                textView.setTextColor(Color.YELLOW);
                getCenterItem(textView);
            });

            textView.setPadding(80,0,0,0);
            mGallery.addView(textView);
            mTextViewList.add(textView);
        }
    }

    public static int getScreenCenterIndex(Context context) {
        return (context.getResources().getDisplayMetrics().widthPixels)/2;
    }

    private void getCenterItem(View view){
        int hroWidth = horizontalScrollView.getWidth();
        int left = view.getLeft();
        int tvWidth = view.getWidth();
        int offset = left + tvWidth/2 + hroWidth/2;
        horizontalScrollView.smoothScrollBy(offset,0);
    }
}