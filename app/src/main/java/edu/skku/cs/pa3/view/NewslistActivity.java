package edu.skku.cs.pa3.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.skku.cs.pa3.NewsListViewAdapter;
import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.contract.NewsContract;
import edu.skku.cs.pa3.model.News;
import edu.skku.cs.pa3.presenter.NewsPresenter;

public class NewslistActivity extends AppCompatActivity implements NewsContract.contractForView {

    private ArrayList<News> newsList;
    private ListView listView;
    private Button searchBtn;
    private Button myPageBtn;
    private EditText searchBox;
    private String keyword;
    private String userSeq;
    private String name;
    private String category;
    public final static String EXT_USER_SEQ = "SEQ";
    public final static String EXT_CATEGORY = "CATEGORY";
    private NewsContract.contractForPresenter presenter;
    private ImageView imageView;
    private String choosed = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);
        presenter = new NewsPresenter(this);
        initView();
    }

    public void getIntentContent()
    {
        Intent intent = getIntent();
        userSeq = intent.getStringExtra(MainActivity.EXT_USER_SEQ);
        name = intent.getStringExtra(MainActivity.EXT_NAME);
        category = intent.getStringExtra(MainActivity.EXT_CATEGORY);
        if(category == null)
            category = intent.getStringExtra(MyPageActivity.EXT_CATEGORY);
        if(userSeq == null)
            userSeq = intent.getStringExtra(MyPageActivity.EXT_USER_SEQ);
    }

    public void initView() {
        getIntentContent();
        listView = findViewById(R.id.listview);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        myPageBtn = (Button) findViewById(R.id.myPage);
        searchBox = (EditText) findViewById(R.id.searchBox);
        imageView = (ImageView) findViewById(R.id.imageView2);

        presenter.doInitSearch(category);
        searchBtn.setOnClickListener(view -> {
            keyword = searchBox.getText().toString();
            presenter.onSearchBtnTouched(keyword, "", choosed);
        });
        myPageBtn.setOnClickListener(view -> {
            Intent intent2 = new Intent(NewslistActivity.this, MyPageActivity.class);
            intent2.putExtra(EXT_USER_SEQ, userSeq);
            intent2.putExtra(EXT_CATEGORY, category);
            startActivity(intent2);
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            String lang = "";
            @Override
            public void onClick(View view) {
                final String[] items = new String[]{"영어", "중국어", "일본어", "프랑스어", "스페인어", "아랍어"};
                final String[] mappedItems = new String[]{"en", "zh", "ja", "fr", "es", "ar"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(NewslistActivity.this);
                dialog.setTitle("기사 작성 언어를 선택하세요")
                        .setSingleChoiceItems(items
                                , -1
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        System.out.println(lang);
                                        lang = mappedItems[i];
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(NewslistActivity.this, "언어가 선택되었습니다", Toast.LENGTH_SHORT).show();
                                choosed = lang;
                                presenter.onSearchBtnTouched(keyword, category, choosed);
                            }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(NewslistActivity.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
                        choosed = "";
                    }
                });
                dialog.create();
                dialog.show();
            }
        });

    }

    public void buttonClicked(View view)
    {
        Button clickedBtn = (Button) findViewById(view.getId());
        category = clickedBtn.getText().toString();
        String category = clickedBtn.getText().toString();
        presenter.onSearchBtnTouched(keyword, category, choosed);
    }

    @Override
    public void showResult(ArrayList<News> response, String type) {
        if(type == "init") {
            NewsListViewAdapter newsListViewAdapter = new NewsListViewAdapter(getApplicationContext(), response, userSeq);
            NewslistActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(newsListViewAdapter);
                }
            });
        }
        else if(type == "fail")
        {
            NewslistActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(NewslistActivity.this, "API 검색이 지연되고 있습니다. 잠시 기다렸다가 사용해주세요", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            if(response.size() == 0){
            NewslistActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(NewslistActivity.this, "검색 하신 뉴스 기사는 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            }

            NewsListViewAdapter newsListViewAdapter = new NewsListViewAdapter(getApplicationContext(), response, userSeq);
            NewslistActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(newsListViewAdapter);
                }
            });
        }
    }
}
