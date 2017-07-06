package com.github.loadmore.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public abstract class LoadMoreLAdapter<T> extends BaseAdapter {
    /*正常view item*/
    private final int normal_view = 2000;
    /*显示加载更多*/
    private final int load_more_view_type = 1000;
    /*暂无更多数据*/
    private final int no_more_view_type = 1001;
    /*加载失败*/
    private final int load_error_view_type = 1002;
    /*回调方法,触发加载更多*/
    private OnLoadMoreListener onLoadMoreListener;
    /***用于判断是否还有更多数据*/
    private int pageSize;
    /***是否还有更多数据,没有更多数据显示"暂无更多"*/
    private boolean hasMoreData = false;
    /*** 加载是否失败,用于点击重新加载*/
    private boolean isLoadError;
    private View loadView,errorView,noMoreView;
    private String loadViewText="正在加载更多...";
    private String noMoreViewText="暂无更多内容";
    private String errorViewText="加载失败,点击重试";

    private boolean isEndLoad=true;

    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected final int mItemLayoutId;
    private ListView listView;

    public LoadMoreLAdapter(Context context, int itemLayoutId,ListView listView, int pageSize ) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = itemLayoutId;
        this.pageSize=pageSize;
        this.listView=listView;

        loadView= getFootView(load_more_view_type);

        errorView= getFootView(load_error_view_type);

        noMoreView= getFootView(no_more_view_type);

    }
    public void setList(List<T> list) {
        setList(list,false);
    }
    public void setList(List<T> list,boolean isNotifyData) {
        isLoadError=false;
        if (list == null || list.size() == 0 || list.size() < pageSize) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
        this.mList = list;
        if(isNotifyData){
            notifyDataSetChanged();
        }
    }
    public void addList(List<T> list) {
        addList(list, false);
    }
    public void addList(List<T> list, boolean isNotifyData) {
        if (list == null || list.size() == 0) {
            hasMoreData = false;
        } else if (list.size() < pageSize) {
            hasMoreData = false;
            this.mList.addAll(list);
        }else{
            hasMoreData = true;
            this.mList.addAll(list);
        }
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }
    public List<T> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        isEndLoad=true;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position==getCount()-2&&getCount()!=0&&onLoadMoreListener!=null&&isEndLoad&&!isLoadError){
            if(hasMoreData){
                isEndLoad=false;
                addFooterView(loadView);
                this.onLoadMoreListener.loadMore();
            }else{
                if(getCount()!=0){
                    addFooterView(noMoreView);
                }
            }
        }
        ViewHolder holder = getViewHolder(position, convertView,parent);

        convert(holder, getItem(position));

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T item);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,position);
    }

    private void addFooterView(View view){
        if(getFooterViewsCount()==0){
            listView.addFooterView(view);
        }
    }
    private void removeFooterView(View view){
        listView.removeFooterView(view);
    }
    private int getFooterViewsCount( ){
        return listView.getFooterViewsCount();
    }
    public View getFootView(int viewType){
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        layoutParams.height=dip2px(mContext,50);
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(layoutParams);
        switch (viewType){
            case load_more_view_type:
                textView.setText(loadViewText);
                break;
            case no_more_view_type:
                textView.setText(noMoreViewText);
                break;
            case load_error_view_type:
                textView.setText(errorViewText);
                break;
        }
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public interface OnLoadMoreListener {
        void loadMore();
    }
    /*是否隐藏底部暂无内容的view*/
    public void setHiddenPromptView() {
        removeFooterView(noMoreView);
    }
    /*是否加载失败*/
    public void setLoadError() {
        removeFooterView(loadView);
        addFooterView(errorView);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFooterView(errorView);
                addFooterView(loadView);
                isEndLoad=false;
                onLoadMoreListener.loadMore();
            }
        });
    }
    /*是否还有更多数据*/
    public void setNoMoreData() {
        this.hasMoreData = false;
        removeFooterView(loadView);
        addFooterView(noMoreView);
    }
    /*设置正在加载的view*/
    public void setLoadView(View loadView) {
        this.loadView = loadView;
    }
    /*设置加载失败的view*/
    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }
    /*设置没有更多数据的view*/
    public void setNoMoreView(View noMoreView) {
        this.noMoreView = noMoreView;
    }
    /*默认BottomView的情况下，设置正在加载的文字*/
    public void setLoadViewText(String loadViewText) {
        this.loadViewText = loadViewText;
    }
    /*默认BottomView的情况下，设置加载失败的文字*/
    public void setErrorViewText(String errorViewText) {
        this.errorViewText = errorViewText;
    }
    /*默认BottomView的情况下，设置没有更多数据的文字*/
    public void setNoMoreViewText(String noMoreViewText) {
        this.noMoreViewText = noMoreViewText;
    }
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public class BottomView extends LinearLayout {
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
