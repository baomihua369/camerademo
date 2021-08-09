package com.example.new_horizontalpickerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HorizontalPickerView picker = (HorizontalPickerView)findViewById(R.id.scrollPicker);
        String[] names = new String[]{"专业", "录像", "拍照", "人像", "夜景", "短视频", "全景", "文档模式", "更多"};
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,names);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,0,list){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                String value = getItem(position);
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(android.R.layout.simple_list_item_1,null);
                }
                TextView view = (TextView) convertView.findViewById(android.R.id.text1);
                view.setText(value);
                return convertView;
            }
        };

        picker.setAdapter(adapter);

        picker.setOnSelectedListener(new HorizontalPickerView.OnSelectedListener() {
            @Override
            public void selected(View v, int index) {
                ViewGroup group = (ViewGroup)picker.getChildAt(0);
                for (int i = 0; i < adapter.getCount(); i++) {
                    View view = (TextView)group.getChildAt(i);
                    /*TextView textView = findViewById()
                    Log.e("sunsun",view.);*/
                    if (i == index) {
                        view.setBackgroundColor(0xFFFF0000);
                    } else {
                        view.setBackgroundColor(group.getDrawingCacheBackgroundColor());
                    }
                }
            }
        });
    }
}