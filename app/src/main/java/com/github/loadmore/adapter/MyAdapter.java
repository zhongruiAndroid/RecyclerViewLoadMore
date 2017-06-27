package com.github.loadmore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.loadmore.R;
import com.github.loadmore.inter.OnLoadMoreListener;
import com.github.loadmore.view.LoadMoreView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final int load_more_view_type=100;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean hasMoreData=false;
    LayoutInflater inflater;
    Context context;
    List<String> list;

    public MyAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
    public void addList(List<String> list) {
        this.list.addAll(this.list);
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if(viewType==load_more_view_type){
            LoadMoreView loadMoreView=new LoadMoreView(context);
            TextView textView=new TextView(context);
            textView.setText("正在加载...");
            loadMoreView.addView(textView);
            loadMoreView.setGravity(Gravity.CENTER);
            viewHolder=new ViewHolder(loadMoreView);
        }else{
            View view = inflater.inflate(R.layout.item_layout, parent, false);
            viewHolder=new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==list.size()){
            return load_more_view_type;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(onLoadMoreListener!=null&&hasMoreData){
            return list==null?0:list.size()+1;
        }else{
            return list==null?0:list.size();
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        LoadMoreView loadMoreView;
        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView instanceof LoadMoreView){
                loadMoreView= (LoadMoreView) itemView;
            }else{
                textView= (TextView) itemView.findViewById(R.id.item_title);
            }
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }
}
