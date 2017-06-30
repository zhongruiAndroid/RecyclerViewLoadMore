package com.github.loadmore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    boolean flag;
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

        for (int i = 0; i < 2; i++) {
            list.add("第"+i+"个item");
        }
        adapter.setList(list);
        recyclerview.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
    }

    @Override
    public void loadMore() {
        if(flag){
            recyclerview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.addList(null);
                    adapter.setHasMoreData(false);
                    adapter.notifyDataSetChanged();
                }
            },2000);
           return;
        }
        flag=true;
//        adapter.setHiddenPromptView(true);
        adapter.addList(list);
        adapter.setLoadError(true);
        adapter.notifyDataSetChanged();
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
