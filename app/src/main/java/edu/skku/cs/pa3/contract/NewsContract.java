package edu.skku.cs.pa3.contract;

import java.util.ArrayList;

import edu.skku.cs.pa3.model.News;

public interface NewsContract {


    interface contractForView{
        void showResult(ArrayList<News> response, String type);
    }

    interface contractForModel{
        interface onFinished_Listener{
            void onFinished(ArrayList<News> response, String type);
        }

        void sendSearchRequest(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String keyword, String category, String lang);
        void sendScrapAdd(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String title, String author, String category, String published, String description, String orgUrl, String img, String userSeq );
        void sendScrapDelete(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String title, String userSeq);
        void sendInitRequest(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String category);
        void sendScrapInitSearch(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String category);
        void sendScrapInitCheck(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String title, String userseq);
    }

    interface contractForPresenter{
        void onSearchBtnTouched(String keyword, String category, String lang);
        void onUndoBtnTouched(String title, String userSeq);
        void onScrapBtnTouched(String title, String author, String category, String published, String description, String orgUrl, String img, String userSeq);
        void doInitSearch(String category);
        void doScrapSearch(String userSeq);
        void doScrapCheck(String title, String userseq);
    }
}
