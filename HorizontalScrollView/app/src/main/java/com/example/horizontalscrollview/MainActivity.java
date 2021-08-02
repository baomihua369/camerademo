package com.example.horizontalscrollview;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //GLSurfaceView gls = new GLSurfaceView(this);
        //gls.setRenderer(new RendererImpl());
        initHorizontal();
    }

    private void initHorizontal() {
        AutoCenterHorizontalScrollView autoCenterHorizontalScrollView;
        autoCenterHorizontalScrollView = findViewById(R.id.achs_test);
        //测试用的随机字符串集合
        List<String> names =new ArrayList<>();
        for(int i=0;i<50;i++){
            String a = ""+i;
            for(int j=0;j<i%4;j++){
                a=a+"A";
            }
            names.add(a);
        }
        //adapter去处理itemView
        HorizontalAdapter hadapter = new HorizontalAdapter(this,names);
        autoCenterHorizontalScrollView.setAdapter(hadapter);
        autoCenterHorizontalScrollView.setOnSelectChangeListener(new AutoCenterHorizontalScrollView.OnSelectChangeListener() {
            @Override
            public void onSelectChange(int position) {
                ((TextView) findViewById(R.id.tv_index)).setText("当前"+position);
            }
        });
        autoCenterHorizontalScrollView.setCurrentIndex(39);
    }
}