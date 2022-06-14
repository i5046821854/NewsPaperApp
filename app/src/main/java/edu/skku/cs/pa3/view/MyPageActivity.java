package edu.skku.cs.pa3.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.skku.cs.pa3.NewsListViewAdapter;
import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.contract.NewsContract;
import edu.skku.cs.pa3.model.News;
import edu.skku.cs.pa3.presenter.NewsPresenter;

public class MyPageActivity extends AppCompatActivity implements NewsContract.contractForView {

    ListView listView;
    private ArrayList<News> newsList;
    NewsContract.contractForPresenter presenter;
    Button backBtn;
    private String category;
    public final static String EXT_CATEGORY = "CATEGORY";
    public final static  String EXT_USER_SEQ = "SEQ";

    String userSeq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);
        presenter = new NewsPresenter(this);
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        userSeq = intent.getStringExtra(NewslistActivity.EXT_USER_SEQ);
        category = intent.getStringExtra(NewslistActivity.EXT_CATEGORY);
        System.out.println(category);
        listView = findViewById(R.id.scrapListView);
        presenter.doScrapSearch(userSeq);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> {
            Intent intent2 = new Intent(MyPageActivity.this, NewslistActivity.class);
            intent2.putExtra(EXT_CATEGORY, category);
            intent2.putExtra(EXT_USER_SEQ, userSeq);
            startActivity(intent2);
        });
    }

    @Override
    public void showResult(ArrayList<News> response, String type) {
        NewsListViewAdapter newsListViewAdapter = new NewsListViewAdapter(getApplicationContext(), response, userSeq);
        MyPageActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(newsListViewAdapter);
            }
        });
    }
}
