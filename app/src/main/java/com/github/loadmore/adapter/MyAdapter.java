package com.github.loadmore.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    Handler handler;
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
    private int pageSize;
    /***是否还有更多数据,没有更多数据显示"暂无更多"*/
    private boolean hasMoreData = false;
    /*** 加载是否失败,用于点击重新加载*/
    private boolean isLoadError;
    /*** 是否隐藏暂无内容的提示*/
    private boolean isHiddenPromptView = false;

    LayoutInflater inflater;
    Context context;
    List<String> list;

    /**
     * pageSize setList或addList size如果小于pageSize,标记没有更多数据了
     *
     * @param context
     * @param pageSize
     */
    public MyAdapter(Context context, int pageSize) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.pageSize = pageSize;
    }

    public void setHiddenPromptView(boolean hiddenPromptView) {
        isHiddenPromptView = hiddenPromptView;
    }

    /**
     * 加载是否失败
     *
     * @return
     */
    public boolean isLoadError() {
        return isLoadError;
    }
    public void setLoadError(boolean loadError) {
        isLoadError = loadError;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    public void setList(List<String> list) {
        setList(list, false);
    }

    public void setList(List<String> list, boolean isNotifyData) {
        if (list == null || list.size() == 0 || list.size() < pageSize) {
            hasMoreData = false;
        }else{
            hasMoreData = true;
        }
        this.list = list;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    public void addList(List<String> list) {
        addList(list, false);
    }

    public void addList(List<String> list, boolean isNotifyData) {
        if (list == null || list.size() == 0) {
            hasMoreData = false;
        } else if (list.size() < pageSize) {
            hasMoreData = false;
            this.list.addAll(list);
        }else{
            hasMoreData = true;
            this.list.addAll(list);
        }
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (viewType == normal_view) {//正常item view  viewType == normal_view
            View view = inflater.inflate(R.layout.item_layout, parent, false);
            viewHolder = new ViewHolder(view);
            return viewHolder;
        } else {
            LoadMoreView loadMoreView = new LoadMoreView(context);
            loadMoreView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height=110;
            TextView textView = new TextView(context);
            switch (viewType) {
                case load_more_view_type://加载更多view
                    textView.setText("正在加载更多...");
                    break;
                case no_more_view_type://暂无更多view
                    if(isHiddenPromptView){
                        layoutParams.height=0;
                    }
                    textView.setText("暂无更多");
                    break;
                case load_error_view_type://加载失败view
                    textView.setText("加载失败,点击重试");
                    break;
            }
            loadMoreView.setLayoutParams(layoutParams);
            loadMoreView.setViewTag(load_more_view_type);
            loadMoreView.addView(textView);
            loadMoreView.setGravity(Gravity.CENTER);
            if(isHiddenPromptView){
                loadMoreView.setVisibility(View.GONE);
            }else{
                loadMoreView.setVisibility(View.VISIBLE);
            }
            viewHolder = new ViewHolder(loadMoreView);
            return viewHolder;
        }
    }
    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, int position) {
        Log.i("============","============"+position);
        if(position<=getItemCount()-2){
            holder.textView.setText(list.get(position));
        }else{
            if(onLoadMoreListener!=null){
                switch (holder.getItemViewType()){
                    case load_more_view_type:
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                isLoadError=false;
                                onLoadMoreListener.loadMore();
                            }
                        });
                        break;
                    case load_error_view_type:
                        holder.loadMoreView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView childAt = (TextView) holder.loadMoreView.getChildAt(0);
                                childAt.setText("正在加载更多...");
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        isLoadError=false;
                                        onLoadMoreListener.loadMore();
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        }

    }
    @Override
    public int getItemViewType(int position) {
        if(list!=null&&onLoadMoreListener!=null&&position==getItemCount()-1){
            if(isLoadError){
                return load_error_view_type;
            }else if(hasMoreData){
                return load_more_view_type;
            }else{
                return no_more_view_type;
            }
        }
        return normal_view;
    }
    @Override
    public int getItemCount() {
        if(onLoadMoreListener!=null){
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

    public Handler getHandler(){
        if(handler==null){
            handler=new Handler();
        }
        return handler;
    }
}
