package com.example.camera_mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.camera_mvp.impl.GetQuoteInteractorImpl;
import com.example.camera_mvp.impl.MainPresenterImpl;

public class MainActivity extends AppCompatActivity implements MainView{
    private TextView mTextView;
    private Button mButton;
    private ProgressBar mProgressBar;
    MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.button);
        mProgressBar = findViewById(R.id.progressBar);
        mMainPresenter = new MainPresenterImpl(this,new GetQuoteInteractorImpl());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPresenter.onButtonClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.onDestroy();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setQuote(String s) {
        mTextView.setText(s);
    }
}