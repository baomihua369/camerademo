package com.example.camera_mvp;

import android.os.Handler;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GetQuoteInteractorImpl implements GetQuoteInteractor {
    //实现接口
    private List<String> arratList = Arrays.asList(
            "Be yourself. everyone else is already taken.",
            "A room without books is like a body without a soul.",
            "You only live once, but if you do it right, once is enough.",
            "Be the change that you wish to see in the world.",
            "If you tell the truth, you don't have to remember anything."
    );


    @Override
    public void getNextQuote(OnFinishedLisher lisher) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lisher.onFinished(getRandomString());
            }
        },1200);
    }

    private String getRandomString() {
        Random randomn = new Random();
        int index = randomn.nextInt(arratList.size());
        return arratList.get(index);
    }
}
