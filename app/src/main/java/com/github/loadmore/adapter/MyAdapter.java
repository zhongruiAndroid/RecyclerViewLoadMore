package com.github.loadmore.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.loadmore.R;
import com.github.loadmore.inter.OnLoadMoreListener;
import com.github.loadmore.view.BottomView;

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

    /***用于判断是否还有更多数据*/
    private int pageSize;
    /***是否还有更多数据,没有更多数据显示"暂无更多"*/
    private boolean hasMoreData = false;
    /*** 加载是否失败,用于点击重新加载*/
    private boolean isLoadError;
    /*** 是否隐藏暂无内容的提示*/
    private boolean isHiddenPromptView = false;

    private View loadView,errorView,noMoreView;
    private String loadViewText,errorViewText,noMoreViewText;

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
            viewHolder = new ViewHolder(setDefaultView(viewType));
            return viewHolder;
        }
    }

    private View setDefaultView(int viewType) {
        BottomView bottomView = new BottomView(context);
        bottomView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        bottomView.setGravity(Gravity.CENTER);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomView.setLayoutParams(layoutParams);

        TextView textView = new TextView(context);
        switch (viewType) {
            case load_more_view_type://加载更多view
                if(loadView!=null){
                    bottomView.addView(loadView);
                }else{
                    layoutParams.height=dip2px(context,50);
                    textView.setText(TextUtils.isEmpty(loadViewText)?"正在加载更多...":loadViewText);
                    bottomView.addView(textView);
                }
                break;
            case no_more_view_type://暂无更多view
                if(noMoreView!=null){
                    bottomView.addView(noMoreView);
                }else{
                    layoutParams.height=dip2px(context,50);
                    textView.setText(TextUtils.isEmpty(noMoreViewText)?"暂无更多":noMoreViewText);
                    bottomView.addView(textView);
                }
                if(isHiddenPromptView){
                    layoutParams.height=0;
                }
                break;
            case load_error_view_type://加载失败view
                if(errorView!=null){
                    bottomView.addView(errorView);
                }else{
                    layoutParams.height=dip2px(context,50);
                    textView.setText(TextUtils.isEmpty(errorViewText)?"加载失败,点击重试":errorViewText);
                    bottomView.addView(textView);
                }
                break;
        }
        return bottomView;
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, int position) {
        if(position<=getItemCount()-2){
            holder.textView.setText(list.get(position));
        }else{
            if(onLoadMoreListener!=null){
                switch (holder.getItemViewType()){
                    case load_more_view_type:
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                onLoadMoreListener.loadMore();
                            }
                        });
                        break;
                    case load_error_view_type:
                        holder.bottomView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isLoadError=false;
                                hasMoreData=true;
                                notifyDataSetChanged();
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
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
    public void setHiddenPromptView(boolean hiddenPromptView) {
        isHiddenPromptView = hiddenPromptView;
    }
    public void setLoadError(boolean loadError) {
        isLoadError = loadError;
    }
    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
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
        BottomView bottomView;
        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView instanceof BottomView){
                bottomView = (BottomView) itemView;
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

    public void setLoadView(View loadView) {
        this.loadView = loadView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setNoMoreView(View noMoreView) {
        this.noMoreView = noMoreView;
    }

    public void setLoadViewText(String loadViewText) {
        this.loadViewText = loadViewText;
    }

    public void setErrorViewText(String errorViewText) {
        this.errorViewText = errorViewText;
    }

    public void setNoMoreViewText(String noMoreViewText) {
        this.noMoreViewText = noMoreViewText;
    }
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
