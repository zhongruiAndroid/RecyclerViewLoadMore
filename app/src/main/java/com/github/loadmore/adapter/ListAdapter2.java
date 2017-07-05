package com.github.loadmore.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.loadmore.R;
import com.github.loadmore.inter.OnLoadMoreListener;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ListAdapter2 extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<String>list;
    private ListView listView;

    private OnLoadMoreListener onLoadMoreListener;
    private BottomView loadView;
    private BottomView loadErrorView;
    private BottomView noMoreView;
    public ListAdapter2(Context context,ListView listView) {
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.listView=listView;

        loadView=  setDefaultView(1);
        this.listView.addFooterView(loadView);
    }
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    private String loadViewText="正在加载更多...";
    private String noMoreViewText="暂无更多";
    private String errorViewText="加载失败,点击重试";

    private BottomView setDefaultView(int viewType) {
         BottomView bottomView = new  BottomView(mContext);
        bottomView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        bottomView.setGravity(Gravity.CENTER);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomView.setLayoutParams(layoutParams);

        TextView textView = new TextView(mContext);
        switch (viewType) {
            case 1://加载更多view
                    layoutParams.height=dip2px(mContext,50);
                    textView.setText(loadViewText);
                    bottomView.addView(textView);
//                    loadView=bottomView;
                break;
            case 0://暂无更多view
                    layoutParams.height=dip2px(mContext,50);
                    textView.setText(noMoreViewText);
                    bottomView.addView(textView);
//                    noMoreView=bottomView;
                break;
            case 2://加载失败view
                    layoutParams.height=dip2px(mContext,50);
                    textView.setText(errorViewText);
                    bottomView.addView(textView);
//                    loadErrorView=bottomView;
                break;
        }
        return bottomView;
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
//            this.onLoadMoreListener.loadMore();
        }
        holder.textView.setText(list.get(position));

        return view;
    }

    private class ViewHolder{
        TextView textView;
    }


    public static class BottomView extends LinearLayout {
        public BottomView(Context context) {
            super(context);
        }
        public BottomView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }
        public BottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    }
}
