package edu.skku.cs.pa3.presenter;

import java.util.ArrayList;

import edu.skku.cs.pa3.contract.NewsContract;
import edu.skku.cs.pa3.model.News;
import edu.skku.cs.pa3.model.ResponseModel;

public class NewsPresenter implements NewsContract.contractForPresenter {

    NewsContract.contractForView view;
    ResponseModel res;

    public NewsPresenter(NewsContract.contractForView view)
    {
        this.view = view;
        this.res = new ResponseModel();
    }


    @Override
    public void doInitSearch(String category) {
        System.out.println(category);
        res.sendInitRequest(new NewsContract.contractForModel.onFinished_Listener() {
            @Override
            public void onFinished(ArrayList<News> response, String type) {
                view.showResult(response, type);
            }
        }, category);
    }

    @Override
    public void onSearchBtnTouched(String keyword, String category, String lang) {
        res.sendSearchRequest(new NewsContract.contractForModel.onFinished_Listener(){
            @Override
            public void onFinished(ArrayList<News> response, String type) {
                view.showResult(response, type);
            }
        }, keyword, category, lang);
    }

    @Override
    public void onScrapBtnTouched(String title, String author, String category, String published, String description, String orgUrl, String img, String userSeq) {
        res.sendScrapAdd(new NewsContract.contractForModel.onFinished_Listener(){
            @Override
            public void onFinished(ArrayList<News> response, String type) {
                view.showResult(response, type);
            }
        }, title,  author, category, published, description, orgUrl, img, userSeq);
    }

    @Override
    public void onUndoBtnTouched(String title, String userSeq) {
        res.sendScrapDelete(new NewsContract.contractForModel.onFinished_Listener(){
            @Override
            public void onFinished(ArrayList<News> response, String type) {
                view.showResult(response, type);
            }
        }, title, userSeq);
    }

    @Override
    public void doScrapSearch(String userSeq) {
        res.sendScrapInitSearch(new NewsContract.contractForModel.onFinished_Listener() {
            @Override
            public void onFinished(ArrayList<News> response, String type) {
                view.showResult(response, type);
            }
        },userSeq);
    }

    @Override
    public void doScrapCheck(String title,String userseq) {
        res.sendScrapInitCheck(new NewsContract.contractForModel.onFinished_Listener() {
            @Override
            public void onFinished(ArrayList<News> response, String type) {
                view.showResult(response, type);
            }
        },title, userseq);
    }
}
