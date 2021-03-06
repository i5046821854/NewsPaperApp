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
                final String[] items = new String[]{"??????", "?????????", "?????????", "????????????", "????????????", "?????????"};
                final String[] mappedItems = new String[]{"en", "zh", "ja", "fr", "es", "ar"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(NewslistActivity.this);
                dialog.setTitle("?????? ?????? ????????? ???????????????")
                        .setSingleChoiceItems(items
                                , -1
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        System.out.println(lang);
                                        lang = mappedItems[i];
                                    }
                                })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(NewslistActivity.this, "????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                                choosed = lang;
                                presenter.onSearchBtnTouched(keyword, category, choosed);
                            }
                        }).setNeutralButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(NewslistActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NewslistActivity.this, "API ????????? ???????????? ????????????. ?????? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            if(response.size() == 0){
            NewslistActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(NewslistActivity.this, "?????? ?????? ?????? ????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
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
