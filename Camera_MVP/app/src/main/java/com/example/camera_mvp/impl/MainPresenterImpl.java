package com.example.camera_mvp;

public class MainPresenterImpl implements MainPresenter,GetQuoteInteractor.OnFinishedLisher{
    private MainView mainView;//初始化View
    private GetQuoteInteractor getQuoteInteractor;//初始化Model

    //构造方法中，应该传递View和GetQuoteInteractorImpl对象
    public MainPresenterImpl(MainView mainView , GetQuoteInteractor getQuoteInteractor){
        this.mainView = mainView;
        this.getQuoteInteractor = getQuoteInteractor;
    }

    //重写MainPresenter中的方法
    @Override
    public void onButtonClick() {
        if (mainView != null){
            mainView.showProgress();
        }
        getQuoteInteractor.getNextQuote(this);//将MainPresenterImpl实例传入
    }

    @Override
    public void onDestroy() {
        mainView = null;

    }

    @Override
    public void onFinished(String s) {
        //Linster中的方法
        if (mainView != null){
            mainView.setQuote(s);
            mainView.hideProgress();
        }
    }
}
