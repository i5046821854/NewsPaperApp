package edu.skku.cs.pa3.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import edu.skku.cs.pa3.contract.NewsContract;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResponseModel implements NewsContract.contractForModel {

    private String status;
    private ArrayList<News> news;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    @Override
    public void sendSearchRequest(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String keyword, String category, String lang) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
        HttpUrl.Builder urlBuilder;
        System.out.println("cate" + category);
        if(category == "") {
            if (keyword == null || keyword == "") {
                if (lang == null || lang.equals(""))
                    urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?language=en&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
                else
                    urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?language=" + lang + "&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
            } else {
                if (lang == null || lang.equals("")) {
                    urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?keywords=" + keyword + "&language=en&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
                } else {
                    urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?keywords=" + keyword + "&language=" +lang + "&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
                }
            }
        }
        else {
            if (keyword == null || keyword == "") {{
                if(lang == null || lang.equals(""))
                {
                    urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?language=en&category=" + category + "&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
                }
                else
                {
                    urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?language=" + lang + "&category=" + category + "&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
                }
            }
            } else {
                if(lang == null || lang.equals("")){
                    urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?keywords=" + keyword + "&language=en&category=" + category + "&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
                }
                else
                {
                    urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?keywords=" + keyword + "&language=" + lang+ "&category=" + category + "&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
                }
            }
        }
        String url = urlBuilder.build().toString();
        System.out.println(url);
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                news = gson.fromJson(myResponse, ResponseModel.class).getNews();
                onFinished_listener.onFinished(news, "search");
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onFinished_listener.onFinished(news, "fail");
                e.printStackTrace();
            }
        });

    }

    @Override
    public void sendScrapInitSearch(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String userSeq) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.0.2.2:8888/mypage").newBuilder();
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("https://4xq112eqqh.execute-api.ap-northeast-2.amazonaws.com/real/mypage").newBuilder();
        urlBuilder.addQueryParameter("userseq", userSeq);
        String url = urlBuilder.build().toString();
        System.out.println(url);
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                System.out.println(myResponse);
                news = gson.fromJson(myResponse, ResponseModel.class).getNews();
                onFinished_listener.onFinished(news, "ScrapInit");
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendScrapAdd(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String title, String author, String category, String published, String description, String orgUrl, String img, String userSeq) {
        OkHttpClient client = new OkHttpClient();
        News news = new News();
        news.setTitle(title);
        news.setAuthor(author);
        news.setCategory(new String[]{category});
        news.setPublished(published);
        news.setDescription(description);
        news.setUrl(orgUrl);
        news.setImage(img);
        news.setUserseq(Integer.parseInt(userSeq));

        Gson gson = new Gson();
        String json = gson.toJson(news, News.class);

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://4xq112eqqh.execute-api.ap-northeast-2.amazonaws.com/real/mypage").newBuilder();
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), json))
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                System.out.println(myResponse);
                final ResponseModel res = gson.fromJson(myResponse, ResponseModel.class);
                final String status = res.getStatus();
                System.out.println("status : " + status);
                if (status == "true") {
                    onFinished_listener.onFinished(new ArrayList<News>(), "insertYes");
                } else {
                    onFinished_listener.onFinished(new ArrayList<News>(), "insertNo");
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendScrapDelete(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String title, String userSeq) {

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.0.2.2:8888/delete").newBuilder();
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("https://4xq112eqqh.execute-api.ap-northeast-2.amazonaws.com/real/delete").newBuilder();

        urlBuilder.addQueryParameter("title", title);
        urlBuilder.addQueryParameter("userseq", userSeq);
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                String status = gson.fromJson(myResponse, ResponseModel.class).getStatus();
                if (status == "true") {
                    onFinished_listener.onFinished(new ArrayList<News>(), "deleteYes");
                } else {
                    onFinished_listener.onFinished(new ArrayList<News>(), "deleteNo");
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendInitRequest(NewsContract.contractForModel.onFinished_Listener onFinished_listener, String category) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.currentsapi.services/v1/search?category=" + category + "&language=en&apiKey=bUOAN1mHVyUahBl1LBy0uTDfcCtiYStsong5IkUzfUFErv5R").newBuilder();
        String url = urlBuilder.build().toString();
        System.out.println(url);
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                news = gson.fromJson(myResponse, ResponseModel.class).getNews();
                onFinished_listener.onFinished(news, "init");
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void sendScrapInitCheck(onFinished_Listener onFinished_listener, String title, String userseq) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.0.2.2:8888/check").newBuilder();
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("https://4xq112eqqh.execute-api.ap-northeast-2.amazonaws.com/real/check").newBuilder();

        urlBuilder.addQueryParameter("title", title);
        urlBuilder.addQueryParameter("userseq", userseq);

        String url = urlBuilder.build().toString();
        System.out.println(url);
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                String check = gson.fromJson(myResponse, ResponseModel.class).getStatus();
                if(check == "true")
                    onFinished_listener.onFinished(new ArrayList<News>(), "true");
                else
                    onFinished_listener.onFinished(new ArrayList<News>(), "false");
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }
}

