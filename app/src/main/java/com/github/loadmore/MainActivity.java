package com.github.loadmore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.loadmore.adapter.MyAdapter;
import com.github.loadmore.inter.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnLoadMoreListener{

    RecyclerView recyclerview;
    MyAdapter adapter;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter=new MyAdapter(this,10);final List<String> list=new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add("第"+i+"个item");
        }
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                Log.i("=========",recyclerview.getScrollState()+"==========loadMore"+recyclerview.isComputingLayout());
                if(flag){
                    return;
                }adapter.notifyDataSetChanged();
                recyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("=========",recyclerview.getScrollState()+"==========loadMorepostDelayed"+recyclerview.isComputingLayout());
                    }
                },200);
               /* flag=true;
                adapter.setHiddenPromptView(true);
                adapter.addList(list);
                adapter.setHasMoreData(false);
                recyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                    }
                },200);*/
            }
        });

        adapter.setList(list);
        recyclerview= (RecyclerView) findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        recyclerview.setAdapter(adapter);

    }

    @Override
    public void loadMore() {

    }
}
