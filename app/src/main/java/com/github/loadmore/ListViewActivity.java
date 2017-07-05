package com.github.loadmore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.github.loadmore.adapter.LoadMoreLAdapter;
import com.github.loadmore.adapter.LoadMoreLViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity implements LoadMoreLAdapter.OnLoadMoreListener,View.OnClickListener{

    int flag;
    ListView lv_list;
    List<String>list=new ArrayList<>();
    private LoadMoreLAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        lv_list=(ListView) findViewById(R.id.lv_list);
        adapter= new LoadMoreLAdapter<String>(this,R.layout.item_layout,10) {
            @Override
            public void convert(LoadMoreLViewHolder loadMoreLViewHolder, String o) {
                loadMoreLViewHolder.setText(R.id.item_title,o);
            }
        };
        adapter.setOnLoadMoreListener(new LoadMoreLAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                lv_list.postDelayed(new Runnable() {
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
                },0);
            }
        });
        for (int i = 0; i < 12; i++) {
            list.add("第"+i+"个item");
        }
        adapter.setList(list);
        lv_list.setAdapter(adapter);
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
