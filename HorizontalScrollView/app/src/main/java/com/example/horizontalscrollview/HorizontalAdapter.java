package com.example.horizontalscrollview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HorizontalAdapter implements AutoCenterHorizontalScrollView.HAdapter{
    List<String> names = new ArrayList<>();
    private Context context;

    public HorizontalAdapter(Context context, List<String> names) {
        this.names = names;
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemView(int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_warp2, null, false);
        HViewHolder hViewHolder = new HViewHolder(v);
        hViewHolder.textView.setBackgroundColor(Color.BLACK);
        hViewHolder.textView.setText(names.get(i));
        return hViewHolder;
    }

    @Override
    public void onSelectStateChanged(RecyclerView.ViewHolder viewHolder, int position, boolean isSelected) {
        if (isSelected) {
            ((HViewHolder) viewHolder).textView.setBackgroundColor(Color.RED);
        } else {
            ((HViewHolder) viewHolder).textView.setBackgroundColor(Color.BLACK);
        }
    }

    public static class HViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public HViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_tab_name);
        }
    }
}
