package edu.skku.cs.pa3.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.skku.cs.pa3.NewsListViewAdapter;
import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.contract.NewsContract;
import edu.skku.cs.pa3.model.News;
import edu.skku.cs.pa3.presenter.NewsPresenter;

public class NewsDetailActivity extends AppCompatActivity implements NewsContract.contractForView {

    TextView titleTxt;
    TextView authorTxt;
    TextView categoryTxt;
    TextView publishedTxt;
    TextView descTxt;
    ImageView imgView;
    Button readFull;
    Button scrap;
    Button myPageBtn;
    Boolean checked = false;
    public final static String EXT_USER_SEQ = "SEQ";
    public final static String EXT_CATEGORY = "CATEGORY";
    NewsContract.contractForPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        presenter = new NewsPresenter(this);
        initView();
    }

    private void initView() {
        titleTxt = (TextView) findViewById(R.id.titleDetail);
        authorTxt = (TextView) findViewById(R.id.authorDetail);
        categoryTxt = (TextView) findViewById(R.id.categoryDetail);
        publishedTxt = (TextView) findViewById(R.id.publishedDetail);
        descTxt = (TextView) findViewById(R.id.description);
        readFull = (Button) findViewById(R.id.readFullBtn);
        myPageBtn = (Button) findViewById(R.id.myPage2);
        scrap = (Button) findViewById(R.id.scrapBtn);
        imgView = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        String title = intent.getStringExtra(NewsListViewAdapter.EXT_TITLE);
        String category = intent.getStringExtra(NewsListViewAdapter.EXT_CATEGORY);
        String published = intent.getStringExtra(NewsListViewAdapter.EXT_PUBLISHED);
        String orgUrl = intent.getStringExtra(NewsListViewAdapter.EXT_URL);
        String description = intent.getStringExtra(NewsListViewAdapter.EXT_DESC);
        String author = intent.getStringExtra(NewsListViewAdapter.EXT_AUTHOR);
        String img = intent.getStringExtra(NewsListViewAdapter.EXT_IMAGE);
        String userSeq = intent.getStringExtra(NewsListViewAdapter.EXT_USER_SEQ);
        titleTxt.setText(title);
        if(author.length() > 12)
            authorTxt.setText(author.substring(0, 12));
        else
            authorTxt.setText(author);
        if(category.length() > 12)
        categoryTxt.setText(category);
        publishedTxt.setText(published);
        descTxt.setText(description);
        if(img.equals("None"))
        {
            imgView.setImageResource(R.drawable.no);
        }
        else
        {
            Glide.with(NewsDetailActivity.this).load(img).into(imgView);
        }
        presenter.doScrapCheck(title, userSeq);
        scrap.setOnClickListener(view -> {
            if(checked) {
                presenter.onUndoBtnTouched(title, userSeq);
            }
            else {
                presenter.onScrapBtnTouched(title,  author, category, published, description, orgUrl, img, userSeq);
            }
        });
        readFull.setOnClickListener(view -> {
            Intent redirect = new Intent(Intent.ACTION_VIEW, Uri.parse(orgUrl));
            startActivity(redirect);
        });
        myPageBtn.setOnClickListener(view -> {
            Intent intent2 = new Intent(NewsDetailActivity.this, MyPageActivity.class);
            intent2.putExtra(EXT_USER_SEQ, userSeq);
            intent2.putExtra(EXT_CATEGORY, category);
            startActivity(intent2);
        });
    }

    @Override
    public void showResult(ArrayList<News> response, String type) {
            if(type == "true") {
                    NewsDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checked = true;
                            scrap.setText("UNDO SCRAP");
                        }
                    });
            }
            else if(type == "insertYes") {
                    NewsDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checked = true;
                        scrap.setText("UNDO SCRAP");
                        Toast.makeText(NewsDetailActivity.this, "스크랩 성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(type == "insertNo") {
                NewsDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsDetailActivity.this, "스크랩 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(type == "deleteYes") {
                NewsDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checked = false;
                        scrap.setText("SCRAP");
                        Toast.makeText(NewsDetailActivity.this, "스크랩 해제", Toast.LENGTH_SHORT).show();

                    }
                });
            }
    }
}