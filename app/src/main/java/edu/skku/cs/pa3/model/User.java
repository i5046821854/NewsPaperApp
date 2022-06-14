package edu.skku.cs.pa3.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import edu.skku.cs.pa3.contract.AuthContract;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User implements AuthContract.contractForModel {
    private String userSeq;
    private String name;
    private String password;
    private String categories;
    private String check;
    private String type;

    public String getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(String userSeq) {
        this.userSeq = userSeq;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void sendIdCheckRequest(onFinished_Listener onFinished_listener, String idStr) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.0.2.2:8888/idcheck").newBuilder();
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("https://4xq112eqqh.execute-api.ap-northeast-2.amazonaws.com/real/idcheck").newBuilder();

        urlBuilder.addQueryParameter("name", idStr);
        String url = urlBuilder.build().toString();
        System.out.println(url);
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final User user = gson.fromJson(myResponse, User.class);
                user.setType("id");
                onFinished_listener.onFinished(user);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendLoginRequest(onFinished_Listener onFinished_listener, String json) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.0.2.2:8888/login").newBuilder();
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("https://4xq112eqqh.execute-api.ap-northeast-2.amazonaws.com/real/login").newBuilder();

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
                final User user = gson.fromJson(myResponse, User.class);
                onFinished_listener.onFinished(user);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendRegRequest(onFinished_Listener onFinished_listener, String json) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.0.2.2:8888/adduser").newBuilder();
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("https://4xq112eqqh.execute-api.ap-northeast-2.amazonaws.com/real/adduser").newBuilder();

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
                final User user = gson.fromJson(myResponse, User.class);
                user.setType("reg");
                onFinished_listener.onFinished(user);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }
}
