package com.github.loadmore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.loadmore.R;
import com.github.loadmore.inter.OnLoadMoreListener;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String>list;


    private OnLoadMoreListener onLoadMoreListener;

    public ListAdapter(Context context) {
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    public void setList(List<String> list) {
        this.list = list;
    }
    public void addList(List<String> list) {
        this.list.addAll(list);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view=inflater.inflate(R.layout.item_layout,null);
            holder.textView= (TextView) view.findViewById(R.id.item_title);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        if (position==list.size()-2){
            this.onLoadMoreListener.loadMore();
        }
        holder.textView.setText(list.get(position));

        return view;
    }

    private class ViewHolder{
        TextView textView;
    }
}
