package com.example.myframelayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frame = findViewById(R.id.myFrameLayout);
        final Persion persion = new Persion(MainActivity.this);
        frame.addView(persion);
        //为小人添加触摸事件监听


        persion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //设置小人显示的位置
                persion.bitmapX = event.getX() - 150;
                persion.bitmapY = event.getY() - 150;
                Log.e("TGA", "X : " + (event.getX()) + ",Y : " + (event.getY()));
                //调用重绘方法
                persion.invalidate();
                if (event.getAction() == MotionEvent.ACTION_UP) {

                }
                return false;

            }
        });
    }
}