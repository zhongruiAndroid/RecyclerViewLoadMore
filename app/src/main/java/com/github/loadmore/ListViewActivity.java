package com.github.loadmore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.loadmore.adapter.ListAdapter;
import com.github.loadmore.adapter.LoadMoreLAdapter;
import com.github.loadmore.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity implements  View.OnClickListener{
    public class AA{

    }
    int flag;
    ListView lv_list;

    private ListAdapter listAdapter;
    private LoadMoreLAdapter lAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        lv_list=(ListView) findViewById(R.id.lv_list);
        listAdapter=new ListAdapter(this);
        lAdapter=new LoadMoreLAdapter<String>(this,R.layout.item_layout,lv_list,10) {
            @Override
            public void convert(ViewHolder viewHolder, String s) {
                viewHolder.setText(R.id.item_title,s);
            }
        };
        lAdapter.setList(getList());
        lv_list.setAdapter(lAdapter);
        AA a=new AA();
        listAdapter.setA(a);
        listAdapter.setListView(lv_list);
        listAdapter.setActivity(this);
        listAdapter.setList(null);
        lAdapter.setOnLoadMoreListener(new LoadMoreLAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {

                lv_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("=========","----------loadMore");
                        if(flag==4){
                            lAdapter.setNoMoreData();
                        }else if(flag%3==0){
                            lAdapter.addList(getList());
                            lAdapter.notifyDataSetChanged();
                        }else if(flag%3==1){
                            lAdapter.setLoadError();
                        }else{
                            lAdapter.addList(getList());
                            lAdapter.notifyDataSetChanged();
                        }
                        flag++;
                    }
                },1500);
            }
        });
//        adapter.setList(list);
//        lv_list.setAdapter(listAdapter);
        final TextView textView=new TextView(ListViewActivity.this);
        textView.setText("textView");

        final TextView textView1=new TextView(ListViewActivity.this);
        textView1.setText("textView1");
        textView1.setVisibility(View.GONE);

        TextView textView2=new TextView(ListViewActivity.this);
        textView2.setText("textView2");
        textView2.setVisibility(View.INVISIBLE);


//        lv_list.removeFooterView(null);
//        lv_list.addFooterView(textView1);
//        lv_list.addFooterView(textView2);
        /*lv_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_list.addFooterView(textView);
                textView1.setVisibility(View.VISIBLE);
            }
        },3000);
        lv_list.postDelayed(new Runnable() {
            @Override
            public void run() {
//                lv_list.removeFooterView(textView);
            }
        },6000);*/
        lv_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean mIsEnd = false;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i("=====","==========1=====");
                if (scrollState == SCROLL_STATE_IDLE) {
                    Log.i("=====","=========2======");
                    if (mIsEnd) {
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("=====",firstVisibleItem+"======="+visibleItemCount+"========"+totalItemCount);
                if (firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
                    mIsEnd = true;
                } else {
                    mIsEnd = false;
                }
            }
        });
    }

    @NonNull
    private List<String> getList() {
        List<String>list=new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            list.add("第"+i+"个item");
        }
        return list;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_listview:
                
                break;
        }
    }
}
