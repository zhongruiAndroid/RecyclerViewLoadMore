package com.github.loadmore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.github.loadmore.adapter.LoadMoreAdapter;
import com.github.loadmore.adapter.LoadMoreViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoadMoreAdapter.OnLoadMoreListener,View.OnClickListener{

    RecyclerView recyclerview;
    Button bt_listview;
    LoadMoreAdapter adapter;
    int  flag=0;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_listview= (Button) findViewById(R.id.bt_listview);
        bt_listview.setOnClickListener(this);
        recyclerview= (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerview.setLayoutManager(new GridLayoutManager(this,3));
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        adapter= new LoadMoreAdapter<String>(this, 10) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_layout;
            }

            @Override
            public void bindData(LoadMoreViewHolder holder, int position, String item) {
                holder.setText(R.id.item_title,item);
            }
        };

        adapter.setList(getList());
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_nomore_view, null);
//        adapter.setNoMoreView(inflate);
        View inflate2 = LayoutInflater.from(this).inflate(R.layout.item_load_view, null);
//        adapter.setLoadView(inflate2);
        View inflate3 = LayoutInflater.from(this).inflate(R.layout.item_error_view, null);
//        adapter.setErrorView(inflate3);
        recyclerview.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
    }

    private List getList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            list.add("第"+i+"个item");
        }
        return list;
    }

    @Override
    public void loadMore() {
        recyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(flag==2){
                    return;
//                    adapter.setHasMoreData(false);
//                    adapter.notifyDataSetChanged();
                }else if(flag%3==0){
                    adapter.addList(getList());
                    adapter.notifyDataSetChanged();
                }else if(flag%3==1){
                    adapter.setLoadError(true);
                    adapter.notifyDataSetChanged();
                }else{
                    adapter.addList(getList());
                    adapter.notifyDataSetChanged();
                }
                flag++;
            }
        },2000);
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
