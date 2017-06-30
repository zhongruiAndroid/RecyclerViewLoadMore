package com.github.loadmore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.github.loadmore.adapter.MyAdapter;
import com.github.loadmore.inter.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnLoadMoreListener,View.OnClickListener{

    RecyclerView recyclerview;
    Button bt_listview;
    MyAdapter adapter;
    int  flag=0;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_listview= (Button) findViewById(R.id.bt_listview);
        bt_listview.setOnClickListener(this);
        recyclerview= (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        adapter=new MyAdapter(this,10);

        list = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            list.add("第"+i+"个item");
        }
        adapter.setList(list);
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_nomore_view, null);
        adapter.setNoMoreView(inflate);
        View inflate2 = LayoutInflater.from(this).inflate(R.layout.item_load_view, null);
        adapter.setLoadView(inflate2);
        View inflate3 = LayoutInflater.from(this).inflate(R.layout.item_error_view, null);
        adapter.setErrorView(inflate3);
        recyclerview.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
    }

    @Override
    public void loadMore() {
        recyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(flag==4){
                    adapter.setHasMoreData(false);
                    adapter.notifyDataSetChanged();
                }else if(flag%3==0){
                    adapter.addList(list);
                    adapter.notifyDataSetChanged();
                }else if(flag%3==1){
                    adapter.setLoadError(true);
                    adapter.notifyDataSetChanged();
                }else{
                    adapter.addList(list);
                    adapter.notifyDataSetChanged();
                }
                flag++;
            }
        },1300);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_listview:
                    startActivity(new Intent(this,ListViewActivity.class));
                break;
        }
    }
}
