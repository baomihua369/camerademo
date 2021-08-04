package com.example.myframelayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class Persion extends View {
    public float bitmapX;//X坐标
    public float bitmapY;//Y坐标

    public Persion(Context context) {
        super(context);
        //设置初始位置
        bitmapX = 0;
        bitmapY = 200;
    }

    //View类的onDraw方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建，并且实例化Paint的对象
        Paint paint = new Paint();
        //生成图片的位图对象
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.littleman);
        //绘制小人
        canvas.drawBitmap(bitmap,bitmapX,bitmapY,paint);
        //判断图片是否收回，如果没有的话强制收回
        if (bitmap.isRecycled()){
            bitmap.recycle();
        }
    }
}
