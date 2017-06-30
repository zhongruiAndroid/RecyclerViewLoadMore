package com.github.loadmore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.loadmore.adapter.LoadMoreAdapter;
import com.github.loadmore.adapter.LoadMoreViewHolder;
import com.github.loadmore.inter.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity implements OnLoadMoreListener,View.OnClickListener{

    int flag;
    RecyclerView recycleListView;
    List<String>list=new ArrayList<>();
    private LoadMoreAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recycleListView=(RecyclerView) findViewById(R.id.recycleListView);
        recycleListView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new LoadMoreAdapter<String>(this, 10) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_layout;
            }
            @Override
            public void bindData(LoadMoreViewHolder holder, int position, String bean) {
                holder.setText(R.id.item_title,bean);
            }
        };
        adapter.setOnLoadMoreListener(new LoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                recycleListView.postDelayed(new Runnable() {
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
        });
        for (int i = 0; i < 12; i++) {
            list.add("第"+i+"个item");
        }
        adapter.setList(list);
        recycleListView.setAdapter(adapter);
    }
    @Override
    public void loadMore() {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_listview:
                
                break;
        }
    }
}
