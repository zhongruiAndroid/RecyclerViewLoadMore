package com.github.loadmore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.github.loadmore.adapter.ListAdapter;
import com.github.loadmore.inter.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity implements OnLoadMoreListener,View.OnClickListener{

    ListView listView;
    List<String>list=new ArrayList<>();
    private ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView= (ListView) findViewById(R.id.listview);
        listAdapter=new ListAdapter(this);
        listAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                listAdapter.addList(list);
                listAdapter.notifyDataSetChanged();
                Log.i("======","==========loadMore");
            }
        });
        for (int i = 0; i < 12; i++) {
            list.add("第"+i+"个item");
        }
        listAdapter.setList(list);
        listView.setAdapter(listAdapter);


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
