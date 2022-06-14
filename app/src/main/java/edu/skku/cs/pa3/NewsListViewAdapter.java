package edu.skku.cs.pa3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.cs.pa3.model.News;
import edu.skku.cs.pa3.view.NewsDetailActivity;

public class NewsListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<News> items;
    private String userSeq;

    public NewsListViewAdapter(Context mContext, ArrayList<News> items, String seq) {
        this.mContext = mContext;
        this.items = items;
        this.userSeq = seq;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public final static String EXT_TITLE = "TITLE";
    public final static String EXT_DESC = "DESC";
    public final static String EXT_URL = "URL";
    public final static String EXT_AUTHOR = "AUTHOR";
    public final static String EXT_IMAGE = "IMAGE";
    public final static String EXT_LANG = "LANG";
    public final static String EXT_CATEGORY = "CATEGORY";
    public final static String EXT_PUBLISHED = "PUBLISHED";
    public final static String EXT_USER_SEQ = "SEQ";




    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.news_listview, viewGroup, false);
        }

        TextView titleTxt  = (TextView)view.findViewById(R.id.titleTxt2);
        //TextView authorTxt  = (TextView)view.findViewById(R.id.authorTxt);
        //Button detailBtn = (Button) view.findViewById(R.id.detailBtn);
        Button detailBtn = (Button) view.findViewById(R.id.detailBtn);
        String id = items.get(i).getId();
        String author = (items.get(i).getAuthor() == null)? " " : items.get(i).getAuthor();
        String title = (items.get(i).getTitle() == null)? " ": items.get(i).getTitle();
//        if(title.length() > 50)
//            title = title.substring(0, 50);
        titleTxt.setText(title);
        //authorTxt.setText(author);

        detailBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(mContext, NewsDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            System.out.println(items.get(i).getImage());
            intent.putExtra(EXT_TITLE, items.get(i).getTitle());
            intent.putExtra(EXT_DESC, items.get(i).getDescription());
            intent.putExtra(EXT_URL, items.get(i).getUrl());
            intent.putExtra(EXT_AUTHOR, items.get(i).getAuthor());
            intent.putExtra(EXT_IMAGE, items.get(i).getImage());
            intent.putExtra(EXT_LANG, items.get(i).getLanguage());
            intent.putExtra(EXT_CATEGORY, items.get(i).getCategory()[0]);
            intent.putExtra(EXT_PUBLISHED, items.get(i).getPublished());
            intent.putExtra(EXT_USER_SEQ, this.userSeq);

            mContext.startActivity(intent);
        });
        return view;

    }
}
