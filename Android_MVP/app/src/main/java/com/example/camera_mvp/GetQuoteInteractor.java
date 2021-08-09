package com.example.camera_mvp;

public interface GetQuoteInteractor {
    interface OnFinishedLisher{
        void onFinished(String s);
    }

    void getNextQuote(OnFinishedLisher lisher);
}
